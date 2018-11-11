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

package com.codeager.ecom.interceptor;

import com.codeager.portal.Role;
import com.codeager.portal.domain.Administrator;
import com.codeager.portal.domain.UserToken;
import com.codeager.portal.service.AdministratorService;
import com.codeager.portal.service.UserTokenService;
import com.codeager.portal.util.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author Jiupeng Zhang
 * @since 03/11/2018
 */
@Component("userInterceptor")
public class AdminSessionInterceptor extends HandlerInterceptorAdapter {
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
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
        HttpSession session = request.getSession();
        if (session.getAttribute("user") == null) {
            UserToken userToken;
            try {
                Cookie tokenCookie = WebUtils.getCookie(request, Constant.AUTH_COOKIE_NAME);
                userToken = userTokenService.getByToken(tokenCookie.getValue());
            } catch (NullPointerException e) {
                e.printStackTrace();
                return;
            }

            if (userToken.getSessionRole().getCode() >= 8 && userToken.getUserId() != 0) {
                Administrator administrator = administratorService.getById(userToken.getUserId());
                session.setAttribute("user", administrator);
                if (administrator.getRole().hasPermission(Role.ADMIN)) {
                    session.setAttribute("onlineAdminUsers", administratorService.getAllLoggedAdministrators());
                }
            }
        }
    }
}
