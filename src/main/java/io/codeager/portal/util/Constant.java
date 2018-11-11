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

package io.codeager.portal.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Non-custom Constants
 *
 * @author Jiupeng Zhang
 * @since 11/18/2017
 */
@Component("portalConstant")
public class Constant {
    private static final Logger LOG = LoggerFactory.getLogger(Constant.class);

    public static final String AUTH_COOKIE_NAME = "_es";
    public static final int USER_ACCESS_VALID_IN_SECONDS = 7200;
    public static final int USER_TOKEN_BASE_LENGTH = 32;

    public static boolean IS_PROD;
    public static String DOMAIN_SHARE;

    static final byte[] TOKEN_SIGNATURE = "DZ0lUfdJH7AT3qWN".getBytes();

    @Value("${ecom.dev}")
    private void setDevMode(boolean dev) {
        IS_PROD = !dev;
        LOG.info("Mode detection: {}", dev ? "DEV" : "PROD");
    }

    @Value("${ecom.prod.domain.share}")
    private void setDomainShare(String domainShare) {
        DOMAIN_SHARE = domainShare;
    }
}