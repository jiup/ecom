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

package com.codeager.portal.controller;

import com.codeager.portal.annotation.Authenticate;
import com.codeager.portal.domain.Administrator;
import com.codeager.portal.domain.UserToken;
import com.codeager.portal.service.AdministratorService;
import com.codeager.portal.service.UserTokenService;
import com.codeager.portal.util.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * @author Jiupeng Zhang
 * @since 03/11/2018
 */
@Component("userResolver")
public class UserResolver implements HandlerMethodArgumentResolver {
    private UserTokenService userTokenService;
    private AdministratorService administratorService;

    @Autowired
    public void setUserTokenService(UserTokenService userTokenService) {
        this.userTokenService = userTokenService;
    }

    @Autowired
    public void setAdministratorService(AdministratorService administratorService) {
        this.administratorService = administratorService;
    }

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.getMethodAnnotation(Authenticate.class) != null &&
                (methodParameter.getParameterType().equals(Administrator.class));
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) {
        HttpServletRequest request = nativeWebRequest.getNativeRequest(HttpServletRequest.class);
        UserToken userToken;
        try {
            Cookie tokenCookie = WebUtils.getCookie(request, Constant.AUTH_COOKIE_NAME);
            userToken = userTokenService.getByToken(tokenCookie.getValue());
        } catch (NullPointerException e) {
            e.printStackTrace();
            return null;
        }

        if (userToken.getSessionRole().getCode() >= 8
                && methodParameter.getParameterType().equals(Administrator.class)) {
            return administratorService.getById(userToken.getUserId());
        }

        return null;
    }
}
