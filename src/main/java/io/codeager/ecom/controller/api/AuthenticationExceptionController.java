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

package com.codeager.ecom.controller.api;

import com.codeager.ecom.dto.view.$;
import com.codeager.ecom.dto.view.ResponseCode;
import com.codeager.ecom.dto.view.RestResponse;
import com.codeager.portal.AuthenticationException;
import com.codeager.portal.TokenHoldException;
import com.codeager.portal.domain.UserToken;
import com.codeager.portal.service.UserTokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author Jiupeng Zhang
 * @since 12/11/2017
 */
@RestControllerAdvice(basePackageClasses = AuthenticationExceptionController.class)
@Component("restfulAuthenticationExceptionHandler")
public class AuthenticationExceptionController {
    private static final Logger LOG = LoggerFactory.getLogger(AuthenticationExceptionController.class);

    private UserTokenService userTokenService;

    @ExceptionHandler(TokenHoldException.class)
    public ResponseEntity<RestResponse> tokenHoldExceptionHandler(TokenHoldException e) {
        try {
            UserToken userToken = e.getHolder();
            userTokenService.delete(userToken);
            return $.restful().code(ResponseCode.FORBIDDEN).asResponseEntity();
        } finally {
            LOG.warn("Abnormal token futureHolder: {}", e.getMessage());
        }
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity authenticationExceptionHandler() {
        return $.restful().code(ResponseCode.API_UNAUTHORIZED).asResponseEntity();
    }

    @Autowired
    public void setUserTokenService(UserTokenService userTokenService) {
        this.userTokenService = userTokenService;
    }
}
