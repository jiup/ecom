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

package com.codeager.portal.service.impl;

import com.codeager.portal.dao.mapper.UserTokenMapper;
import com.codeager.portal.domain.UserToken;
import com.codeager.portal.service.UserTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Jiupeng Zhang
 * @since 11/26/2017
 */
@Service
public class UserTokenServiceImpl implements UserTokenService {

    private UserTokenMapper userTokenMapper;

    @Autowired
    public void setUserTokenMapper(UserTokenMapper userTokenMapper) {
        this.userTokenMapper = userTokenMapper;
    }

    @Override
    public void add(UserToken userToken) {
        userTokenMapper.insert(userToken);
    }

    @Override
    public void delete(UserToken userToken) {
        userTokenMapper.deleteById(userToken.getId());
    }

    @Override
    public void deleteByUserHash(String userHash) {
        userTokenMapper.deleteByUserHash(userHash);
    }

    @Override
    public int deleteExpiredToken() {
        return userTokenMapper.deleteExpiredRows();
    }

    @Override
    public void update(UserToken userToken) {
        userTokenMapper.update(userToken);
    }

    @Override
    public UserToken getByToken(String token) {
        return userTokenMapper.selectByToken(token);
    }

    @Override
    public UserToken getByUserHash(String userHash) {
        return userTokenMapper.selectByUserHash(userHash);
    }

}
