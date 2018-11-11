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

package io.codeager.portal.dao.mapper;

import io.codeager.portal.Role;
import io.codeager.portal.dao.type.RoleTypeHandler;
import io.codeager.portal.domain.UserToken;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

/**
 * @author Jiupeng Zhang
 * @since 11/26/2017
 */
@Mapper
@Repository
public interface UserTokenMapper {
    @Select({
            "SELECT *",
            "FROM t_user_access",
            "WHERE USER_HASH = #{userHash}"
    })
    @Results(id = "userTokenResult", value = {
            @Result(property = "id", column = "USER_ACCESS_ID"),
            @Result(property = "userHash", column = "USER_HASH"),
            @Result(property = "token", column = "TOKEN"),
            @Result(property = "userId", column = "USER_ID"),
            @Result(property = "failureCount", column = "FAILURE_COUNT"),
            @Result(property = "sessionRole", column = "SESSION_ROLE",
                    typeHandler = RoleTypeHandler.class),
            @Result(property = "sessionIp", column = "SESSION_IP"),
            @Result(property = "sessionUserAgent", column = "SESSION_UA"),
            @Result(property = "expireTime", column = "EXPIRE_TIME"),
            @Result(property = "createTime", column = "CREATE_TIME")
    })
    UserToken selectByUserHash(String userHash);

    @Select({
            "SELECT *",
            "FROM t_user_access",
            "WHERE TOKEN = #{token}"
    })
    @ResultMap("userTokenResult")
    UserToken selectByToken(String token);

    @Insert({
            "INSERT INTO t_user_access (USER_HASH, TOKEN, USER_ID, FAILURE_COUNT, SESSION_ROLE, SESSION_IP, SESSION_UA, EXPIRE_TIME, CREATE_TIME)",
            "VALUES (#{userHash}, #{token}, #{userId}, #{failureCount}, " +
                    "#{sessionRole,typeHandler=io.codeager.portal.dao.type.RoleTypeHandler}," +
                    "#{sessionIp}, #{sessionUserAgent}, #{expireTime}, #{createTime})"
    })
    void insert(UserToken userToken);

    @Delete({
            "DELETE FROM t_user_access",
            "WHERE now() > EXPIRE_TIME"
    })
    int deleteExpiredRows();

    @Delete({
            "DELETE FROM t_user_access",
            "WHERE USER_ACCESS_ID = #{id}"
    })
    void deleteById(long id);

    @Delete({
            "DELETE FROM t_user_access",
            "WHERE USER_HASH = #{userHash}"
    })
    void deleteByUserHash(String userHash);

    @Update({
            "UPDATE t_user_access",
            "SET",
            "USER_HASH = #{userHash},",
            "TOKEN = #{token},",
            "USER_ID = #{userId},",
            "FAILURE_COUNT = #{failureCount},",
            "SESSION_ROLE = #{sessionRole,typeHandler=io.codeager.portal.dao.type.RoleTypeHandler},",
            "SESSION_IP = #{sessionIp},",
            "SESSION_UA = #{sessionUserAgent},",
            "EXPIRE_TIME = #{expireTime}",
            "WHERE USER_ACCESS_ID = #{id}"
    })
    void update(UserToken userToken);

    @Update({
            "UPDATE t_user_access",
            "SET",
            "USER_HASH = #{userHash},",
            "USER_ID = #{userId},",
            "FAILURE_COUNT = #{failureCount},",
            "SESSION_ROLE = #{sessionRole,typeHandler=io.codeager.portal.dao.type.RoleTypeHandler},",
            "SESSION_IP = #{sessionIp},",
            "SESSION_UA = #{sessionUserAgent},",
            "EXPIRE_TIME = #{expireTime}",
            "WHERE TOKEN = #{token}"
    })
    void updateByToken(UserToken userToken);

    @Update({
            "UPDATE t_user_access",
            "SET",
            "SESSION_ROLE = #{role,typeHandler=io.codeager.portal.dao.type.RoleTypeHandler},",
            "WHERE USER_ACCESS_ID = #{id}"
    })
    void updateRoleById(@Param("id") long id, @Param("role") Role role);
}
