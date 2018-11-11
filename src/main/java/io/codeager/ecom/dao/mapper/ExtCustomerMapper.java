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

import com.codeager.ecom.domain.ExtCustomer;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface ExtCustomerMapper {
    @Delete({
            "delete from t_customer_ext",
            "where CUSTOMER_EXT_ID = #{id,jdbcType=BIGINT}"
    })
    int deleteById(long id);

    @Insert({
            "insert into t_customer_ext (CONTACT_NAME, COMPANY_NAME, ",
            "BUSINESS_TYPE, LICENSE_TYPE, ",
            "EMAIL, PHONE_NUMBER, ",
            "ADDRESS, STATE, ",
            "COUNTRY, WEBSITE, ",
            "FACEBOOK, INSTAGRAM, ",
            "TWITTER, UPDATE_TIME, ",
            "IF_DELETE, IF_UNSUBSCRIBE, ",
            "MEMO)",
            "values (#{contactName,jdbcType=VARCHAR}, #{companyName,jdbcType=VARCHAR}, ",
            "#{businessType,jdbcType=VARCHAR}, #{licenseType,jdbcType=VARCHAR}, ",
            "#{email,jdbcType=VARCHAR}, #{phoneNumber,jdbcType=VARCHAR}, ",
            "#{address,jdbcType=VARCHAR}, #{state,jdbcType=VARCHAR}, ",
            "#{country,jdbcType=VARCHAR}, #{website,jdbcType=VARCHAR}, ",
            "#{facebook,jdbcType=VARCHAR}, #{instagram,jdbcType=VARCHAR}, ",
            "#{twitter,jdbcType=VARCHAR}, #{updateTime,jdbcType=TIMESTAMP}, ",
            "#{deleted,jdbcType=TINYINT}, #{unsubscribe,jdbcType=TINYINT}, ",
            "#{memo,jdbcType=LONGVARCHAR})"
    })
    @SelectKey(statement = "SELECT LAST_INSERT_ID()", keyProperty = "id", before = false, resultType = long.class)
    int insert(ExtCustomer record);

    @Select({
            "select",
            "CUSTOMER_EXT_ID, CONTACT_NAME, COMPANY_NAME, BUSINESS_TYPE, LICENSE_TYPE, EMAIL, ",
            "PHONE_NUMBER, ADDRESS, STATE, COUNTRY, WEBSITE, FACEBOOK, INSTAGRAM, TWITTER, ",
            "UPDATE_TIME, IF_DELETE, IF_UNSUBSCRIBE, MEMO",
            "from t_customer_ext",
            "where CUSTOMER_EXT_ID = #{id,jdbcType=BIGINT}"
    })
    @Results({
            @Result(column = "CUSTOMER_EXT_ID", property = "id", jdbcType = JdbcType.BIGINT, id = true),
            @Result(column = "CONTACT_NAME", property = "contactName", jdbcType = JdbcType.VARCHAR),
            @Result(column = "COMPANY_NAME", property = "companyName", jdbcType = JdbcType.VARCHAR),
            @Result(column = "BUSINESS_TYPE", property = "businessType", jdbcType = JdbcType.VARCHAR),
            @Result(column = "LICENSE_TYPE", property = "licenseType", jdbcType = JdbcType.VARCHAR),
            @Result(column = "EMAIL", property = "email", jdbcType = JdbcType.VARCHAR),
            @Result(column = "PHONE_NUMBER", property = "phoneNumber", jdbcType = JdbcType.VARCHAR),
            @Result(column = "ADDRESS", property = "address", jdbcType = JdbcType.VARCHAR),
            @Result(column = "STATE", property = "state", jdbcType = JdbcType.VARCHAR),
            @Result(column = "COUNTRY", property = "country", jdbcType = JdbcType.VARCHAR),
            @Result(column = "WEBSITE", property = "website", jdbcType = JdbcType.VARCHAR),
            @Result(column = "FACEBOOK", property = "facebook", jdbcType = JdbcType.VARCHAR),
            @Result(column = "INSTAGRAM", property = "instagram", jdbcType = JdbcType.VARCHAR),
            @Result(column = "TWITTER", property = "twitter", jdbcType = JdbcType.VARCHAR),
            @Result(column = "UPDATE_TIME", property = "updateTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "IF_DELETE", property = "deleted", jdbcType = JdbcType.TINYINT),
            @Result(column = "IF_UNSUBSCRIBE", property = "unsubscribe", jdbcType = JdbcType.TINYINT),
            @Result(column = "MEMO", property = "memo", jdbcType = JdbcType.LONGVARCHAR)
    })
    ExtCustomer selectById(long id);

    @Select({
            "select",
            "CUSTOMER_EXT_ID, CONTACT_NAME, COMPANY_NAME, BUSINESS_TYPE, LICENSE_TYPE, EMAIL, ",
            "PHONE_NUMBER, ADDRESS, STATE, COUNTRY, WEBSITE, FACEBOOK, INSTAGRAM, TWITTER, ",
            "UPDATE_TIME, IF_DELETE, IF_UNSUBSCRIBE, MEMO",
            "from t_customer_ext",
            "where IF_DELETE <> 1",
            "order by CUSTOMER_EXT_ID desc"
    })
    @Results({
            @Result(column = "CUSTOMER_EXT_ID", property = "id", jdbcType = JdbcType.BIGINT, id = true),
            @Result(column = "CONTACT_NAME", property = "contactName", jdbcType = JdbcType.VARCHAR),
            @Result(column = "COMPANY_NAME", property = "companyName", jdbcType = JdbcType.VARCHAR),
            @Result(column = "BUSINESS_TYPE", property = "businessType", jdbcType = JdbcType.VARCHAR),
            @Result(column = "LICENSE_TYPE", property = "licenseType", jdbcType = JdbcType.VARCHAR),
            @Result(column = "EMAIL", property = "email", jdbcType = JdbcType.VARCHAR),
            @Result(column = "PHONE_NUMBER", property = "phoneNumber", jdbcType = JdbcType.VARCHAR),
            @Result(column = "ADDRESS", property = "address", jdbcType = JdbcType.VARCHAR),
            @Result(column = "STATE", property = "state", jdbcType = JdbcType.VARCHAR),
            @Result(column = "COUNTRY", property = "country", jdbcType = JdbcType.VARCHAR),
            @Result(column = "WEBSITE", property = "website", jdbcType = JdbcType.VARCHAR),
            @Result(column = "FACEBOOK", property = "facebook", jdbcType = JdbcType.VARCHAR),
            @Result(column = "INSTAGRAM", property = "instagram", jdbcType = JdbcType.VARCHAR),
            @Result(column = "TWITTER", property = "twitter", jdbcType = JdbcType.VARCHAR),
            @Result(column = "UPDATE_TIME", property = "updateTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "IF_DELETE", property = "deleted", jdbcType = JdbcType.TINYINT),
            @Result(column = "IF_UNSUBSCRIBE", property = "unsubscribe", jdbcType = JdbcType.TINYINT),
            @Result(column = "MEMO", property = "memo", jdbcType = JdbcType.LONGVARCHAR)
    })
    List<ExtCustomer> selectAll();

    @Update({
            "update t_customer_ext",
            "set CONTACT_NAME = #{contactName,jdbcType=VARCHAR},",
            "COMPANY_NAME = #{companyName,jdbcType=VARCHAR},",
            "BUSINESS_TYPE = #{businessType,jdbcType=VARCHAR},",
            "LICENSE_TYPE = #{licenseType,jdbcType=VARCHAR},",
            "EMAIL = #{email,jdbcType=VARCHAR},",
            "PHONE_NUMBER = #{phoneNumber,jdbcType=VARCHAR},",
            "ADDRESS = #{address,jdbcType=VARCHAR},",
            "STATE = #{state,jdbcType=VARCHAR},",
            "COUNTRY = #{country,jdbcType=VARCHAR},",
            "WEBSITE = #{website,jdbcType=VARCHAR},",
            "FACEBOOK = #{facebook,jdbcType=VARCHAR},",
            "INSTAGRAM = #{instagram,jdbcType=VARCHAR},",
            "TWITTER = #{twitter,jdbcType=VARCHAR},",
            "UPDATE_TIME = #{updateTime,jdbcType=TIMESTAMP},",
            "IF_DELETE = #{deleted,jdbcType=TINYINT},",
            "IF_UNSUBSCRIBE = #{unsubscribe,jdbcType=TINYINT},",
            "MEMO = #{memo,jdbcType=LONGVARCHAR}",
            "where CUSTOMER_EXT_ID = #{id,jdbcType=BIGINT}"
    })
    int updateById(ExtCustomer record);

    @Update({
            "update t_customer_ext",
            "set IF_DELETE = 1",
            "where CUSTOMER_EXT_ID = #{id,jdbcType=BIGINT}"
    })
    int softDeleteById(long id);

    @Update({
            "update t_customer_ext",
            "set IF_UNSUBSCRIBE = 1",
            "where CUSTOMER_EXT_ID = #{id,jdbcType=BIGINT}"
    })
    int unsubscribeById(long id);
}