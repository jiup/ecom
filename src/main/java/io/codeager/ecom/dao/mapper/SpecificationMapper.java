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

import com.codeager.ecom.domain.Specification;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Mapper
@Repository
public interface SpecificationMapper {
    @Delete({
            "delete from t_product_spec",
            "where PRODUCT_SPEC_ID = #{id,jdbcType=BIGINT}"
    })
    int deleteByPrimaryKey(long id);

    @Delete({
            "delete from t_product_spec",
            "where SKU_ID = #{id,jdbcType=BIGINT}"
    })
    int deleteBySkuId(long id);

    @Insert({
            "insert into t_product_spec (CATEGORY_ID, SKU_ID, ",
            "ATTR_NAME, UOM, ",
            "IF_GROUPED, CREATE_TIME, ",
            "ATTR_VALUE)",
            "values (#{categoryId,jdbcType=INTEGER}, #{skuId,jdbcType=BIGINT}, ",
            "#{name,jdbcType=VARCHAR}, #{unitsOfMeasurement,jdbcType=VARCHAR}, ",
            "#{grouped,jdbcType=TINYINT}, #{createTime,jdbcType=TIMESTAMP}, ",
            "#{value,jdbcType=LONGVARCHAR})"
    })
    @SelectKey(statement = "SELECT LAST_INSERT_ID()", keyProperty = "id", before = false, resultType = long.class)
    int insert(Specification record);

