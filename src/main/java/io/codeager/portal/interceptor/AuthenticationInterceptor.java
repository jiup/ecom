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

package io.codeager.portal.interceptor;

import io.codeager.portal.*;
import io.codeager.portal.annotation.Authenticate;
import io.codeager.portal.domain.UserToken;
import io.codeager.portal.service.UserTokenService;
import io.codeager.portal.util.Constant;
import io.codeager.portal.util.TokenHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.ZonedDateTime;
import java.util.Map;

/**
 * Authority Authentication Module
 *
 * @author Jiupeng Zhang
 * @since 11/25/2017
 */
@Component("authenticationInterceptor")
public class AuthenticationInterceptor extends HandlerInterceptorAdapter {
    private WebApplicationContext webContext;
    private UserTokenService userTokenService;

    @Autowired
    public void setWebContext(WebApplicationContext webContext) {
        this.webContext = webContext;
    }

    @Autowired
    public void setUserTokenService(UserTokenService userTokenService) {
        this.userTokenService = userTokenService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (handler instanceof HandlerMethod) {
            UserToken userToken = getUserToken(request);
            if (userToken == null) {
                userTokenService.add(userToken = createNewToken(request));
                saveTokenToCookie(response, userToken);
            }

            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Authenticate authenticate = handlerMethod.getMethodAnnotation(Authenticate.class);
            for (Role role : authenticate != null ? authenticate.value() : new Role[]{Role.GUEST}) {
                if ((authenticate != null && authenticate.explicit()) ?
                        userToken.getSessionRole() == role : userToken.getSessionRole().hasPermission(role)) {
                    if (ZonedDateTime.now().compareTo(userToken.getExpireTime()) > 0) {
                        if (role.equals(Role.GUEST)) {
                            // Auto-renew cookie for guests
                            userTokenService.delete(userToken);
                            userTokenService.add(userToken = createNewToken(request));
                            saveTokenToCookie(response, userToken);
                        } else {
                            TokenExpiredException tokenExpiredException = new TokenExpiredException(userToken);
                            tokenExpiredException.setForward(request.getRequestURL().toString());
                            throw tokenExpiredException;
                        }
                    }
                    return true;
                }
            }

            if (authenticate != null) {
                // Authenticate Refused
                AuthenticationHandler authenticationHandler = webContext.getBean(authenticate.handler());
                return authenticationHandler.handleIntrude(request, userToken.getSessionRole(), authenticate.value());
            }
        }
        return true;
    }

    private UserToken getUserToken(HttpServletRequest request) {
        Cookie tokenCookie = WebUtils.getCookie(request, Constant.AUTH_COOKIE_NAME);
        if (tokenCookie == null) {
            return null;
        }

        String token = tokenCookie.getValue();
        if (!TokenHelper.isValid(token, Constant.USER_TOKEN_BASE_LENGTH)) {
            throw new InvalidTokenException("self-checking failed, ip=" + getRemoteAddr(request));
        }

        UserToken userToken = userTokenService.getByToken(token);
        if (userToken == null) {
            return null;
        }

        if (!userToken.getSessionIp().equals(getRemoteAddr(request))) {
            throw new TokenHoldException(userToken, "session identifier changed: ip ["
                    + userToken.getSessionIp() + "] -> [" + getRemoteAddr(request) + "]");
        }

        if (!userToken.getSessionUserAgent().equals(request.getHeader("User-Agent"))) {
            throw new TokenHoldException(userToken, "session identifier changed: ua ["
                    + userToken.getSessionUserAgent() + "] -> [" + request.getHeader("User-Agent") + "]");
        }

        Object userHashObject = ((Map) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE)).get("userHash");
        if (userHashObject != null && !userHashObject.toString().equals(userToken.getUserHash())) {
            throw new TokenHoldException(userToken, "user-hash mismatched");
        }
        return userToken;
    }

    private UserToken createNewToken(HttpServletRequest request) {
        UserToken userToken = new UserToken();
        userToken.setToken(TokenHelper.createToken(Constant.USER_TOKEN_BASE_LENGTH));
        userToken.setSessionRole(Role.GUEST);
        userToken.setSessionIp(getRemoteAddr(request));
        userToken.setSessionUserAgent(request.getHeader("User-Agent"));
        userToken.setCreateTime(ZonedDateTime.now());
        userToken.setExpireTime(userToken.getCreateTime().plusSeconds(Constant.USER_ACCESS_VALID_IN_SECONDS));
        return userToken;
    }

    private void saveTokenToCookie(HttpServletResponse response, UserToken userToken) {
        Cookie cookie = new Cookie(Constant.AUTH_COOKIE_NAME, userToken.getToken());
        cookie.setMaxAge(Constant.USER_ACCESS_VALID_IN_SECONDS);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        if (Constant.IS_PROD) {
            cookie.setDomain(Constant.DOMAIN_SHARE);
        }
        response.addCookie(cookie);
    }

    private String getRemoteAddr(HttpServletRequest request) {
        String remoteAddr;
        if ((remoteAddr = request.getHeader("X-Real-IP")) != null)
            return remoteAddr;
        if ((remoteAddr = request.getHeader("X-Forwarded-For")) != null)
            return remoteAddr;

        return request.getRemoteAddr();
    }
}
