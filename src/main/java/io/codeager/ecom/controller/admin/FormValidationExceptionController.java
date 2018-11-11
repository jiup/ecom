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

package com.codeager.ecom.controller.admin;

import com.codeager.ecom.controller.FormValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import static com.codeager.ecom.util.Routing.ADMIN_BASE;
import static com.codeager.ecom.util.Routing.redirect;

/**
 * @author Jiupeng Zhang
 * @since 12/18/2017
 */
@ControllerAdvice(basePackageClasses = FormValidationExceptionController.class)
@Component("formValidationExceptionController")
public class FormValidationExceptionController {
    private static Logger LOG = LoggerFactory.getLogger(FormValidationExceptionController.class);

    private MessageSourceAccessor messageSourceAccessor;

    @Autowired
    public void setMessageSourceAccessor(MessageSourceAccessor messageSourceAccessor) {
        this.messageSourceAccessor = messageSourceAccessor;
    }

    @ExceptionHandler(FormValidationException.class)
    public String formValidationExceptionHandler(FormValidationException e) {
        try {
            if (e.getBindingResult() != null) {
                StringBuilder stringBuilder = new StringBuilder();
                e.getBindingResult()
                        .getFieldErrors().forEach(fieldError -> stringBuilder
                        .append(fieldError.getField())
                        .append(" ").append(fieldError.getDefaultMessage()).append(", "));

                String message = stringBuilder.toString().trim();
                message = URLEncoder.encode(message.endsWith(",") ? message.substring(0, message.length() - 1) : message, "UTF-8");
                return redirect(ADMIN_BASE, e.getRedirectPath(), "?resp=", message);
            } else {
                return redirect(ADMIN_BASE, e.getRedirectPath(), "?resp=", e.getBindingMessage());
            }
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
            return redirect(ADMIN_BASE, e.getRedirectPath());
        } finally {
            if (e.getBindingMessage() != null) {
                LOG.warn("Form validation failure: {} ({})", e.getRedirectPath(), e.getBindingMessage());
            } else {
                LOG.warn("Form validation failure: {}", e.getRedirectPath());
            }
        }
    }
}
