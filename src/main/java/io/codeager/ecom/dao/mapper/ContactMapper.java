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

package com.codeager.ecom.dao.mapper;

import com.codeager.ecom.domain.Contact;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface ContactMapper {
    @Delete({
            "delete from t_contact",
            "where CONTACT_ID = #{id,jdbcType=BIGINT}"
    })
    int deleteById(long id);

    @Insert({
            "insert into t_contact (TYPE, FIRST_NAME, ",
            "LAST_NAME, EMAIL, ",
            "PHONE, SUBJECT, ",
            "IF_SENT, IF_DELETE, CREATE_TIME, ",
            "UPDATE_TIME, CONTENT)",
            "values (#{type,jdbcType=TINYINT}, #{firstName,jdbcType=VARCHAR}, ",
            "#{lastName,jdbcType=VARCHAR}, #{email,jdbcType=VARCHAR}, ",
            "#{phone,jdbcType=VARCHAR}, #{subject,jdbcType=VARCHAR}, ",
            "#{sent,jdbcType=TINYINT}, #{deleted,jdbcType=TINYINT}, #{createTime,jdbcType=TIMESTAMP}, ",
            "#{updateTime,jdbcType=TIMESTAMP}, #{content,jdbcType=LONGVARCHAR})"
    })
    @SelectKey(statement = "SELECT LAST_INSERT_ID()", keyProperty = "id", before = false, resultType = long.class)
    int insert(Contact record);

    @Select({
            "select",
            "CONTACT_ID, TYPE, FIRST_NAME, LAST_NAME, EMAIL, PHONE, SUBJECT, IF_SENT, IF_DELETE, ",
            "CREATE_TIME, UPDATE_TIME, CONTENT",
            "from t_contact",
            "where CONTACT_ID = #{id,jdbcType=BIGINT}"
    })
    @Results({
            @Result(column = "CONTACT_ID", property = "id", jdbcType = JdbcType.BIGINT, id = true),
            @Result(column = "TYPE", property = "type", jdbcType = JdbcType.TINYINT),
            @Result(column = "FIRST_NAME", property = "firstName", jdbcType = JdbcType.VARCHAR),
            @Result(column = "LAST_NAME", property = "lastName", jdbcType = JdbcType.VARCHAR),
            @Result(column = "EMAIL", property = "email", jdbcType = JdbcType.VARCHAR),
            @Result(column = "PHONE", property = "phone", jdbcType = JdbcType.VARCHAR),
            @Result(column = "SUBJECT", property = "subject", jdbcType = JdbcType.VARCHAR),
            @Result(column = "IF_SENT", property = "sent", jdbcType = JdbcType.TINYINT),
            @Result(column = "IF_DELETE", property = "deleted", jdbcType = JdbcType.TINYINT),
            @Result(column = "CREATE_TIME", property = "createTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "UPDATE_TIME", property = "updateTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "CONTENT", property = "content", jdbcType = JdbcType.LONGVARCHAR)
    })
    Contact selectById(long id);

    @Select({
            "select",
            "CONTACT_ID, TYPE, FIRST_NAME, LAST_NAME, EMAIL, PHONE, SUBJECT, IF_SENT, IF_DELETE, ",
            "CREATE_TIME, UPDATE_TIME, CONTENT",
            "from t_contact",
            "where IF_DELETE <> 1",
            "order by UPDATE_TIME DESC"
    })
    @Results({
            @Result(column = "CONTACT_ID", property = "id", jdbcType = JdbcType.BIGINT, id = true),
            @Result(column = "TYPE", property = "type", jdbcType = JdbcType.TINYINT),
            @Result(column = "FIRST_NAME", property = "firstName", jdbcType = JdbcType.VARCHAR),
            @Result(column = "LAST_NAME", property = "lastName", jdbcType = JdbcType.VARCHAR),
            @Result(column = "EMAIL", property = "email", jdbcType = JdbcType.VARCHAR),
            @Result(column = "PHONE", property = "phone", jdbcType = JdbcType.VARCHAR),
            @Result(column = "SUBJECT", property = "subject", jdbcType = JdbcType.VARCHAR),
            @Result(column = "IF_SENT", property = "sent", jdbcType = JdbcType.TINYINT),
            @Result(column = "IF_DELETE", property = "deleted", jdbcType = JdbcType.TINYINT),
            @Result(column = "CREATE_TIME", property = "createTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "UPDATE_TIME", property = "updateTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "CONTENT", property = "content", jdbcType = JdbcType.LONGVARCHAR)
    })
    List<Contact> selectAll();

    @Update({
            "update t_contact",
            "set TYPE = #{type,jdbcType=TINYINT},",
            "FIRST_NAME = #{firstName,jdbcType=VARCHAR},",
            "LAST_NAME = #{lastName,jdbcType=VARCHAR},",
            "EMAIL = #{email,jdbcType=VARCHAR},",
            "PHONE = #{phone,jdbcType=VARCHAR},",
            "SUBJECT = #{subject,jdbcType=VARCHAR},",
            "IF_SENT = #{sent,jdbcType=TINYINT},",
            "IF_DELETE = #{deleted,jdbcType=TINYINT},",
            "CREATE_TIME = #{createTime,jdbcType=TIMESTAMP},",
            "UPDATE_TIME = #{updateTime,jdbcType=TIMESTAMP},",
            "CONTENT = #{content,jdbcType=LONGVARCHAR}",
            "where CONTACT_ID = #{id,jdbcType=BIGINT}"
    })
    int updateById(Contact record);

    @Update({
            "update t_contact",
            "set IF_DELETE = 1",
            "where CONTACT_ID = #{id,jdbcType=BIGINT}"
    })
    int softDeleteById(long id);
}