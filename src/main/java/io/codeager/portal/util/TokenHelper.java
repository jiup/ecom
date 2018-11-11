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

package com.codeager.portal.util;

import com.google.common.base.Preconditions;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Base64Utils;

/**
 * @author Jiupeng Zhang
 * @since 11/26/2017
 */
public class TokenHelper {
    private static final HashFunction HASH_FUNCTION = Hashing.hmacSha1(Constant.TOKEN_SIGNATURE);

    public static String createToken(int length) {
        return createToken(length, true);
    }

    public static String createToken(int baseLength, boolean withHmac) {
        Preconditions.checkArgument(baseLength > 0, "Length of token must be positive");

        String base = RandomStringUtils.randomAlphanumeric(baseLength);
        return !withHmac ? base : base.concat(StringUtils.stripEnd(Base64Utils.encodeToUrlSafeString(hash(base.getBytes())), "="));
    }

    public static boolean isValid(String token, int baseLength) {
        Preconditions.checkArgument(baseLength > 0, "Length of token must be positive");
        if (token.length() < baseLength) return false;
        if (token.length() == baseLength) return true;

        String base = token.substring(0, baseLength);
        String checkCode = StringUtils.stripEnd(Base64Utils.encodeToUrlSafeString(hash(base.getBytes())), "=");
        return token.length() == baseLength + checkCode.length() && token.endsWith(checkCode);
    }

    private static byte[] hash(byte[] base) {
        return HASH_FUNCTION.newHasher().putBytes(base).hash().asBytes();
    }
}