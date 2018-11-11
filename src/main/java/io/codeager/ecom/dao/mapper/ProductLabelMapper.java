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

import com.codeager.ecom.dao.type.CommaSeparatedStringTypeHandler;
import com.codeager.ecom.domain.ProductLabel;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.type.JdbcType;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Jiupeng Zhang
 * @since 02/25/2018
 */
@Mapper
@Repository
public interface ProductLabelMapper {
    @Select({
            "SELECT",
            "t_product.PRODUCT_ID, PRODUCT_NAME, OVERVIEW, MAIN_IMAGE, SUB_IMAGES",
            "FROM t_product",
            "LEFT JOIN (",
            "SELECT PRODUCT_ID, SUBSTRING_INDEX(GROUP_CONCAT(IMAGE_URL), ',', 1) AS MAIN_IMAGE, SUBSTRING_INDEX(GROUP_CONCAT(IMAGE_URL), ',', -2) AS SUB_IMAGES",
            "FROM t_product_image",
            "GROUP BY PRODUCT_ID",
            "ORDER BY IF_PRIOR",
            ") temp_product_image",
            "ON temp_product_image.PRODUCT_ID = t_product.PRODUCT_ID",
            "WHERE IF_DELETE <> 1",
            "GROUP BY t_product.PRODUCT_ID",
            "ORDER BY SORT_INDEX ASC"
    })
    @Results({
            @Result(column = "PRODUCT_ID", property = "id", jdbcType = JdbcType.BIGINT, id = true),
            @Result(column = "PRODUCT_NAME", property = "name", jdbcType = JdbcType.VARCHAR),
            @Result(column = "OVERVIEW", property = "overview", jdbcType = JdbcType.LONGVARCHAR),
            @Result(column = "MAIN_IMAGE", property = "mainImage", jdbcType = JdbcType.VARCHAR),
            @Result(column = "SUB_IMAGES", property = "subImages", jdbcType = JdbcType.VARCHAR,
                    typeHandler = CommaSeparatedStringTypeHandler.class)
    })
    List<ProductLabel> selectAll();

    @Select({
            "SELECT",
            "t_product.PRODUCT_ID, PRODUCT_NAME, OVERVIEW, MAIN_IMAGE, SUB_IMAGES",
            "FROM t_product",
            "LEFT JOIN (",
            "SELECT PRODUCT_ID, SUBSTRING_INDEX(GROUP_CONCAT(IMAGE_URL), ',', 1) AS MAIN_IMAGE, SUBSTRING_INDEX(GROUP_CONCAT(IMAGE_URL), ',', -2) AS SUB_IMAGES",
            "FROM t_product_image",
            "GROUP BY PRODUCT_ID",
            "ORDER BY IF_PRIOR",
            ") temp_product_image",
            "ON temp_product_image.PRODUCT_ID = t_product.PRODUCT_ID",
            "WHERE t_product.PRODUCT_ID = #{id,jdbcType=BIGINT}"
    })
    @Results({
            @Result(column = "PRODUCT_ID", property = "id", jdbcType = JdbcType.BIGINT, id = true),
            @Result(column = "PRODUCT_NAME", property = "name", jdbcType = JdbcType.VARCHAR),
            @Result(column = "OVERVIEW", property = "overview", jdbcType = JdbcType.LONGVARCHAR),
            @Result(column = "MAIN_IMAGE", property = "mainImage", jdbcType = JdbcType.VARCHAR),
            @Result(column = "SUB_IMAGES", property = "subImages", jdbcType = JdbcType.VARCHAR,
                    typeHandler = CommaSeparatedStringTypeHandler.class)
    })
    ProductLabel selectByProductId(long productId);
}

/*

SELECT
t_product.PRODUCT_ID, PRODUCT_NAME, OVERVIEW, MAIN_IMAGE, SUB_IMAGES
FROM t_product
LEFT JOIN (
SELECT PRODUCT_ID, SUBSTRING_INDEX(GROUP_CONCAT(IMAGE_URL), ',', 1) AS MAIN_IMAGE, SUBSTRING_INDEX(GROUP_CONCAT(IMAGE_URL), ',', -2) AS SUB_IMAGES
FROM t_product_image
GROUP BY PRODUCT_ID
ORDER BY IF_PRIOR
) temp_product_image
ON temp_product_image.PRODUCT_ID = t_product.PRODUCT_ID
where t_product.PRODUCT_ID = 5

 */