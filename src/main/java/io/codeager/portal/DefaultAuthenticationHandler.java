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

import javax.servlet.http.HttpServletRequest;

/**
 * @author Jiupeng Zhang
 * @since 11/25/2017
 */
public class DefaultAuthenticationHandler implements AuthenticationHandler {
    @Override
    public boolean handleIntrude(HttpServletRequest request, Role currentRole, Role[] requiredRoles) {
        if (currentRole == Role.GUEST) {
            UnregisteredException unregisteredException = new UnregisteredException();
            unregisteredException.setForward(request.getRequestURL().toString());
            throw unregisteredException;
        }
        throw new AuthenticationException("permission denied");
    }
}