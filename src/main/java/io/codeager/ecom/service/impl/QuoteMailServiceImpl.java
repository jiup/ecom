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

package com.codeager.ecom.service.impl;

import com.google.common.base.Strings;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.codeager.ecom.dao.mapper.QuoteRequestMapper;
import com.codeager.ecom.domain.QuoteRequest;
import com.codeager.ecom.dto.form.QuoteRequestForm;
import com.codeager.ecom.service.QuoteMailService;
import com.codeager.ecom.util.Constant;
import com.codeager.plugin.MailSender;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.mail.SendFailedException;
import java.time.ZonedDateTime;
import java.util.Optional;

/**
 * @author Jiupeng Zhang
 * @since 12/29/2017
 */
@Service
public class QuoteMailServiceImpl extends BaseMailService implements QuoteMailService {
    private QuoteRequestMapper quoteRequestMapper;

    @Autowired
    public void setQuoteRequestMapper(QuoteRequestMapper quoteRequestMapper) {
        this.quoteRequestMapper = quoteRequestMapper;
    }

    @Autowired
    protected QuoteMailServiceImpl(Optional<MailSender> optionalFutureHolder) {
        super(optionalFutureHolder);
    }

    @Override
    public void send(QuoteRequestForm form) {
        for (String notifyEmailAddress : Constant.ADMIN_EMAIL_ADDRESSES) {
            send(form, notifyEmailAddress);
        }
    }

    protected void send(QuoteRequestForm form, String notifyEmailAddress) {
        Futures.addCallback(recordAndSend(form, notifyEmailAddress), new FutureCallback<Long>() {
            @Override
            public void onSuccess(@Nullable Long quoteRequestId) {
                try {
                    if (quoteRequestId != null) {
                        QuoteRequest quoteRequest = quoteRequestMapper.selectById(quoteRequestId);
                        if (quoteRequest != null) {
                            quoteRequest.setSent(true);
                            quoteRequest.setUpdateTime(ZonedDateTime.now());
                            quoteRequestMapper.updateById(quoteRequest);
                        }
                    }
                } finally {
                    LOG.info("Quote request from '{}' has been successfully sent to {}", form.getFirstName(), notifyEmailAddress);
                }
            }

            @Override
            public void onFailure(@ParametersAreNonnullByDefault Throwable throwable) {
                LOG.info("Quote request from '{}' sent failure: {}", form.getFirstName(), throwable.getMessage());
            }
        }, MAIL_SERVICE_EXECUTOR);
    }

    @Transactional
    protected ListenableFuture<Long> recordAndSend(QuoteRequestForm form, String notifyEmailAddress) {
        QuoteRequest quoteRequest = new QuoteRequest();
        BeanUtils.copyProperties(form, quoteRequest);
        quoteRequest.setAddress(form.getCompanyAddressLineOne());
        quoteRequest.setCity(form.getCompanyAddressCity());
        quoteRequest.setState(form.getCompanyAddressState());
        quoteRequest.setZipCode(form.getCompanyAddressZipCode());
        quoteRequest.setPhone(form.getPhoneNumber());
        quoteRequest.setPreferCall(form.getPreferCallOrText());
        quoteRequest.setProduct(form.getAboutProduct());
        quoteRequest.setCreateTime(ZonedDateTime.now());
        quoteRequest.setUpdateTime(ZonedDateTime.now());
        quoteRequestMapper.insert(quoteRequest);
        return MAIL_SERVICE_EXECUTOR.submit(() -> {
            try {
                futureHolder.sendHtml(
                        notifyEmailAddress,
                        getSubject(form),
                        renderHtml(form)
                );
                quoteRequest.setUpdateTime(ZonedDateTime.now());
                quoteRequestMapper.updateById(quoteRequest);
            } catch (SendFailedException e) {
                throw new RuntimeException(e.getMessage());
            }
        }, quoteRequest.getId());
    }

    private String getSubject(QuoteRequestForm form) {
        return "Quote Request from " + (form.getFirstName() + " " + Strings.nullToEmpty(form.getLastName())).trim();
    }

    private String renderHtml(QuoteRequestForm form) {
        return new StringBuilder()
                .append("<html><body><table>")
                .append("<tr><td><strong>First Name</strong></td>")
                .append("<td>")
                .append(form.getFirstName())
                .append("</td>")
                .append("</tr>")
                .append("<tr><td><strong>Last Name</strong></td>")
                .append("<td>")
                .append(form.getLastName())
                .append("</td>")
                .append("</tr>")
                .append("<tr><td><strong>Company Name</strong></td>")
                .append("<td>")
                .append(form.getCompanyName())
                .append("</td>")
                .append("</tr>")
                .append("<tr><td><strong>Phone Number</strong></td>")
                .append("<td>")
                .append(form.getPhoneNumber())
                .append("</td>")
                .append("</tr>")
                .append("<tr><td><strong>Email</strong></td>")
                .append("<td>")
                .append(form.getEmail())
                .append("</td>")
                .append("</tr>")
                .append("<tr><td><strong>Prefer Call or Text</strong></td>")
                .append("<td>")
                .append(form.getPreferCallOrText() ? "Call" : "Text")
                .append("</td>")
                .append("</tr>")
                .append("<tr><td><strong>Company Address Line 1</strong></td>")
                .append("<td>")
                .append(form.getCompanyAddressLineOne())
                .append("</td>")
                .append("</tr>")
                .append("<tr><td><strong>Company Address City</strong></td>")
                .append("<td>")
                .append(form.getCompanyAddressCity())
                .append("</td>")
                .append("</tr>")
                .append("<tr><td><strong>Company Address State</strong></td>")
                .append("<td>")
                .append(form.getCompanyAddressState())
                .append("</td>")
                .append("</tr>")
                .append("<tr><td><strong>Company Address Zip Code</strong></td>")
                .append("<td>")
                .append(form.getCompanyAddressZipCode())
                .append("</td>")
                .append("</tr>")
                .append("<tr><td><strong>About Product</strong></td>")
                .append("<td>")
                .append(form.getAboutProduct())
                .append("</td>")
                .append("</tr>")
                .append("<tr><td><strong>Description</strong></td>")
                .append("<td>")
                .append(form.getDescription())
                .append("</td>")
                .append("</tr>")
                .append("<tr><td><strong>Estimate Quantity</strong></td>")
                .append("<td>")
                .append(form.getEstimateQuantity())
                .append("</td>")
                .append("</tr>")
                .append("</table></body></html>")
                .toString();
    }

}