    @Select({
            "select",
            "PRODUCT_SPEC_ID, CATEGORY_ID, SKU_ID, ATTR_NAME, UOM, IF_GROUPED, CREATE_TIME, ",
            "ATTR_VALUE",
            "from t_product_spec",
            "where PRODUCT_SPEC_ID = #{id,jdbcType=BIGINT}"
    })
    @Results({
            @Result(column = "PRODUCT_SPEC_ID", property = "id", jdbcType = JdbcType.BIGINT, id = true),
            @Result(column = "CATEGORY_ID", property = "categoryId", jdbcType = JdbcType.INTEGER),
            @Result(column = "SKU_ID", property = "skuId", jdbcType = JdbcType.BIGINT),
            @Result(column = "ATTR_NAME", property = "name", jdbcType = JdbcType.VARCHAR),
            @Result(column = "UOM", property = "unitsOfMeasurement", jdbcType = JdbcType.VARCHAR),
            @Result(column = "IF_GROUPED", property = "grouped", jdbcType = JdbcType.TINYINT),
            @Result(column = "CREATE_TIME", property = "createTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "ATTR_VALUE", property = "value", jdbcType = JdbcType.LONGVARCHAR)
    })
    Specification selectByPrimaryKey(long id);

    @Select({
            "select",
            "PRODUCT_SPEC_ID, CATEGORY_ID, SKU_ID, ATTR_NAME, UOM, IF_GROUPED, CREATE_TIME, ",
            "ATTR_VALUE",
            "from t_product_spec",
            "where SKU_ID = #{id,jdbcType=BIGINT}"
    })
    @Results({
            @Result(column = "PRODUCT_SPEC_ID", property = "id", jdbcType = JdbcType.BIGINT, id = true),
            @Result(column = "CATEGORY_ID", property = "categoryId", jdbcType = JdbcType.INTEGER),
            @Result(column = "SKU_ID", property = "skuId", jdbcType = JdbcType.BIGINT),
            @Result(column = "ATTR_NAME", property = "name", jdbcType = JdbcType.VARCHAR),
            @Result(column = "UOM", property = "unitsOfMeasurement", jdbcType = JdbcType.VARCHAR),
            @Result(column = "IF_GROUPED", property = "grouped", jdbcType = JdbcType.TINYINT),
            @Result(column = "CREATE_TIME", property = "createTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "ATTR_VALUE", property = "value", jdbcType = JdbcType.LONGVARCHAR)
    })
    List<Specification> selectBySkuId(long id);

    @Select({
            "select",
            "PRODUCT_SPEC_ID, CATEGORY_ID, SKU_ID, ATTR_NAME, UOM, IF_GROUPED, CREATE_TIME, ",
            "ATTR_VALUE",
            "from t_product_spec",
            "where SKU_ID = #{skuId,jdbcType=BIGINT} and ATTR_NAME = #{name}"
    })
    @Results({
            @Result(column = "PRODUCT_SPEC_ID", property = "id", jdbcType = JdbcType.BIGINT, id = true),
            @Result(column = "CATEGORY_ID", property = "categoryId", jdbcType = JdbcType.INTEGER),
            @Result(column = "SKU_ID", property = "skuId", jdbcType = JdbcType.BIGINT),
            @Result(column = "ATTR_NAME", property = "name", jdbcType = JdbcType.VARCHAR),
            @Result(column = "UOM", property = "unitsOfMeasurement", jdbcType = JdbcType.VARCHAR),
            @Result(column = "IF_GROUPED", property = "grouped", jdbcType = JdbcType.TINYINT),
            @Result(column = "CREATE_TIME", property = "createTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "ATTR_VALUE", property = "value", jdbcType = JdbcType.LONGVARCHAR)
    })
    Specification selectBySkuIdAndAttrName(@Param("skuId") long skuId, @Param("name") String name);

    @Select({
            "select",
            "PRODUCT_SPEC_ID, CATEGORY_ID, SKU_ID, ATTR_NAME, UOM, IF_GROUPED, CREATE_TIME, ",
            "ATTR_VALUE",
            "from t_product_spec",
            "where CATEGORY_ID = #{categoryId,jdbcType=BIGINT} and ATTR_NAME = #{name}",
            "limit 1"
    })
    @Results({
            @Result(column = "PRODUCT_SPEC_ID", property = "id", jdbcType = JdbcType.BIGINT, id = true),
            @Result(column = "CATEGORY_ID", property = "categoryId", jdbcType = JdbcType.INTEGER),
            @Result(column = "SKU_ID", property = "skuId", jdbcType = JdbcType.BIGINT),
            @Result(column = "ATTR_NAME", property = "name", jdbcType = JdbcType.VARCHAR),
            @Result(column = "UOM", property = "unitsOfMeasurement", jdbcType = JdbcType.VARCHAR),
            @Result(column = "IF_GROUPED", property = "grouped", jdbcType = JdbcType.TINYINT),
            @Result(column = "CREATE_TIME", property = "createTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "ATTR_VALUE", property = "value", jdbcType = JdbcType.LONGVARCHAR)
    })
    Specification selectOneByCategoryIdAndAttrName(@Param("categoryId") long categoryId, @Param("name") String name);

    @Select({
            "select",
            "distinct ATTR_NAME, UOM, IF_GROUPED, CATEGORY_ID",
            "from t_product_spec",
            "where CATEGORY_ID = #{categoryId,jdbcType=BIGINT}"
    })
    @Results({
            @Result(column = "PRODUCT_SPEC_ID", property = "id", jdbcType = JdbcType.BIGINT, id = true),
            @Result(column = "CATEGORY_ID", property = "categoryId", jdbcType = JdbcType.INTEGER),
            @Result(column = "SKU_ID", property = "skuId", jdbcType = JdbcType.BIGINT),
            @Result(column = "ATTR_NAME", property = "name", jdbcType = JdbcType.VARCHAR),
            @Result(column = "UOM", property = "unitsOfMeasurement", jdbcType = JdbcType.VARCHAR),
            @Result(column = "IF_GROUPED", property = "grouped", jdbcType = JdbcType.TINYINT),
            @Result(column = "CREATE_TIME", property = "createTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "ATTR_VALUE", property = "value", jdbcType = JdbcType.LONGVARCHAR)
    })
    Set<Specification> selectDistinctNameAndUomAndGroupStatusByCategoryId(@Param("categoryId") long categoryId);

    @Select({
            "select",
            "PRODUCT_SPEC_ID, CATEGORY_ID, SKU_ID, ATTR_NAME, UOM, IF_GROUPED, CREATE_TIME, ",
            "ATTR_VALUE",
            "from t_product_spec"
    })
    @Results({
            @Result(column = "PRODUCT_SPEC_ID", property = "id", jdbcType = JdbcType.BIGINT, id = true),
            @Result(column = "CATEGORY_ID", property = "categoryId", jdbcType = JdbcType.INTEGER),
            @Result(column = "SKU_ID", property = "skuId", jdbcType = JdbcType.BIGINT),
            @Result(column = "ATTR_NAME", property = "name", jdbcType = JdbcType.VARCHAR),
            @Result(column = "UOM", property = "unitsOfMeasurement", jdbcType = JdbcType.VARCHAR),
            @Result(column = "IF_GROUPED", property = "grouped", jdbcType = JdbcType.TINYINT),
            @Result(column = "CREATE_TIME", property = "createTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "ATTR_VALUE", property = "value", jdbcType = JdbcType.LONGVARCHAR)
    })
    List<Specification> selectAll();

    @Update({
            "update t_product_spec",
            "set CATEGORY_ID = #{categoryId,jdbcType=INTEGER},",
            "SKU_ID = #{skuId,jdbcType=BIGINT},",
            "ATTR_NAME = #{name,jdbcType=VARCHAR},",
            "UOM = #{unitsOfMeasurement,jdbcType=VARCHAR},",
            "IF_GROUPED = #{grouped,jdbcType=TINYINT},",
            "CREATE_TIME = #{createTime,jdbcType=TIMESTAMP},",
            "ATTR_VALUE = #{value,jdbcType=LONGVARCHAR}",
            "where PRODUCT_SPEC_ID = #{id,jdbcType=BIGINT}"
    })
    int updateByPrimaryKey(Specification record);

    @Update({
            "update t_product_spec",
            "set IF_GROUPED = !IF_GROUPED",
            "where ATTR_NAME = #{attrName} and CATEGORY_ID = #{categoryId,jdbcType=INTEGER}"
    })
    int flipGroupStatusByAttrNameAndCategoryId(@Param("attrName") String attrName, @Param("categoryId") int categoryId);

    @Update({
            "update t_product_spec",
            "set CATEGORY_ID = #{to,jdbcType=INTEGER}",
            "where CATEGORY_ID = #{from,jdbcType=INTEGER}"
    })
    int updateCategoryIds(@Param("from") int from, @Param("to") int to);

    @Update({
            "update t_product_spec",
            "set CATEGORY_ID = #{categoryId,jdbcType=INTEGER}",
            "where SKU_ID = #{skuId,jdbcType=BIGINT}"
    })
    int updateCategoryIdsBySkuId(@Param("categoryId") int categoryId, @Param("skuId") long skuId);
}