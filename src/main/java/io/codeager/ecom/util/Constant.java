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

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Non-custom Constants
 *
 * @author Jiupeng Zhang
 * @since 11/27/2017
 */
@Component("ecomConstant")
public class Constant {
    public static final double MAIL_REQUEST_LIMIT_RATE = 5.0; // maximum 5.0
    public static final double CAPTCHA_REQUEST_LIMIT_RATE = 50.0; // maximum 50.0
    public static final double FORMAT_CHECK_REQUEST_LIMIT_RATE = 50.0; // maximum 50.0

    public static String[] ADMIN_EMAIL_ADDRESSES = {};
    public static String UNSUBSCRIBE_URL_BASE;

    @Value("${ecom.email.notify.address}")
    private void setAdminEmailAddresses(String adminEmailAddresses) {
        ADMIN_EMAIL_ADDRESSES = adminEmailAddresses.split(",");
    }

    @Value("${ecom.email.unsubscribe.url.base}")
    private void setUnsubscribeUrlBase(String unsubscribeUrlBase) {
        UNSUBSCRIBE_URL_BASE = unsubscribeUrlBase;
    }
}
