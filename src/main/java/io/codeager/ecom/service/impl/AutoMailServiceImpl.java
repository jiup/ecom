/*
 * Copyright 2018 Jiupeng Zhang
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.codeager.ecom.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.common.base.Strings;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.codeager.ecom.dao.mapper.AutoMailMapper;
import com.codeager.ecom.dao.mapper.MailTemplateMapper;
import com.codeager.ecom.domain.AutoMail;
import com.codeager.ecom.domain.ExtCustomer;
import com.codeager.ecom.domain.MailTemplate;
import com.codeager.ecom.dto.form.MailTemplateForm;
import com.codeager.ecom.service.AutoMailService;
import com.codeager.ecom.service.ExtCustomerService;
import com.codeager.ecom.util.Constant;
import com.codeager.plugin.MailSender;
import com.codeager.portal.util.TokenHelper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.mail.SendFailedException;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

/**
 * @author Jiupeng Zhang
 * @since 01/08/2018
 */
@Service
public class AutoMailServiceImpl extends BaseMailService implements AutoMailService {
    private static final int UNSUBSCRIBE_TOKEN_LENGTH = 32;

    private TemplateEngine templateEngine;
    private MailTemplateMapper mailTemplateMapper;
    private AutoMailMapper autoMailMapper;

    private ExtCustomerService extCustomerService;

    @Autowired
    protected AutoMailServiceImpl(Optional<MailSender> optionalFutureHolder) {
        super(optionalFutureHolder);
    }

