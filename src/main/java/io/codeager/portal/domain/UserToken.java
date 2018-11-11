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

package io.codeager.portal.domain;

import io.codeager.portal.Role;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * User Identity Holder
 *
 * @author Jiupeng Zhang
 * @since 11/25/2017
 */
public class UserToken implements Serializable {
    private long id;
    private String userHash;
    private String token;
    private long userId;
    private short failureCount;
    private Role sessionRole;
    private String sessionIp;
    private String sessionUserAgent;
    private ZonedDateTime expireTime;
    private ZonedDateTime createTime;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUserHash() {
        return userHash;
    }

    public void setUserHash(String userHash) {
        this.userHash = userHash;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public short getFailureCount() {
        return failureCount;
    }

    public void setFailureCount(short failureCount) {
        this.failureCount = failureCount;
    }

    public Role getSessionRole() {
        return Objects.requireNonNull(sessionRole);
    }

    public void setSessionRole(Role sessionRole) {
        this.sessionRole = sessionRole;
    }

    public String getSessionIp() {
        return sessionIp;
    }

    public void setSessionIp(String sessionIp) {
        this.sessionIp = sessionIp;
    }

    public String getSessionUserAgent() {
        return sessionUserAgent;
    }

    public void setSessionUserAgent(String sessionUserAgent) {
        this.sessionUserAgent = sessionUserAgent;
    }

    public ZonedDateTime getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(ZonedDateTime expireTime) {
        this.expireTime = expireTime;
    }

    public ZonedDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(ZonedDateTime createTime) {
        this.createTime = createTime;
    }
}
