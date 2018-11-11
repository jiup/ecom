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

package com.codeager.ecom.controller;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.codeager.ecom.dto.view.$;
import com.codeager.ecom.dto.view.ErrorView;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.util.NestedServletException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Jiupeng Zhang
 * @since 12/22/2017
 */
@Controller("errorController")
public class ErrorController {
    private static final Logger LOG = LoggerFactory.getLogger(ErrorController.class);
    private static final String ERROR_MAPPING = "/error";

    private ViewResolver viewResolver;

    @RequestMapping(ERROR_MAPPING)
    @ResponseBody
    public ResponseEntity<?> errorHandler(HttpServletRequest request, HttpServletResponse response, Model model) {
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        int status = httpStatus.value();

        String statusStr = String.valueOf(ObjectUtils.defaultIfNull(request.getAttribute("javax.servlet.error.status_code"), "unknown"));
        if (NumberUtils.isParsable(statusStr)) {
            status = Integer.parseInt(statusStr);
            httpStatus = HttpStatus.valueOf(status);
        }

        String message = String.valueOf(ObjectUtils.defaultIfNull(request.getAttribute("javax.servlet.error.message"), ""));
        String requestUri = String.valueOf(ObjectUtils.defaultIfNull(request.getAttribute("javax.servlet.forward.request_uri"), ERROR_MAPPING));
        String queryString = String.valueOf(ObjectUtils.defaultIfNull(request.getAttribute("javax.servlet.forward.query_string"), ""));
        String path = queryString.length() == 0 ? requestUri : requestUri + "?" + queryString;
        Throwable throwable = (Throwable) request.getAttribute("javax.servlet.error.exception");

        ErrorView error = new ErrorView();
        error.setType(httpStatus.series().name().toLowerCase());
        error.setPath(path);
        if (throwable instanceof NestedServletException) {
            throwable = throwable.getCause();
        }
        if (throwable != null) {
            error.setName(underlineJoined(throwable.getClass().getSimpleName()).toLowerCase());
            error.setDescribe(throwable.getLocalizedMessage());
            if (throwable.getCause() != null) {
                error.setCauseBy(underlineJoined(throwable.getCause().getClass().getSimpleName()).toLowerCase());
            }
        }

        try {
            if (requestUri.startsWith(request.getContextPath() + "/api")) {
                // API Json Error
                return $.restful()
                        .status(status)
                        .code(10000 + 10 * status)
                        .message((Strings.isNullOrEmpty(message) ? httpStatus.getReasonPhrase() : message))
                        .data().putErrors(error).ready()
                        .asResponseEntity();
            } else {
                // General Error View
                try {
                    model.addAttribute("status", status);
                    model.addAttribute("message", (Strings.isNullOrEmpty(message) ? httpStatus.getReasonPhrase() : message));
                    model.addAttribute("error", error);
                    response.setStatus(status);
                    View resolvedView = viewResolver.resolveViewName("error", RequestContextUtils.getLocale(request));
                    resolvedView.render(model.asMap(), request, response);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        } finally {
            if (httpStatus.is5xxServerError()) {
                LOG.error("Error shown: {}", error);
            } else if (httpStatus.is3xxRedirection() || httpStatus.is4xxClientError()) {
                LOG.info("Error shown: {}", error);
            } else if (httpStatus.is2xxSuccessful() || httpStatus.is1xxInformational()) {
                LOG.debug("Error shown: {}", error);
            }
        }
    }

    private String underlineJoined(String str) {
        Preconditions.checkNotNull(str);
        return StringUtils.trimLeadingCharacter(str.replaceAll("[A-Z]", "_$0"), '_');
    }

    @Autowired
    public void setViewResolver(@Qualifier("thymeleafViewResolver") ViewResolver viewResolver) {
        this.viewResolver = viewResolver;
    }
}
