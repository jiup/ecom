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

package com.codeager.ecom.util;

/**
 * @author Jiupeng Zhang
 * @since 12/18/2017
 */
public class Routing {
    private static final String _FORWARD = "forward:";
    private static final String _REDIRECT = "redirect:";

    public static final String FAVICON = "favicon.ico";
    public static final String ADMIN_BASE = "/admin";
    public static final String API_VERSION = "v1";
    public static final String API_BASE = "/api/" + API_VERSION;
    public static final String ADMIN_LOGIN = "/login";
    public static final String ADMIN_LOGOUT = "/logout";
    public static final String MEMBER_SIGN_UP = "/members";
    public static final String MEMBER_LOGIN = "/members/{username}/session";
    public static final String MEMBER_LOGOUT = "/members/{username}/session";
    public static final String SIGN_UP_CHECK_USERNAME = "/signup_check/username";
    public static final String SIGN_UP_CHECK_PASSWORD = "/signup_check/password";
    public static final String SIGN_UP_CHECK_EMAIL = "/signup_check/email";
    public static final String PRODUCT = "/product/{id}";
    public static final String PRODUCTS = "/products";
    public static final String CONTACT = "/contact";
    public static final String QUOTE = "/quote";
    public static final String HOME = "/home";
    public static final String INDEX = "/";


    public static String forward(String... routes) {
        return concat(_FORWARD, routes);
    }

    public static String redirect(String... routes) {
        return concat(_REDIRECT, routes);
    }

    public static String concat(String base, String... elements) {
        StringBuilder builder = new StringBuilder();
        builder.append(base);
        for (String e : elements) {
            builder.append(e);
        }
        return builder.toString();
    }
}
