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

import io.codeager.ecom.domain.StockKeepingUnit;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface StockKeepingUnitMapper {
    @Delete({
            "delete from t_sku",
            "where SKU_ID = #{id,jdbcType=BIGINT}"
    })
    int deleteByPrimaryKey(long id);

    @Insert({
            "insert into t_sku (PRODUCT_ID, SKU_SN, ",
            "QUANTITY, IF_FROZEN, ",
            "LIST_PRICE, PRICE, ",
            "CREATE_TIME, UPDATE_TIME)",
            "values (#{productId,jdbcType=BIGINT}, #{serialNumber,jdbcType=VARCHAR}, ",
            "#{quantity,jdbcType=INTEGER}, #{frozen,jdbcType=TINYINT}, ",
            "#{listPrice,jdbcType=DECIMAL}, #{price,jdbcType=DECIMAL}, ",
            "#{createTime,jdbcType=TIMESTAMP}, #{updateTime,jdbcType=TIMESTAMP})"
    })
    @SelectKey(statement = "SELECT LAST_INSERT_ID()", keyProperty = "id", before = false, resultType = long.class)
    int insert(StockKeepingUnit record);

    @Select({
            "select",
            "SKU_ID, PRODUCT_ID, SKU_SN, QUANTITY, IF_FROZEN, LIST_PRICE, PRICE, CREATE_TIME, ",
            "UPDATE_TIME",
            "from t_sku",
            "where SKU_ID = #{id,jdbcType=BIGINT}"
    })
    @Results({
            @Result(column = "SKU_ID", property = "id", jdbcType = JdbcType.BIGINT, id = true),
            @Result(column = "PRODUCT_ID", property = "productId", jdbcType = JdbcType.BIGINT),
            @Result(column = "SKU_SN", property = "serialNumber", jdbcType = JdbcType.VARCHAR),
            @Result(column = "QUANTITY", property = "quantity", jdbcType = JdbcType.INTEGER),
            @Result(column = "IF_FROZEN", property = "frozen", jdbcType = JdbcType.TINYINT),
            @Result(column = "LIST_PRICE", property = "listPrice", jdbcType = JdbcType.DECIMAL),
            @Result(column = "PRICE", property = "price", jdbcType = JdbcType.DECIMAL),
            @Result(column = "CREATE_TIME", property = "createTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "UPDATE_TIME", property = "updateTime", jdbcType = JdbcType.TIMESTAMP)
    })
    StockKeepingUnit selectByPrimaryKey(long id);

    @Select({
            "select",
            "SKU_ID, PRODUCT_ID, SKU_SN, QUANTITY, IF_FROZEN, LIST_PRICE, PRICE, CREATE_TIME, ",
            "UPDATE_TIME",
            "from t_sku",
            "where SKU_SN = #{skuSeriesNumber} and PRODUCT_ID = #{productId}",
            "limit 1"
    })
    @Results({
            @Result(column = "SKU_ID", property = "id", jdbcType = JdbcType.BIGINT, id = true),
            @Result(column = "PRODUCT_ID", property = "productId", jdbcType = JdbcType.BIGINT),
            @Result(column = "SKU_SN", property = "serialNumber", jdbcType = JdbcType.VARCHAR),
            @Result(column = "QUANTITY", property = "quantity", jdbcType = JdbcType.INTEGER),
            @Result(column = "IF_FROZEN", property = "frozen", jdbcType = JdbcType.TINYINT),
            @Result(column = "LIST_PRICE", property = "listPrice", jdbcType = JdbcType.DECIMAL),
            @Result(column = "PRICE", property = "price", jdbcType = JdbcType.DECIMAL),
            @Result(column = "CREATE_TIME", property = "createTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "UPDATE_TIME", property = "updateTime", jdbcType = JdbcType.TIMESTAMP)
    })
    StockKeepingUnit selectBySeriesNumberAndProductId(@Param("skuSeriesNumber") String skuSeriesNumber, @Param("productId") long productId);

    @Select({
            "select",
            "SKU_ID, PRODUCT_ID, SKU_SN, QUANTITY, IF_FROZEN, LIST_PRICE, PRICE, CREATE_TIME, ",
            "UPDATE_TIME",
            "from t_sku"
    })
    @Results({
            @Result(column = "SKU_ID", property = "id", jdbcType = JdbcType.BIGINT, id = true),
            @Result(column = "PRODUCT_ID", property = "productId", jdbcType = JdbcType.BIGINT),
            @Result(column = "SKU_SN", property = "serialNumber", jdbcType = JdbcType.VARCHAR),
            @Result(column = "QUANTITY", property = "quantity", jdbcType = JdbcType.INTEGER),
            @Result(column = "IF_FROZEN", property = "frozen", jdbcType = JdbcType.TINYINT),
            @Result(column = "LIST_PRICE", property = "listPrice", jdbcType = JdbcType.DECIMAL),
            @Result(column = "PRICE", property = "price", jdbcType = JdbcType.DECIMAL),
            @Result(column = "CREATE_TIME", property = "createTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "UPDATE_TIME", property = "updateTime", jdbcType = JdbcType.TIMESTAMP)
    })
    List<StockKeepingUnit> selectAll();

    @Select({
            "select",
            "SKU_ID, PRODUCT_ID, SKU_SN, QUANTITY, IF_FROZEN, LIST_PRICE, PRICE, CREATE_TIME, ",
            "UPDATE_TIME",
            "from t_sku",
            "where PRODUCT_ID = #{productId}"
    })
    @Results({
            @Result(column = "SKU_ID", property = "id", jdbcType = JdbcType.BIGINT, id = true),
            @Result(column = "PRODUCT_ID", property = "productId", jdbcType = JdbcType.BIGINT),
            @Result(column = "SKU_SN", property = "serialNumber", jdbcType = JdbcType.VARCHAR),
            @Result(column = "QUANTITY", property = "quantity", jdbcType = JdbcType.INTEGER),
            @Result(column = "IF_FROZEN", property = "frozen", jdbcType = JdbcType.TINYINT),
            @Result(column = "LIST_PRICE", property = "listPrice", jdbcType = JdbcType.DECIMAL),
            @Result(column = "PRICE", property = "price", jdbcType = JdbcType.DECIMAL),
            @Result(column = "CREATE_TIME", property = "createTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "UPDATE_TIME", property = "updateTime", jdbcType = JdbcType.TIMESTAMP)
    })
    List<StockKeepingUnit> selectByProductId(long productId);

    @Update({
            "update t_sku",
            "set PRODUCT_ID = #{productId,jdbcType=BIGINT},",
            "SKU_SN = #{serialNumber,jdbcType=VARCHAR},",
            "QUANTITY = #{quantity,jdbcType=INTEGER},",
            "IF_FROZEN = #{frozen,jdbcType=TINYINT},",
            "LIST_PRICE = #{listPrice,jdbcType=DECIMAL},",
            "PRICE = #{price,jdbcType=DECIMAL},",
            "CREATE_TIME = #{createTime,jdbcType=TIMESTAMP},",
            "UPDATE_TIME = #{updateTime,jdbcType=TIMESTAMP}",
            "where SKU_ID = #{id,jdbcType=BIGINT}"
    })
    int updateByPrimaryKey(StockKeepingUnit record);
}