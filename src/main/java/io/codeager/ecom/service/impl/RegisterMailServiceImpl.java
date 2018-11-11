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

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import io.codeager.ecom.service.RegisterMailService;
import io.codeager.plugin.MailSender;
import io.codeager.portal.domain.Administrator;
import io.codeager.portal.service.AdministratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Nullable;
import javax.mail.SendFailedException;
import java.time.ZonedDateTime;
import java.util.Optional;

/**
 * @author Jiupeng Zhang
 * @since 12/14/2017
 */
@Service
public class RegisterMailServiceImpl extends BaseMailService implements RegisterMailService {
    private AdministratorService administratorService;

    @Autowired
    public void setAdministratorService(AdministratorService administratorService) {
        this.administratorService = administratorService;
    }

    @Autowired
    protected RegisterMailServiceImpl(Optional<MailSender> optionalFutureHolder) {
        super(optionalFutureHolder);
    }

    public void send(String toAddress) {
        ListenableFuture<Void> listenableFuture = MAIL_SERVICE_EXECUTOR.submit(() -> {
            try {
                futureHolder.sendText(
                        toAddress,
                        "欢迎注册 Codeager IO",
                        "这是一封通过SMTP发送的邮件，欢迎您阅读该源码，在这里享受最快捷的购物服务，请点击下方链接激活您的账户："
                );
            } catch (SendFailedException e) {
                throw new RuntimeException(e.getMessage());
            }
        }, null);

        Futures.addCallback(listenableFuture, new FutureCallback<Void>() {
            public void onSuccess(@Nullable Void aVoid) {
                LOG.info("Register mail sent successfully to <>");
            }

            public void onFailure(Throwable throwable) {
                LOG.warn("Register mail sent failure: <>");
            }
        }, MAIL_SERVICE_EXECUTOR);
    }

    @Override
    @Transactional
    public void sendNewAdminAccountInform(String toAddress, String displayName, String loginName, String initPassword) {
        ListenableFuture<Void> listenableFuture = MAIL_SERVICE_EXECUTOR.submit(() -> {
            try {
                futureHolder.sendHtml(
                        toAddress,
                        "Account message from Codeager backend",
                        renderInformHtml(displayName, loginName, initPassword)
                );
            } catch (SendFailedException e) {
                throw new RuntimeException(e.getMessage());
            }
        }, null);

        Futures.addCallback(listenableFuture, new FutureCallback<Void>() {
            public void onSuccess(@Nullable Void aVoid) {
                Administrator administrator = administratorService.getByLoginName(loginName);
                administrator.setUpdateTime(ZonedDateTime.now());
                administratorService.updateAdministratorById(administrator);
                LOG.info("Backend account inform mail sent successfully to '{}'", loginName);
            }

            public void onFailure(Throwable throwable) {
                LOG.info("Backend account inform mail for '{}' sent failure", loginName);
            }
        }, MAIL_SERVICE_EXECUTOR);
    }

    private String renderInformHtml(String displayName, String loginName, String initPassword) {
        return new StringBuilder()
                .append("<html><body>")
                .append("Dear ").append(displayName)
                .append(":<br/><br/>")
                .append("Your account has been created/reset by an administrator, please enter the following to login - <br/><br/>")
                .append("Username:&nbsp;").append(loginName).append("<br/>").append("Password:&nbsp;").append(initPassword)
                .append("<br/><br/>Don't forget to change your password after login : )<br/><br/>Have a nice day!<br/><br/> Codeager DevTeam")
                .append("</body></html>")
                .toString();
    }

}
