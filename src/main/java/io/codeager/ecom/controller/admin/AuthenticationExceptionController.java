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

import com.codeager.portal.*;
import com.codeager.portal.domain.UserToken;
import com.codeager.portal.service.UserTokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Objects;

import static com.codeager.ecom.util.Routing.*;

/**
 * @author Jiupeng Zhang
 * @since 12/11/2017
 */
@ControllerAdvice(basePackageClasses = AuthenticationExceptionController.class)
@Component("authenticationExceptionHandler")
public class AuthenticationExceptionController {
    private static final Logger LOG = LoggerFactory.getLogger(AuthenticationExceptionController.class);

    private UserTokenService userTokenService;

    @ExceptionHandler(TokenExpiredException.class)
    public String tokenExpiredExceptionHandler(TokenExpiredException e) {
        try {
            userTokenService.delete(Objects.requireNonNull(e.getHolder()));
            return redirect(ADMIN_BASE, ADMIN_LOGIN, e.getForward() == null ? "" : "?forward=" + e.getForward());
        } finally {
            LOG.warn("Access token expired, userId=", e.getHolder().getUserId());
        }
    }

    @ExceptionHandler(TokenHoldException.class)
    public ResponseEntity<?> tokenHoldExceptionHandler(TokenHoldException e) {
        try {
            userTokenService.delete(e.getHolder());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } finally {
            LOG.warn("Abnormal token futureHolder: {}", e.getMessage());
        }
    }

    @ExceptionHandler(UnregisteredException.class)
    public String unregisteredExceptionHandler(UnregisteredException e) {
        return redirect(ADMIN_BASE, ADMIN_LOGIN, e.getForward() == null ? "" : "?forward=" + e.getForward());
    }

    @ExceptionHandler(InvalidTokenException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public String invalidTokenExceptionHandler(InvalidTokenException e) {
        try {
            return "401 - Unauthorized";
        } finally {
            LOG.warn("Invalid token: {}", e.getMessage());
        }
    }

    @ExceptionHandler(LoginException.class)
    public String loginExceptionHandler(LoginException e) {
        try {
            UserToken userToken = e.getHolder();
            if (userToken != null) {
                short failureCount = userToken.getFailureCount();
                if (failureCount > 0) {
                    // todo: captcha required
                }
                if (failureCount >= 3) {
                    // todo: frozen account, needs reactivated by email
                    return redirect(ADMIN_BASE, ADMIN_LOGIN, "?resp=too many attempts, please retry after 2 hours");
                }
                userToken.setFailureCount(++failureCount);
                userTokenService.update(userToken);
            }
            return redirect(ADMIN_BASE, ADMIN_LOGIN, "?resp=",
                    userToken != null ? "username or password not correct" : "token expired, please try again");
        } finally {
            LOG.warn("Login failure: {}", e.getMessage());
        }
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public String authenticationExceptionHandler(AuthenticationException e) {
        try {
            return "401 - Unauthorized";
        } finally {
            LOG.warn("Authentication refused: {}", e.getMessage());
        }
    }

    @Autowired
    public void setUserTokenService(UserTokenService userTokenService) {
        this.userTokenService = userTokenService;
    }
}