    @Autowired
    public void setTemplateEngine(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    @Autowired
    public void setMailTemplateMapper(MailTemplateMapper mailTemplateMapper) {
        this.mailTemplateMapper = mailTemplateMapper;
    }

    @Autowired
    public void setAutoMailMapper(AutoMailMapper autoMailMapper) {
        this.autoMailMapper = autoMailMapper;
    }

    @Autowired
    public void setExtCustomerService(ExtCustomerService extCustomerService) {
        this.extCustomerService = extCustomerService;
    }

    @Override
    public String loadParsedHtml() {
        List<MailTemplate> mailTemplates = mailTemplateMapper.selectAll();
        if (mailTemplates.size() != 1) {
            LOG.warn("Mail template not exists");
            return null;
        }

        MailTemplate mailTemplate = mailTemplates.get(0);

        Context context = new Context();
        context.setVariables(new HashMap<String, Object>() {{
            put("title", mailTemplate.getTitle());
            put("content", mailTemplate.getContent().replaceAll("\n", "<br/>"));
            put("imgUrl", Strings.emptyToNull(mailTemplate.getImgUrl()));
            put("hideUnsubscribeButton", mailTemplate.isHideUnsubscribeButton());
        }});

        return templateEngine.process("templates/" + mailTemplate.getName(), context);
    }

    @Override
    public MailTemplate loadTemplate() {
        List<MailTemplate> mailTemplates = mailTemplateMapper.selectAll();
        if (mailTemplates.size() != 1) {
            LOG.warn("Mail template not exists");
            return null;
        }

        return mailTemplates.get(0);
    }

    @Override
    @Transactional
    public void save(MailTemplateForm form, boolean overwriteImage, String imageUrl) {
        List<MailTemplate> mailTemplates = mailTemplateMapper.selectAll();
        if (mailTemplates.size() == 0) {
            MailTemplate mailTemplate = new MailTemplate();
            BeanUtils.copyProperties(form, mailTemplate);
            if (overwriteImage) {
                mailTemplate.setImgUrl(imageUrl);
            }
            mailTemplate.setCreateTime(ZonedDateTime.now());
            mailTemplate.setUpdateTime(ZonedDateTime.now());
            mailTemplate.setName("automail");
            mailTemplateMapper.insert(mailTemplate);
            LOG.info("Mail template created");
        } else if (mailTemplates.size() > 1) {
            LOG.error("Logic error: mail templates size > 1");
        } else {
            MailTemplate mailTemplate = mailTemplates.get(0);
            BeanUtils.copyProperties(form, mailTemplate);
            if (overwriteImage) {
                mailTemplate.setImgUrl(imageUrl);
            }
            mailTemplate.setUpdateTime(ZonedDateTime.now());
            mailTemplateMapper.updateById(mailTemplate);
            LOG.info("Mail template updated");
        }
    }

    @Override
    @Transactional
    public void delete(MailTemplateForm form) {
        for (MailTemplate mailTemplate : mailTemplateMapper.selectAll()) {
            mailTemplateMapper.softDeleteById(mailTemplate.getId());
        }
    }

    @Override
    public void send(long templateId, String group, long[] receiverIds) {
        // notice: templateId is not used in this version

        switch (group) {
            case "external":
                for (long id : receiverIds) {
                    ExtCustomer extCustomer = extCustomerService.get(id);
                    if (extCustomer != null) {
                        if (extCustomer.isUnsubscribe()) {
                            LOG.warn("Automail to t_customer_ext(id={}) not sent: customer unsubscribe automail for {}", id, extCustomer.getEmail());
                        } else {
                            MailTemplate template = loadTemplate();
                            String unsubscribeToken = TokenHelper.createToken(UNSUBSCRIBE_TOKEN_LENGTH);
                            Context context = new Context();
                            context.setVariables(new HashMap<String, Object>() {{
                                put("title", template.getTitle());
                                put("content", template.getContent().replaceAll("\n", "<br/>"));
                                put("imgUrl", Strings.emptyToNull(template.getImgUrl()));
                                put("hideUnsubscribeButton", template.isHideUnsubscribeButton());
                                put("unsubscribeUrl", Constant.UNSUBSCRIBE_URL_BASE + unsubscribeToken);
                            }});
                            String content = templateEngine.process("templates/" + template.getName(), context);
                            AutoMail autoMail = new AutoMail();
                            autoMail.setType("Automail");
                            autoMail.setGroup(group);
                            autoMail.setSubject(template.getSubject());
                            autoMail.setContent(content);
                            autoMail.setMailTo(extCustomer.getEmail());
                            autoMail.setReceiverId(id);
                            autoMail.setReceiverName(extCustomer.getContactName());
                            autoMail.setToken(unsubscribeToken);
                            send(autoMail);
                        }
                    } else {
                        LOG.warn("Automail to t_customer_ext(id={}) not sent: record not exists", id);
                    }
                }
                break;
            default:
                LOG.error("Send automail failure: no group={} specified", group);
        }
    }

    @Override
    public void send(AutoMail mail) {
        Futures.addCallback(recordAndSend(mail), new FutureCallback<Long>() {
            @Override
            public void onSuccess(@Nullable Long mailId) {
                try {
                    if (mailId != null) {
                        AutoMail autoMail = autoMailMapper.selectById(mailId);
                        if (autoMail != null) {
                            autoMail.setSent(true);
                            autoMail.setUpdateTime(ZonedDateTime.now());
                            autoMailMapper.updateById(autoMail);
                        }
                    }
                } finally {
                    LOG.info("Automail '{}' has been successfully sent to {} <{}>", mail.getSubject(), mail.getReceiverName(), mail.getMailTo());
                }
            }

            @Override
            public void onFailure(@ParametersAreNonnullByDefault Throwable throwable) {
                LOG.info("Automail '{}' to {} <{}> sent failure: {}", mail.getSubject(), mail.getReceiverName(), mail.getMailTo(), throwable.getMessage());
            }
        }, MAIL_SERVICE_EXECUTOR);
    }

    @Transactional
    protected ListenableFuture<Long> recordAndSend(AutoMail mail) {
        mail.setCreateTime(ZonedDateTime.now());
        mail.setUpdateTime(ZonedDateTime.now());
        autoMailMapper.insert(mail);
        return MAIL_SERVICE_EXECUTOR.submit(() -> {
            try {
                futureHolder.sendHtml(mail.getMailTo(), mail.getSubject(), mail.getContent());
            } catch (SendFailedException e) {
                mail.setUpdateTime(ZonedDateTime.now());
                autoMailMapper.updateById(mail);
                throw new RuntimeException(e.getMessage());
            }
        }, mail.getId());
    }

    @Override
    public void resend(AutoMail mail) {
        Futures.addCallback(recordAndResend(mail), new FutureCallback<AutoMail>() {
            @Override
            public void onSuccess(@Nullable AutoMail autoMail) {
                try {
                    if (autoMail != null) {
                        autoMail.setSent(true);
                        autoMail.setUpdateTime(ZonedDateTime.now());
                        autoMailMapper.updateById(autoMail);
                    }
                } finally {
                    LOG.info("Automail '{}' has been successfully resent to {} <{}>", mail.getSubject(), mail.getReceiverName(), mail.getMailTo());
                }
            }

            @Override
            public void onFailure(@ParametersAreNonnullByDefault Throwable throwable) {
                LOG.info("Automail '{}' to {} <{}> resent failure: {}", mail.getSubject(), mail.getReceiverName(), mail.getMailTo(), throwable.getMessage());
            }
        }, MAIL_SERVICE_EXECUTOR);
    }

    @Transactional
    protected ListenableFuture<AutoMail> recordAndResend(AutoMail mail) {
        mail.setCount(mail.getCount() + 1);
        mail.setCreateTime(ZonedDateTime.now());
        mail.setUpdateTime(ZonedDateTime.now());
        mail.setToken(TokenHelper.createToken(32));
        autoMailMapper.updateById(mail);
        return MAIL_SERVICE_EXECUTOR.submit(() -> {
            try {
                futureHolder.sendHtml(mail.getMailTo(), mail.getSubject(), mail.getContent());
            } catch (SendFailedException e) {
                mail.setUpdateTime(ZonedDateTime.now());
                autoMailMapper.updateById(mail);
                throw new RuntimeException(e.getMessage());
            }
        }, mail);
    }

    @Override
    public void delete(AutoMail mail) {
        autoMailMapper.softDeleteById(mail.getId());
    }

    @Override
    @Transactional
    public void unsubscribeByToken(String token) {
        if (!TokenHelper.isValid(token, UNSUBSCRIBE_TOKEN_LENGTH)) {
            LOG.warn("Unsubscribe automail ignored: illegal token '{}'", token);
            return;
        }

        AutoMail autoMail = autoMailMapper.selectByToken(token);
        if (autoMail == null) {
            LOG.error("Unsubscribe automail failure: no automail matched token '{}' found", token);
            return;
        }

        switch (autoMail.getGroup()) {
            case "external":
                extCustomerService.unsubscribeById(autoMail.getReceiverId());
                break;
            default:
                LOG.error("Unsubscribe automail failure: no group={} specified, receiverId={}", autoMail.getGroup(), autoMail.getReceiverId());
        }
    }

    @Override
    public Page<AutoMail> getAll(int pageNum, int pageSize) {
        return PageHelper.startPage(pageNum, pageSize, true).doSelectPage(() -> autoMailMapper.selectAll());
    }
}
