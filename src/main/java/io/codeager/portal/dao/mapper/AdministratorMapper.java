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

package com.codeager.portal.dao.mapper;

import com.codeager.portal.dao.type.RoleTypeHandler;
import com.codeager.portal.domain.Administrator;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Jiupeng Zhang
 * @since 12/11/2017
 */
@Mapper
@Repository
public interface AdministratorMapper {
    @Select({
            "SELECT *",
            "FROM t_administrator",
            "WHERE LOGIN_NAME = #{loginName}"
    })
    @Results(id = "administratorResult", value = {
            @Result(property = "id", column = "ADMINISTRATOR_ID"),
            @Result(property = "loginName", column = "LOGIN_NAME"),
            @Result(property = "password", column = "PASSWORD"),
            @Result(property = "passwordSalt", column = "PW_SALT"),
            @Result(property = "role", column = "ROLE",
                    typeHandler = RoleTypeHandler.class),
            @Result(property = "displayName", column = "DISPLAY_NAME"),
            @Result(property = "emailAddress", column = "EMAIL_ADDRESS"),
            @Result(property = "loginCount", column = "LOGIN_COUNT"),
            @Result(property = "lastLoginIp", column = "LAST_LOGIN_IP"),
            @Result(property = "lastLoginUserAgent", column = "LAST_LOGIN_UA"),
            @Result(property = "lastLoginTime", column = "LAST_LOGIN_TIME"),
            @Result(property = "createTime", column = "CREATE_TIME"),
            @Result(property = "updateTime", column = "UPDATE_TIME")
    })
    Administrator selectByLoginName(String loginName);

    @Insert({
            "INSERT INTO t_administrator (LOGIN_NAME, PASSWORD, PW_SALT, ROLE, DISPLAY_NAME, EMAIL_ADDRESS, LOGIN_COUNT, LAST_LOGIN_IP, LAST_LOGIN_UA, LAST_LOGIN_TIME, CREATE_TIME, UPDATE_TIME)",
            "VALUES (#{loginName}, #{password}, #{passwordSalt}, #{role,typeHandler=com.codeager.portal.dao.type.RoleTypeHandler}, #{displayName}, #{emailAddress}, #{loginCount}, " +
                    "#{lastLoginIp}, #{lastLoginUserAgent}, #{lastLoginTime}, #{createTime}, #{updateTime})"
    })
    void insert(Administrator administrator);

    @Select({
            "SELECT *",
            "FROM t_administrator",
            "WHERE ADMINISTRATOR_ID = #{id}"
    })
    @ResultMap("administratorResult")
    Administrator selectById(long id);

    @Select({
            "SELECT *",
            "FROM t_administrator, t_user_access",
            "WHERE ROLE >= 8 and USER_ID = ADMINISTRATOR_ID"
    })
    @ResultMap("administratorResult")
    List<Administrator> selectAllLoggedRecords();

    @Select({
            "SELECT *",
            "FROM t_administrator"
    })
    @ResultMap("administratorResult")
    List<Administrator> selectAll();

    @Select({
            "SELECT *",
            "FROM t_administrator",
            "WHERE ROLE = 15"
    })
    @ResultMap("administratorResult")
    List<Administrator> selectAllEditors();

    @Select({
            "SELECT *",
            "FROM t_administrator",
            "WHERE ROLE = 63"
    })
    @ResultMap("administratorResult")
    List<Administrator> selectAllAdministrators();

    @Update({
            "UPDATE t_administrator",
            "SET",
            "LOGIN_NAME = #{loginName},",
            "PASSWORD = #{password},",
            "PW_SALT = #{passwordSalt},",
            "ROLE = #{role,typeHandler=com.codeager.portal.dao.type.RoleTypeHandler},",
            "DISPLAY_NAME = #{displayName},",
            "EMAIL_ADDRESS = #{emailAddress},",
            "LOGIN_COUNT = #{loginCount},",
            "LAST_LOGIN_IP = #{lastLoginIp},",
            "LAST_LOGIN_UA = #{lastLoginUserAgent},",
            "LAST_LOGIN_TIME = #{lastLoginTime},",
            "CREATE_TIME = #{createTime},",
            "UPDATE_TIME = #{updateTime}",
            "WHERE ADMINISTRATOR_ID = #{id}"
    })
    void updateById(Administrator administrator);

    @Delete({
            "DELETE FROM t_administrator",
            "WHERE ADMINISTRATOR_ID = #{id}"
    })
    void deleteById(long id);
}
