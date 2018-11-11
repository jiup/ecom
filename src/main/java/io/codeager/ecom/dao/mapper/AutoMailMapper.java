/*
 * Copyright 2018 Jiupeng Zhang
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

package io.codeager.ecom.dao.mapper;

import io.codeager.ecom.domain.AutoMail;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface AutoMailMapper {
    @Delete({
            "delete from t_automail",
            "where MAIL_ID = #{id,jdbcType=BIGINT}"
    })
    int deleteById(long id);

    @Insert({
            "insert into t_automail (MAIL_TYPE, SUBJECT, ",
            "MAIL_TO, RECEIVER_GROUP, RECEIVER_ID, ",
            "RECEIVER_NAME, COUNT, ",
            "TOKEN, CREATE_TIME, ",
            "UPDATE_TIME, IF_SENT, ",
            "IF_DELETE, CONTENT)",
            "values (#{type,jdbcType=VARCHAR}, #{subject,jdbcType=VARCHAR}, ",
            "#{mailTo,jdbcType=VARCHAR}, #{group,jdbcType=VARCHAR}, #{receiverId,jdbcType=BIGINT}, ",
            "#{receiverName,jdbcType=VARCHAR}, #{count,jdbcType=INTEGER}, ",
            "#{token,jdbcType=VARCHAR}, #{createTime,jdbcType=TIMESTAMP}, ",
            "#{updateTime,jdbcType=TIMESTAMP}, #{sent,jdbcType=TINYINT}, ",
            "#{deleted,jdbcType=TINYINT}, #{content,jdbcType=LONGVARCHAR})"
    })
    @SelectKey(statement = "SELECT LAST_INSERT_ID()", keyProperty = "id", before = false, resultType = long.class)
    int insert(AutoMail record);

    @Select({
            "select",
            "MAIL_ID, MAIL_TYPE, SUBJECT, MAIL_TO, RECEIVER_GROUP, RECEIVER_ID, RECEIVER_NAME, ",
            "COUNT, TOKEN, CREATE_TIME, UPDATE_TIME, IF_SENT, IF_DELETE, CONTENT",
            "from t_automail",
            "where MAIL_ID = #{id,jdbcType=BIGINT}"
    })
    @Results({
            @Result(column = "MAIL_ID", property = "id", jdbcType = JdbcType.BIGINT, id = true),
            @Result(column = "MAIL_TYPE", property = "type", jdbcType = JdbcType.VARCHAR),
            @Result(column = "SUBJECT", property = "subject", jdbcType = JdbcType.VARCHAR),
            @Result(column = "MAIL_TO", property = "mailTo", jdbcType = JdbcType.VARCHAR),
            @Result(column = "RECEIVER_GROUP", property = "group", jdbcType = JdbcType.VARCHAR),
            @Result(column = "RECEIVER_ID", property = "receiverId", jdbcType = JdbcType.BIGINT),
            @Result(column = "RECEIVER_NAME", property = "receiverName", jdbcType = JdbcType.VARCHAR),
            @Result(column = "COUNT", property = "count", jdbcType = JdbcType.INTEGER),
            @Result(column = "TOKEN", property = "token", jdbcType = JdbcType.VARCHAR),
            @Result(column = "CREATE_TIME", property = "createTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "UPDATE_TIME", property = "updateTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "IF_SENT", property = "sent", jdbcType = JdbcType.TINYINT),
            @Result(column = "IF_DELETE", property = "deleted", jdbcType = JdbcType.TINYINT),
            @Result(column = "CONTENT", property = "content", jdbcType = JdbcType.LONGVARCHAR)
    })
    AutoMail selectById(long id);

    @Select({
            "select",
            "MAIL_ID, MAIL_TYPE, SUBJECT, MAIL_TO, RECEIVER_GROUP, RECEIVER_ID, RECEIVER_NAME, ",
            "COUNT, TOKEN, CREATE_TIME, UPDATE_TIME, IF_SENT, IF_DELETE, CONTENT",
            "from t_automail",
            "where TOKEN = #{token}"
    })
    @Results({
            @Result(column = "MAIL_ID", property = "id", jdbcType = JdbcType.BIGINT, id = true),
            @Result(column = "MAIL_TYPE", property = "type", jdbcType = JdbcType.VARCHAR),
            @Result(column = "SUBJECT", property = "subject", jdbcType = JdbcType.VARCHAR),
            @Result(column = "MAIL_TO", property = "mailTo", jdbcType = JdbcType.VARCHAR),
            @Result(column = "RECEIVER_GROUP", property = "group", jdbcType = JdbcType.VARCHAR),
            @Result(column = "RECEIVER_ID", property = "receiverId", jdbcType = JdbcType.BIGINT),
            @Result(column = "RECEIVER_NAME", property = "receiverName", jdbcType = JdbcType.VARCHAR),
            @Result(column = "COUNT", property = "count", jdbcType = JdbcType.INTEGER),
            @Result(column = "TOKEN", property = "token", jdbcType = JdbcType.VARCHAR),
            @Result(column = "CREATE_TIME", property = "createTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "UPDATE_TIME", property = "updateTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "IF_SENT", property = "sent", jdbcType = JdbcType.TINYINT),
            @Result(column = "IF_DELETE", property = "deleted", jdbcType = JdbcType.TINYINT),
            @Result(column = "CONTENT", property = "content", jdbcType = JdbcType.LONGVARCHAR)
    })
    AutoMail selectByToken(String token);

    @Select({
            "select",
            "MAIL_ID, MAIL_TYPE, SUBJECT, MAIL_TO, RECEIVER_GROUP, RECEIVER_ID, RECEIVER_NAME, ",
            "COUNT, TOKEN, CREATE_TIME, UPDATE_TIME, IF_SENT, IF_DELETE, CONTENT",
            "from t_automail",
            "where IF_DELETE <> 1",
            "order by UPDATE_TIME desc"
    })
    @Results({
            @Result(column = "MAIL_ID", property = "id", jdbcType = JdbcType.BIGINT, id = true),
            @Result(column = "MAIL_TYPE", property = "type", jdbcType = JdbcType.VARCHAR),
            @Result(column = "SUBJECT", property = "subject", jdbcType = JdbcType.VARCHAR),
            @Result(column = "MAIL_TO", property = "mailTo", jdbcType = JdbcType.VARCHAR),
            @Result(column = "RECEIVER_GROUP", property = "group", jdbcType = JdbcType.VARCHAR),
            @Result(column = "RECEIVER_ID", property = "receiverId", jdbcType = JdbcType.BIGINT),
            @Result(column = "RECEIVER_NAME", property = "receiverName", jdbcType = JdbcType.VARCHAR),
            @Result(column = "COUNT", property = "count", jdbcType = JdbcType.INTEGER),
            @Result(column = "TOKEN", property = "token", jdbcType = JdbcType.VARCHAR),
            @Result(column = "CREATE_TIME", property = "createTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "UPDATE_TIME", property = "updateTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "IF_SENT", property = "sent", jdbcType = JdbcType.TINYINT),
            @Result(column = "IF_DELETE", property = "deleted", jdbcType = JdbcType.TINYINT),
            @Result(column = "CONTENT", property = "content", jdbcType = JdbcType.LONGVARCHAR)
    })
    List<AutoMail> selectAll();

    @Update({
            "update t_automail",
            "set MAIL_TYPE = #{type,jdbcType=VARCHAR},",
            "SUBJECT = #{subject,jdbcType=VARCHAR},",
            "MAIL_TO = #{mailTo,jdbcType=VARCHAR},",
            "RECEIVER_GROUP = #{group,jdbcType=VARCHAR},",
            "RECEIVER_ID = #{receiverId,jdbcType=BIGINT},",
            "RECEIVER_NAME = #{receiverName,jdbcType=VARCHAR},",
            "COUNT = #{count,jdbcType=INTEGER},",
            "TOKEN = #{token,jdbcType=VARCHAR},",
            "CREATE_TIME = #{createTime,jdbcType=TIMESTAMP},",
            "UPDATE_TIME = #{updateTime,jdbcType=TIMESTAMP},",
            "IF_SENT = #{sent,jdbcType=TINYINT},",
            "IF_DELETE = #{deleted,jdbcType=TINYINT},",
            "CONTENT = #{content,jdbcType=LONGVARCHAR}",
            "where MAIL_ID = #{id,jdbcType=BIGINT}"
    })
    int updateById(AutoMail record);

    @Update({
            "update t_automail",
            "set IF_DELETE = 1",
            "where MAIL_ID = #{id,jdbcType=BIGINT}"
    })
    int softDeleteById(long id);
}