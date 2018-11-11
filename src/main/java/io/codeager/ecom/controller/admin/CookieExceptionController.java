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

import com.codeager.ecom.controller.CookiePassingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static com.codeager.ecom.util.Routing.*;

/**
 * @author Jiupeng Zhang
 * @since 12/17/2017
 */
@ControllerAdvice(basePackageClasses = CookieExceptionController.class)
@Component("cookieExceptionController")
public class CookieExceptionController {
    private static final Logger LOG = LoggerFactory.getLogger(CookieExceptionController.class);

    @ExceptionHandler(CookiePassingException.class)
    public String cookieNotSupportExceptionHandler(CookiePassingException e) {
        try {
            return redirect(ADMIN_BASE, ADMIN_LOGIN);
        } finally {
            LOG.warn("Cookie not support: {}", e.getMessage());
        }
    }
}
