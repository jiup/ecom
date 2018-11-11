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

import io.codeager.ecom.domain.ProductDetail;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface ProductDetailMapper {
    @Delete({
            "delete from t_product_detail",
            "where PRODUCT_DETAIL_ID = #{id,jdbcType=BIGINT}"
    })
    int deleteById(long id);

    @Insert({
            "insert into t_product_detail (PRODUCT_ID, IF_KV_PAIRS, ",
            "VISIT_COUNT, CREATE_TIME, ",
            "UPDATE_TIME, CONTENT)",
            "values (#{productId,jdbcType=BIGINT}, #{kVPairs,jdbcType=TINYINT}, ",
            "#{visitCount,jdbcType=BIGINT}, #{createTime,jdbcType=TIMESTAMP}, ",
            "#{updateTime,jdbcType=TIMESTAMP}, #{content,jdbcType=LONGVARCHAR})"
    })
    @SelectKey(statement = "SELECT LAST_INSERT_ID()", keyProperty = "id", before = false, resultType = long.class)
    int insert(ProductDetail record);

    @Select({
            "select",
            "PRODUCT_DETAIL_ID, PRODUCT_ID, IF_KV_PAIRS, VISIT_COUNT, CREATE_TIME, UPDATE_TIME, ",
            "CONTENT",
            "from t_product_detail",
            "where PRODUCT_DETAIL_ID = #{id,jdbcType=BIGINT}"
    })
    @Results({
            @Result(column = "PRODUCT_DETAIL_ID", property = "id", jdbcType = JdbcType.BIGINT, id = true),
            @Result(column = "PRODUCT_ID", property = "productId", jdbcType = JdbcType.BIGINT),
            @Result(column = "IF_KV_PAIRS", property = "kVPairs", jdbcType = JdbcType.TINYINT),
            @Result(column = "VISIT_COUNT", property = "visitCount", jdbcType = JdbcType.BIGINT),
            @Result(column = "CREATE_TIME", property = "createTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "UPDATE_TIME", property = "updateTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "CONTENT", property = "content", jdbcType = JdbcType.LONGVARCHAR)
    })
    ProductDetail selectById(long id);

    @Select({
            "select",
            "PRODUCT_DETAIL_ID, PRODUCT_ID, IF_KV_PAIRS, VISIT_COUNT, CREATE_TIME, UPDATE_TIME, ",
            "CONTENT",
            "from t_product_detail",
            "where PRODUCT_ID = #{id,jdbcType=BIGINT}"
    })
    @Results({
            @Result(column = "PRODUCT_DETAIL_ID", property = "id", jdbcType = JdbcType.BIGINT, id = true),
            @Result(column = "PRODUCT_ID", property = "productId", jdbcType = JdbcType.BIGINT),
            @Result(column = "IF_KV_PAIRS", property = "kVPairs", jdbcType = JdbcType.TINYINT),
            @Result(column = "VISIT_COUNT", property = "visitCount", jdbcType = JdbcType.BIGINT),
            @Result(column = "CREATE_TIME", property = "createTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "UPDATE_TIME", property = "updateTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "CONTENT", property = "content", jdbcType = JdbcType.LONGVARCHAR)
    })
    ProductDetail selectByProductId(long id);

    @Select({
            "select",
            "PRODUCT_DETAIL_ID, PRODUCT_ID, IF_KV_PAIRS, VISIT_COUNT, CREATE_TIME, UPDATE_TIME, ",
            "CONTENT",
            "from t_product_detail"
    })
    @Results({
            @Result(column = "PRODUCT_DETAIL_ID", property = "id", jdbcType = JdbcType.BIGINT, id = true),
            @Result(column = "PRODUCT_ID", property = "productId", jdbcType = JdbcType.BIGINT),
            @Result(column = "IF_KV_PAIRS", property = "kVPairs", jdbcType = JdbcType.TINYINT),
            @Result(column = "VISIT_COUNT", property = "visitCount", jdbcType = JdbcType.BIGINT),
            @Result(column = "CREATE_TIME", property = "createTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "UPDATE_TIME", property = "updateTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "CONTENT", property = "content", jdbcType = JdbcType.LONGVARCHAR)
    })
    List<ProductDetail> selectAll();

    @Update({
            "update t_product_detail",
            "set PRODUCT_ID = #{productId,jdbcType=BIGINT},",
            "IF_KV_PAIRS = #{kVPairs,jdbcType=TINYINT},",
            "VISIT_COUNT = #{visitCount,jdbcType=BIGINT},",
            "CREATE_TIME = #{createTime,jdbcType=TIMESTAMP},",
            "UPDATE_TIME = #{updateTime,jdbcType=TIMESTAMP},",
            "CONTENT = #{content,jdbcType=LONGVARCHAR}",
            "where PRODUCT_DETAIL_ID = #{id,jdbcType=BIGINT}"
    })
    int updateById(ProductDetail record);
}