/*
 * Copyright 2017 Jiupeng Zhang
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

package io.codeager.ecom.service.impl;

import com.google.common.base.Strings;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import io.codeager.ecom.dao.mapper.ContactMapper;
import io.codeager.ecom.domain.Contact;
import io.codeager.ecom.dto.form.ContactForm;
import io.codeager.ecom.service.ContactMailService;
import io.codeager.ecom.util.Constant;
import io.codeager.plugin.MailSender;
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
import java.util.Optional;

/**
 * @author Jiupeng Zhang
 * @since 12/26/2017
 */
@Service
public class ContactMailServiceImpl extends BaseMailService implements ContactMailService {

    private TemplateEngine templateEngine;
    private ContactMapper contactMapper;

    @Autowired
    public void setTemplateEngine(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    @Autowired
    public void setContactMapper(ContactMapper contactMapper) {
        this.contactMapper = contactMapper;
    }

    @Autowired
    protected ContactMailServiceImpl(Optional<MailSender> optionalFutureHolder) {
        super(optionalFutureHolder);
    }

    @Override
    public void send(ContactForm form) {
        for (String notifyEmailAddress : Constant.ADMIN_EMAIL_ADDRESSES) {
            send(form, notifyEmailAddress);
        }
    }

    public void send(ContactForm form, String notifyEmailAddress) {
        Futures.addCallback(recordAndSend(form, notifyEmailAddress), new FutureCallback<Long>() {
            @Override
            public void onSuccess(@Nullable Long contactId) {
                try {
                    if (contactId != null) {
                        Contact contact = contactMapper.selectById(contactId);
                        if (contact != null) {
                            contact.setSent(true);
                            contact.setUpdateTime(ZonedDateTime.now());
                            contactMapper.updateById(contact);
                        }
                    }
                } finally {
                    LOG.info("Contact request from '{}' has been successfully sent to {}", form.getFirstName(), notifyEmailAddress);
                }
            }

            @Override
            public void onFailure(@ParametersAreNonnullByDefault Throwable throwable) {
                LOG.info("Contact request from '{}' sent failure: {}", form.getFirstName(), throwable.getMessage());
            }
        }, MAIL_SERVICE_EXECUTOR);
    }

    @Transactional
    protected ListenableFuture<Long> recordAndSend(ContactForm form, String notifyEmailAddress) {
        Contact contact = new Contact();
        BeanUtils.copyProperties(form, contact);
        contact.setContent(form.getMessage());
        contact.setCreateTime(ZonedDateTime.now());
        contact.setUpdateTime(ZonedDateTime.now());
        contactMapper.insert(contact);
        return MAIL_SERVICE_EXECUTOR.submit(() -> {
            try {
                futureHolder.sendHtml(
                        notifyEmailAddress,
                        getSubject(form),
                        renderHtml(form)
                );
            } catch (SendFailedException e) {
                contact.setUpdateTime(ZonedDateTime.now());
                contactMapper.updateById(contact);
                throw new RuntimeException(e.getMessage());
            }
        }, contact.getId());
    }

    private String getSubject(ContactForm form) {
        return "Contact Request from " + (form.getFirstName() + " " + Strings.nullToEmpty(form.getLastName())).trim();
    }

    private String renderHtml(ContactForm form) {
        Context context = new Context();
        context.setVariables(new HashMap<String, Object>() {{
            put("title", "Contact Request from " + (form.getFirstName() + " " + Strings.nullToEmpty(form.getLastName())).trim());
            put("message", form.getMessage());
            put("email", form.getEmail());
        }});
        return templateEngine.process("templates/contact", context);
    }
}
