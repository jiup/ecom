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

package io.codeager.portal.service;

import io.codeager.portal.domain.UserToken;

/**
 * @author Jiupeng Zhang
 * @since 11/26/2017
 */
public interface UserTokenService {

    void add(UserToken userToken);

    void delete(UserToken userToken);

    void deleteByUserHash(String userHash);

    int deleteExpiredToken();

    void update(UserToken userToken);

    UserToken getByToken(String token);

    UserToken getByUserHash(String userHash);

}
