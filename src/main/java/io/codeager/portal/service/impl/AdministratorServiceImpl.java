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

import com.codeager.portal.LoginException;
import com.codeager.portal.Role;
import com.codeager.portal.dao.mapper.AdministratorMapper;
import com.codeager.portal.domain.Administrator;
import com.codeager.portal.domain.UserToken;
import com.codeager.portal.service.AdministratorService;
import com.codeager.portal.service.UserTokenService;
import com.codeager.portal.util.Constant;
import com.codeager.portal.util.TokenHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.time.ZonedDateTime;
import java.util.List;

/**
 * @author Jiupeng Zhang
 * @since 12/11/2017
 */
@Service
public class AdministratorServiceImpl implements AdministratorService {
    private AdministratorMapper administratorMapper;
    private UserTokenService userTokenService;

    @Autowired
    public void setAdministratorMapper(AdministratorMapper administratorMapper) {
        this.administratorMapper = administratorMapper;
    }

    @Autowired
    public void setUserTokenService(UserTokenService userTokenService) {
        this.userTokenService = userTokenService;
    }

    @Override
    @Transactional
    public Administrator login(String token, String loginName, String password) {
        UserToken userToken = userTokenService.getByToken(token);
        if (userToken == null) {
            throw new LoginException(null, "token not found in database, re-apply identity");
        }

        Administrator administrator = administratorMapper.selectByLoginName(loginName);
        if (administrator == null) {
            throw new LoginException(userToken, "user \"" + loginName + "\" not exists");
        }
        if (!administrator.getPassword()
                .equals(DigestUtils.md5DigestAsHex((password + administrator.getPasswordSalt()).getBytes()))) {
            throw new LoginException(userToken, "wrong password for user \"" + loginName + "\"");
        }

        userToken.setUserId(administrator.getId());
        userToken.setUserHash(administrator.getLoginName());
        userToken.setSessionRole(administrator.getRole());
        userToken.setFailureCount((short) 0);
        userToken.setExpireTime(ZonedDateTime.now().plusSeconds(Constant.USER_ACCESS_VALID_IN_SECONDS)); // postpone
        UserToken ghost = userTokenService.getByUserHash(userToken.getUserHash());
        if (ghost != null) {
            userTokenService.deleteByUserHash(ghost.getUserHash()); // kick out
        }
        userTokenService.update(userToken);

        administrator.setLastLoginIp(userToken.getSessionIp());
        administrator.setLastLoginUserAgent(userToken.getSessionUserAgent());
        administrator.setLoginCount(administrator.getLoginCount() + 1);
        administrator.setLastLoginTime(ZonedDateTime.now());
        administratorMapper.updateById(administrator);
        return administrator;
    }

    @Override
    @Transactional
    public void logout(String token) {
        UserToken userToken = userTokenService.getByToken(token);
        if (userToken != null) {
            userToken.setUserHash(null);
            userToken.setUserId(0);
            userToken.setFailureCount((short) 0);
            userToken.setSessionRole(Role.GUEST);
            userToken.setExpireTime(ZonedDateTime.now().plusSeconds(Constant.USER_ACCESS_VALID_IN_SECONDS)); // postpone
            userTokenService.update(userToken);
        }
    }

    @Override
    public Administrator getById(long id) {
        return administratorMapper.selectById(id);
    }

    @Override
    public Administrator getByLoginName(String loginName) {
        return administratorMapper.selectByLoginName(loginName);
    }

    @Override
    public List<Administrator> getAllLoggedAdministrators() {
        return administratorMapper.selectAllLoggedRecords();
    }

    @Override
    public List<Administrator> getAll() {
        return administratorMapper.selectAll();
    }

    @Override
    public List<Administrator> getAllEditors() {
        return administratorMapper.selectAllEditors();
    }

    @Override
    public List<Administrator> getAllAdministrators() {
        return administratorMapper.selectAllAdministrators();
    }

    @Override
    public void updateAdministratorById(Administrator administrator) {
        administratorMapper.updateById(administrator);
    }

    @Override
    public boolean isRightPassword(Administrator administrator, String password) {
        return administrator.getPassword()
                .equals(DigestUtils.md5DigestAsHex((password + administrator.getPasswordSalt()).getBytes()));
    }

    @Override
    public void updatePasswordById(Administrator administrator, String password) {
        administrator.setPasswordSalt(TokenHelper.createToken(64, false));
        administrator.setPassword(DigestUtils.md5DigestAsHex((password + administrator.getPasswordSalt()).getBytes()));
        administrator.setUpdateTime(ZonedDateTime.now());
        administratorMapper.updateById(administrator);
    }

    @Override
    public void deleteById(long id) {
        administratorMapper.deleteById(id);
    }

    @Override
    public boolean containsLoginName(String loginName) {
        return administratorMapper.selectByLoginName(loginName) != null;
    }

    @Override
    public void addNewAdministrator(String loginName, String displayName, String emailAddress, String password) {
        Administrator administrator = fillNewAdministrator(loginName, displayName, emailAddress, password);
        administrator.setRole(Role.ADMIN);
        administrator.setCreateTime(ZonedDateTime.now());
        administrator.setUpdateTime(ZonedDateTime.now());
        administratorMapper.insert(administrator);
    }

    @Override
    public void addNewEditor(String loginName, String displayName, String emailAddress, String password) {
        Administrator administrator = fillNewAdministrator(loginName, displayName, emailAddress, password);
        administrator.setRole(Role.EDITOR);
        administrator.setCreateTime(ZonedDateTime.now());
        administrator.setUpdateTime(ZonedDateTime.now());
        administratorMapper.insert(administrator);
    }

    @Override
    public void kickOffById(long id) {
        Administrator administrator = administratorMapper.selectById(id);
        if (administrator == null) {
            return;
        }

        UserToken userToken = userTokenService.getByUserHash(administrator.getLoginName());
        if (userToken != null && userToken.getSessionRole() == administrator.getRole()) {
            userTokenService.deleteByUserHash(administrator.getLoginName());
        }
    }

    private Administrator fillNewAdministrator(String loginName, String displayName, String emailAddress, String password) {
        Administrator administrator = new Administrator();
        administrator.setLoginName(loginName);
        administrator.setDisplayName(displayName);
        administrator.setEmailAddress(emailAddress);
        administrator.setPasswordSalt(TokenHelper.createToken(64, false));
        administrator.setPassword(DigestUtils.md5DigestAsHex((password + administrator.getPasswordSalt()).getBytes()));
        return administrator;
    }
}
