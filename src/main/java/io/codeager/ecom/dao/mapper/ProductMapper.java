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

import com.codeager.ecom.domain.Product;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface ProductMapper {
    @Delete({
            "delete from t_product",
            "where PRODUCT_ID = #{id,jdbcType=BIGINT}"
    })
    int deleteById(long id);

    @Insert({
            "insert into t_product (CATEGORY_ID, PRODUCT_NAME, ",
            "SORT_INDEX, IF_DELETE, ",
            "CREATE_TIME, UPDATE_TIME, ",
            "OVERVIEW, ADMIN_NOTE)",
            "values (#{categoryId,jdbcType=INTEGER}, #{productName,jdbcType=VARCHAR}, ",
            "#{sortIndex,jdbcType=BIGINT}, #{deleted,jdbcType=TINYINT}, ",
            "#{createTime,jdbcType=TIMESTAMP}, #{updateTime,jdbcType=TIMESTAMP}, ",
            "#{overview,jdbcType=LONGVARCHAR}, #{adminNote,jdbcType=LONGVARCHAR})"
    })
    @SelectKey(statement = "SELECT LAST_INSERT_ID()", keyProperty = "id", before = false, resultType = long.class)
    int insert(Product record);

    @Select({
            "select",
            "PRODUCT_ID, CATEGORY_ID, PRODUCT_NAME, SORT_INDEX, IF_DELETE, CREATE_TIME, UPDATE_TIME, ",
            "OVERVIEW, ADMIN_NOTE",
            "from t_product",
            "where PRODUCT_ID = #{id,jdbcType=BIGINT}"
    })
    @Results({
            @Result(column = "PRODUCT_ID", property = "id", jdbcType = JdbcType.BIGINT, id = true),
            @Result(column = "CATEGORY_ID", property = "categoryId", jdbcType = JdbcType.INTEGER),
            @Result(column = "PRODUCT_NAME", property = "productName", jdbcType = JdbcType.VARCHAR),
            @Result(column = "SORT_INDEX", property = "sortIndex", jdbcType = JdbcType.BIGINT),
            @Result(column = "IF_DELETE", property = "deleted", jdbcType = JdbcType.TINYINT),
            @Result(column = "CREATE_TIME", property = "createTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "UPDATE_TIME", property = "updateTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "OVERVIEW", property = "overview", jdbcType = JdbcType.LONGVARCHAR),
            @Result(column = "ADMIN_NOTE", property = "adminNote", jdbcType = JdbcType.LONGVARCHAR)
    })
    Product selectById(long id);

    @Select({
            "select",
            "PRODUCT_ID, CATEGORY_ID, PRODUCT_NAME, SORT_INDEX, IF_DELETE, CREATE_TIME, UPDATE_TIME, ",
            "OVERVIEW, ADMIN_NOTE",
            "from t_product",
            "where CATEGORY_ID = #{categoryId}"
    })
    @Results({
            @Result(column = "PRODUCT_ID", property = "id", jdbcType = JdbcType.BIGINT, id = true),
            @Result(column = "CATEGORY_ID", property = "categoryId", jdbcType = JdbcType.INTEGER),
            @Result(column = "PRODUCT_NAME", property = "productName", jdbcType = JdbcType.VARCHAR),
            @Result(column = "SORT_INDEX", property = "sortIndex", jdbcType = JdbcType.BIGINT),
            @Result(column = "IF_DELETE", property = "deleted", jdbcType = JdbcType.TINYINT),
            @Result(column = "CREATE_TIME", property = "createTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "UPDATE_TIME", property = "updateTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "OVERVIEW", property = "overview", jdbcType = JdbcType.LONGVARCHAR),
            @Result(column = "ADMIN_NOTE", property = "adminNote", jdbcType = JdbcType.LONGVARCHAR)
    })
    List<Product> selectByCategoryId(long categoryId);

    @Select({
            "select",
            "PRODUCT_ID, CATEGORY_ID, PRODUCT_NAME, SORT_INDEX, IF_DELETE, CREATE_TIME, UPDATE_TIME, ",
            "OVERVIEW, ADMIN_NOTE",
            "from t_product",
            "where PRODUCT_NAME like concat('%',#{keyword},'%')",
            "order by SORT_INDEX asc"
    })
    @Results({
            @Result(column = "PRODUCT_ID", property = "id", jdbcType = JdbcType.BIGINT, id = true),
            @Result(column = "CATEGORY_ID", property = "categoryId", jdbcType = JdbcType.INTEGER),
            @Result(column = "PRODUCT_NAME", property = "productName", jdbcType = JdbcType.VARCHAR),
            @Result(column = "SORT_INDEX", property = "sortIndex", jdbcType = JdbcType.BIGINT),
            @Result(column = "IF_DELETE", property = "deleted", jdbcType = JdbcType.TINYINT),
            @Result(column = "CREATE_TIME", property = "createTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "UPDATE_TIME", property = "updateTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "OVERVIEW", property = "overview", jdbcType = JdbcType.LONGVARCHAR),
            @Result(column = "ADMIN_NOTE", property = "adminNote", jdbcType = JdbcType.LONGVARCHAR)
    })
    List<Product> selectByKeyword(String keyword);

    @Select({
            "select",
            "PRODUCT_ID, CATEGORY_ID, PRODUCT_NAME, SORT_INDEX, IF_DELETE, CREATE_TIME, UPDATE_TIME, ",
            "OVERVIEW, ADMIN_NOTE",
            "from t_product",
            "order by SORT_INDEX asc"
    })
    @Results({
            @Result(column = "PRODUCT_ID", property = "id", jdbcType = JdbcType.BIGINT, id = true),
            @Result(column = "CATEGORY_ID", property = "categoryId", jdbcType = JdbcType.INTEGER),
            @Result(column = "PRODUCT_NAME", property = "productName", jdbcType = JdbcType.VARCHAR),
            @Result(column = "SORT_INDEX", property = "sortIndex", jdbcType = JdbcType.BIGINT),
            @Result(column = "IF_DELETE", property = "deleted", jdbcType = JdbcType.TINYINT),
            @Result(column = "CREATE_TIME", property = "createTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "UPDATE_TIME", property = "updateTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "OVERVIEW", property = "overview", jdbcType = JdbcType.LONGVARCHAR),
            @Result(column = "ADMIN_NOTE", property = "adminNote", jdbcType = JdbcType.LONGVARCHAR)
    })
    List<Product> selectAll();

    @Update({
            "update t_product",
            "set CATEGORY_ID = #{categoryId,jdbcType=INTEGER},",
            "PRODUCT_NAME = #{productName,jdbcType=VARCHAR},",
            "SORT_INDEX = #{sortIndex,jdbcType=BIGINT},",
            "IF_DELETE = #{deleted,jdbcType=TINYINT},",
            "CREATE_TIME = #{createTime,jdbcType=TIMESTAMP},",
            "UPDATE_TIME = #{updateTime,jdbcType=TIMESTAMP},",
            "OVERVIEW = #{overview,jdbcType=LONGVARCHAR},",
            "ADMIN_NOTE = #{adminNote,jdbcType=LONGVARCHAR}",
            "where PRODUCT_ID = #{id,jdbcType=BIGINT}"
    })
    int updateById(Product record);

    @Update({
            "update t_product",
            "set CATEGORY_ID = #{to,jdbcType=INTEGER}",
            "where CATEGORY_ID = #{from,jdbcType=INTEGER}"
    })
    int updateCategoryIds(@Param("from") int from, @Param("to") int to);

    @Update({
            "update t_product",
            "set IF_DELETE = 1",
            "where PRODUCT_ID = #{id,jdbcType=BIGINT}"
    })
    int softDeleteById(long id);
}