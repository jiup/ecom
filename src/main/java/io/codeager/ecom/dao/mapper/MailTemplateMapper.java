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

package com.codeager.ecom.dao.mapper;

import com.codeager.ecom.domain.MailTemplate;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface MailTemplateMapper {
    @Delete({
            "delete from t_mail_template",
            "where MAIL_TEMPLATE_ID = #{id,jdbcType=BIGINT}"
    })
    int deleteById(long id);

    @Insert({
            "insert into t_mail_template (MAIL_TEMPLATE_NAME, SUBJECT, ",
            "IMG_URL, TITLE, NOTE, ",
            "CREATE_TIME, UPDATE_TIME, ",
            "IF_DISABLE_UNSUBSCRIBE, IF_DELETE, ",
            "CONTENT)",
            "values (#{name,jdbcType=VARCHAR}, #{subject,jdbcType=VARCHAR}, ",
            "#{imgUrl,jdbcType=VARCHAR}, #{title,jdbcType=VARCHAR}, #{note,jdbcType=VARCHAR}, ",
            "#{createTime,jdbcType=TIMESTAMP}, #{updateTime,jdbcType=TIMESTAMP}, ",
            "#{hideUnsubscribeButton,jdbcType=TINYINT}, #{deleted,jdbcType=TINYINT}, ",
            "#{content,jdbcType=LONGVARCHAR})"
    })
    @SelectKey(statement = "SELECT LAST_INSERT_ID()", keyProperty = "id", before = false, resultType = long.class)
    int insert(MailTemplate record);

    @Select({
            "select",
            "MAIL_TEMPLATE_ID, MAIL_TEMPLATE_NAME, SUBJECT, IMG_URL, TITLE, NOTE, CREATE_TIME, ",
            "UPDATE_TIME, IF_DISABLE_UNSUBSCRIBE, IF_DELETE, CONTENT",
            "from t_mail_template",
            "where MAIL_TEMPLATE_ID = #{id,jdbcType=BIGINT}"
    })
    @Results({
            @Result(column = "MAIL_TEMPLATE_ID", property = "id", jdbcType = JdbcType.BIGINT, id = true),
            @Result(column = "MAIL_TEMPLATE_NAME", property = "name", jdbcType = JdbcType.VARCHAR),
            @Result(column = "SUBJECT", property = "subject", jdbcType = JdbcType.VARCHAR),
            @Result(column = "IMG_URL", property = "imgUrl", jdbcType = JdbcType.VARCHAR),
            @Result(column = "TITLE", property = "title", jdbcType = JdbcType.VARCHAR),
            @Result(column = "NOTE", property = "note", jdbcType = JdbcType.VARCHAR),
            @Result(column = "CREATE_TIME", property = "createTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "UPDATE_TIME", property = "updateTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "IF_DISABLE_UNSUBSCRIBE", property = "hideUnsubscribeButton", jdbcType = JdbcType.TINYINT),
            @Result(column = "IF_DELETE", property = "deleted", jdbcType = JdbcType.TINYINT),
            @Result(column = "CONTENT", property = "content", jdbcType = JdbcType.LONGVARCHAR)
    })
    MailTemplate selectById(long id);

    @Select({
            "select",
            "MAIL_TEMPLATE_ID, MAIL_TEMPLATE_NAME, SUBJECT, IMG_URL, TITLE, NOTE, CREATE_TIME, ",
            "UPDATE_TIME, IF_DISABLE_UNSUBSCRIBE, IF_DELETE, CONTENT",
            "from t_mail_template",
            "where IF_DELETE <> 1"
    })
    @Results({
            @Result(column = "MAIL_TEMPLATE_ID", property = "id", jdbcType = JdbcType.BIGINT, id = true),
            @Result(column = "MAIL_TEMPLATE_NAME", property = "name", jdbcType = JdbcType.VARCHAR),
            @Result(column = "SUBJECT", property = "subject", jdbcType = JdbcType.VARCHAR),
            @Result(column = "IMG_URL", property = "imgUrl", jdbcType = JdbcType.VARCHAR),
            @Result(column = "TITLE", property = "title", jdbcType = JdbcType.VARCHAR),
            @Result(column = "NOTE", property = "note", jdbcType = JdbcType.VARCHAR),
            @Result(column = "CREATE_TIME", property = "createTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "UPDATE_TIME", property = "updateTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "IF_DISABLE_UNSUBSCRIBE", property = "hideUnsubscribeButton", jdbcType = JdbcType.TINYINT),
            @Result(column = "IF_DELETE", property = "deleted", jdbcType = JdbcType.TINYINT),
            @Result(column = "CONTENT", property = "content", jdbcType = JdbcType.LONGVARCHAR)
    })
    List<MailTemplate> selectAll();

    @Update({
            "update t_mail_template",
            "set MAIL_TEMPLATE_NAME = #{name,jdbcType=VARCHAR},",
            "SUBJECT = #{subject,jdbcType=VARCHAR},",
            "IMG_URL = #{imgUrl,jdbcType=VARCHAR},",
            "TITLE = #{title,jdbcType=VARCHAR},",
            "NOTE = #{note,jdbcType=VARCHAR},",
            "CREATE_TIME = #{createTime,jdbcType=TIMESTAMP},",
            "UPDATE_TIME = #{updateTime,jdbcType=TIMESTAMP},",
            "IF_DISABLE_UNSUBSCRIBE = #{hideUnsubscribeButton,jdbcType=TINYINT},",
            "IF_DELETE = #{deleted,jdbcType=TINYINT},",
            "CONTENT = #{content,jdbcType=LONGVARCHAR}",
            "where MAIL_TEMPLATE_ID = #{id,jdbcType=BIGINT}"
    })
    int updateById(MailTemplate record);

    @Update({
            "update t_mail_template",
            "set IF_DELETE = 1",
            "where MAIL_TEMPLATE_ID = #{id,jdbcType=BIGINT}"
    })
    int softDeleteById(long id);
}