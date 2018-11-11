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

package com.codeager.portal;

import com.codeager.portal.domain.UserToken;

/**
 * @author Jiupeng Zhang
 * @since 11/23/2017
 */
public class TokenExpiredException extends AuthenticationException {
    private final UserToken holder;
    private String forward;

    public TokenExpiredException(UserToken userToken) {
        this.holder = userToken;
    }

    public String getForward() {
        return forward;
    }

    public void setForward(String forward) {
        this.forward = forward;
    }

    public UserToken getHolder() {
        return holder;
    }
}
