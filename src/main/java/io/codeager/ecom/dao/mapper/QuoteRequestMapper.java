package com.codeager.ecom.dao.mapper;

import com.codeager.ecom.domain.QuoteRequest;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface QuoteRequestMapper {
    @Delete({
        "delete from t_quote_request",
        "where QUOTE_ID = #{id,jdbcType=BIGINT}"
    })
    int deleteById(long id);

    @Insert({
        "insert into t_quote_request (FIRST_NAME, LAST_NAME, ",
        "COMPANY_NAME, COMPANY_ADDRESS, ",
        "COMPANY_ADDRESS_EXT, COMPANY_ADDRESS_CITY, ",
        "COMPANY_ADDRESS_STATE, COMPANY_ADDRESS_ZIPCODE, ",
        "EMAIL, PHONE, IF_PREFER_CALL, ",
        "PRODUCT, ESTIMATE_QUANTITY, ",
        "IF_SENT, CREATE_TIME, ",
        "UPDATE_TIME, IF_DELETE, ",
        "DESCRIPTION)",
        "values (#{firstName,jdbcType=VARCHAR}, #{lastName,jdbcType=VARCHAR}, ",
        "#{companyName,jdbcType=VARCHAR}, #{address,jdbcType=VARCHAR}, ",
        "#{addressExtend,jdbcType=VARCHAR}, #{city,jdbcType=VARCHAR}, ",
        "#{state,jdbcType=VARCHAR}, #{zipCode,jdbcType=VARCHAR}, ",
        "#{email,jdbcType=VARCHAR}, #{phone,jdbcType=VARCHAR}, #{preferCall,jdbcType=TINYINT}, ",
        "#{product,jdbcType=VARCHAR}, #{estimateQuantity,jdbcType=INTEGER}, ",
        "#{sent,jdbcType=TINYINT}, #{createTime,jdbcType=TIMESTAMP}, ",
        "#{updateTime,jdbcType=TIMESTAMP}, #{deleted,jdbcType=TINYINT}, ",
        "#{description,jdbcType=LONGVARCHAR})"
    })
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="id", before=false, resultType=long.class)
    int insert(QuoteRequest record);

    @Select({
        "select",
        "QUOTE_ID, FIRST_NAME, LAST_NAME, COMPANY_NAME, COMPANY_ADDRESS, COMPANY_ADDRESS_EXT, ",
        "COMPANY_ADDRESS_CITY, COMPANY_ADDRESS_STATE, COMPANY_ADDRESS_ZIPCODE, EMAIL, ",
        "PHONE, IF_PREFER_CALL, PRODUCT, ESTIMATE_QUANTITY, IF_SENT, CREATE_TIME, UPDATE_TIME, ",
        "IF_DELETE, DESCRIPTION",
        "from t_quote_request",
        "where QUOTE_ID = #{id,jdbcType=BIGINT}"
    })
    @Results({
        @Result(column="QUOTE_ID", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="FIRST_NAME", property="firstName", jdbcType=JdbcType.VARCHAR),
        @Result(column="LAST_NAME", property="lastName", jdbcType=JdbcType.VARCHAR),
        @Result(column="COMPANY_NAME", property="companyName", jdbcType=JdbcType.VARCHAR),
        @Result(column="COMPANY_ADDRESS", property="address", jdbcType=JdbcType.VARCHAR),
        @Result(column="COMPANY_ADDRESS_EXT", property="addressExtend", jdbcType=JdbcType.VARCHAR),
        @Result(column="COMPANY_ADDRESS_CITY", property="city", jdbcType=JdbcType.VARCHAR),
        @Result(column="COMPANY_ADDRESS_STATE", property="state", jdbcType=JdbcType.VARCHAR),
        @Result(column="COMPANY_ADDRESS_ZIPCODE", property="zipCode", jdbcType=JdbcType.VARCHAR),
        @Result(column="EMAIL", property="email", jdbcType=JdbcType.VARCHAR),
        @Result(column="PHONE", property="phone", jdbcType=JdbcType.VARCHAR),
        @Result(column="IF_PREFER_CALL", property="preferCall", jdbcType=JdbcType.TINYINT),
        @Result(column="PRODUCT", property="product", jdbcType=JdbcType.VARCHAR),
        @Result(column="ESTIMATE_QUANTITY", property="estimateQuantity", jdbcType=JdbcType.INTEGER),
        @Result(column="IF_SENT", property="sent", jdbcType=JdbcType.TINYINT),
        @Result(column="CREATE_TIME", property="createTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="UPDATE_TIME", property="updateTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="IF_DELETE", property="deleted", jdbcType=JdbcType.TINYINT),
        @Result(column="DESCRIPTION", property="description", jdbcType=JdbcType.LONGVARCHAR)
    })
    QuoteRequest selectById(long id);

    @Select({
        "select",
        "QUOTE_ID, FIRST_NAME, LAST_NAME, COMPANY_NAME, COMPANY_ADDRESS, COMPANY_ADDRESS_EXT, ",
        "COMPANY_ADDRESS_CITY, COMPANY_ADDRESS_STATE, COMPANY_ADDRESS_ZIPCODE, EMAIL, ",
        "PHONE, IF_PREFER_CALL, PRODUCT, ESTIMATE_QUANTITY, IF_SENT, CREATE_TIME, UPDATE_TIME, ",
        "IF_DELETE, DESCRIPTION",
        "from t_quote_request",
        "where IF_DELETE <> 1",
        "order by UPDATE_TIME DESC"
    })
    @Results({
        @Result(column="QUOTE_ID", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="FIRST_NAME", property="firstName", jdbcType=JdbcType.VARCHAR),
        @Result(column="LAST_NAME", property="lastName", jdbcType=JdbcType.VARCHAR),
        @Result(column="COMPANY_NAME", property="companyName", jdbcType=JdbcType.VARCHAR),
        @Result(column="COMPANY_ADDRESS", property="address", jdbcType=JdbcType.VARCHAR),
        @Result(column="COMPANY_ADDRESS_EXT", property="addressExtend", jdbcType=JdbcType.VARCHAR),
        @Result(column="COMPANY_ADDRESS_CITY", property="city", jdbcType=JdbcType.VARCHAR),
        @Result(column="COMPANY_ADDRESS_STATE", property="state", jdbcType=JdbcType.VARCHAR),
        @Result(column="COMPANY_ADDRESS_ZIPCODE", property="zipCode", jdbcType=JdbcType.VARCHAR),
        @Result(column="EMAIL", property="email", jdbcType=JdbcType.VARCHAR),
        @Result(column="PHONE", property="phone", jdbcType=JdbcType.VARCHAR),
        @Result(column="IF_PREFER_CALL", property="preferCall", jdbcType=JdbcType.TINYINT),
        @Result(column="PRODUCT", property="product", jdbcType=JdbcType.VARCHAR),
        @Result(column="ESTIMATE_QUANTITY", property="estimateQuantity", jdbcType=JdbcType.INTEGER),
        @Result(column="IF_SENT", property="sent", jdbcType=JdbcType.TINYINT),
        @Result(column="CREATE_TIME", property="createTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="UPDATE_TIME", property="updateTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="IF_DELETE", property="deleted", jdbcType=JdbcType.TINYINT),
        @Result(column="DESCRIPTION", property="description", jdbcType=JdbcType.LONGVARCHAR)
    })
    List<QuoteRequest> selectAll();

    @Update({
        "update t_quote_request",
        "set FIRST_NAME = #{firstName,jdbcType=VARCHAR},",
          "LAST_NAME = #{lastName,jdbcType=VARCHAR},",
          "COMPANY_NAME = #{companyName,jdbcType=VARCHAR},",
          "COMPANY_ADDRESS = #{address,jdbcType=VARCHAR},",
          "COMPANY_ADDRESS_EXT = #{addressExtend,jdbcType=VARCHAR},",
          "COMPANY_ADDRESS_CITY = #{city,jdbcType=VARCHAR},",
          "COMPANY_ADDRESS_STATE = #{state,jdbcType=VARCHAR},",
          "COMPANY_ADDRESS_ZIPCODE = #{zipCode,jdbcType=VARCHAR},",
          "EMAIL = #{email,jdbcType=VARCHAR},",
          "PHONE = #{phone,jdbcType=VARCHAR},",
          "IF_PREFER_CALL = #{preferCall,jdbcType=TINYINT},",
          "PRODUCT = #{product,jdbcType=VARCHAR},",
          "ESTIMATE_QUANTITY = #{estimateQuantity,jdbcType=INTEGER},",
          "IF_SENT = #{sent,jdbcType=TINYINT},",
          "CREATE_TIME = #{createTime,jdbcType=TIMESTAMP},",
          "UPDATE_TIME = #{updateTime,jdbcType=TIMESTAMP},",
          "IF_DELETE = #{deleted,jdbcType=TINYINT},",
          "DESCRIPTION = #{description,jdbcType=LONGVARCHAR}",
        "where QUOTE_ID = #{id,jdbcType=BIGINT}"
    })
    int updateById(QuoteRequest record);

    @Update({
            "update t_quote_request",
            "set IF_DELETE = 1",
            "where QUOTE_ID = #{id,jdbcType=BIGINT}"
    })
    int softDeleteById(long id);
}