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

import com.codeager.ecom.domain.ProductImage;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface ProductImageMapper {
    @Delete({
            "delete from t_product_image",
            "where PRODUCT_IMAGE_ID = #{id,jdbcType=BIGINT}"
    })
    int deleteById(long id);

    @Insert({
            "insert into t_product_image (PRODUCT_ID, IMAGE_URL, ",
            "IF_PRIOR, CREATE_TIME, ",
            "UPDATE_TIME)",
            "values (#{productId,jdbcType=BIGINT}, #{imageUrl,jdbcType=VARCHAR}, ",
            "#{prior,jdbcType=TINYINT}, #{createTime,jdbcType=TIMESTAMP}, ",
            "#{updateTime,jdbcType=TIMESTAMP})"
    })
    @SelectKey(statement = "SELECT LAST_INSERT_ID()", keyProperty = "id", before = false, resultType = long.class)
    int insert(ProductImage record);

    @Select({
            "select",
            "PRODUCT_IMAGE_ID, PRODUCT_ID, IMAGE_URL, IF_PRIOR, CREATE_TIME, UPDATE_TIME",
            "from t_product_image",
            "where PRODUCT_IMAGE_ID = #{id,jdbcType=BIGINT}"
    })
    @Results({
            @Result(column = "PRODUCT_IMAGE_ID", property = "id", jdbcType = JdbcType.BIGINT, id = true),
            @Result(column = "PRODUCT_ID", property = "productId", jdbcType = JdbcType.BIGINT),
            @Result(column = "IMAGE_URL", property = "imageUrl", jdbcType = JdbcType.VARCHAR),
            @Result(column = "IF_PRIOR", property = "prior", jdbcType = JdbcType.TINYINT),
            @Result(column = "CREATE_TIME", property = "createTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "UPDATE_TIME", property = "updateTime", jdbcType = JdbcType.TIMESTAMP)
    })
    ProductImage selectById(long id);

    @Select({
            "select",
            "PRODUCT_IMAGE_ID, PRODUCT_ID, IMAGE_URL, IF_PRIOR, CREATE_TIME, UPDATE_TIME",
            "from t_product_image",
            "where PRODUCT_ID = #{id,jdbcType=BIGINT}"
    })
    @Results({
            @Result(column = "PRODUCT_IMAGE_ID", property = "id", jdbcType = JdbcType.BIGINT, id = true),
            @Result(column = "PRODUCT_ID", property = "productId", jdbcType = JdbcType.BIGINT),
            @Result(column = "IMAGE_URL", property = "imageUrl", jdbcType = JdbcType.VARCHAR),
            @Result(column = "IF_PRIOR", property = "prior", jdbcType = JdbcType.TINYINT),
            @Result(column = "CREATE_TIME", property = "createTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "UPDATE_TIME", property = "updateTime", jdbcType = JdbcType.TIMESTAMP)
    })
    List<ProductImage> selectByProductId(long productId);

    @Select({
            "select",
            "PRODUCT_IMAGE_ID, PRODUCT_ID, IMAGE_URL, IF_PRIOR, CREATE_TIME, UPDATE_TIME",
            "from t_product_image",
            "where PRODUCT_ID = #{id,jdbcType=BIGINT} and IF_PRIOR = 1 limit 1"
    })
    @Results({
            @Result(column = "PRODUCT_IMAGE_ID", property = "id", jdbcType = JdbcType.BIGINT, id = true),
            @Result(column = "PRODUCT_ID", property = "productId", jdbcType = JdbcType.BIGINT),
            @Result(column = "IMAGE_URL", property = "imageUrl", jdbcType = JdbcType.VARCHAR),
            @Result(column = "IF_PRIOR", property = "prior", jdbcType = JdbcType.TINYINT),
            @Result(column = "CREATE_TIME", property = "createTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "UPDATE_TIME", property = "updateTime", jdbcType = JdbcType.TIMESTAMP)
    })
    ProductImage selectMainImageByProductId(long productId);

    @Select({
            "select",
            "PRODUCT_IMAGE_ID, PRODUCT_ID, IMAGE_URL, IF_PRIOR, CREATE_TIME, UPDATE_TIME",
            "from t_product_image",
            "where PRODUCT_ID = #{id,jdbcType=BIGINT} and IF_PRIOR = 0"
    })
    @Results({
            @Result(column = "PRODUCT_IMAGE_ID", property = "id", jdbcType = JdbcType.BIGINT, id = true),
            @Result(column = "PRODUCT_ID", property = "productId", jdbcType = JdbcType.BIGINT),
            @Result(column = "IMAGE_URL", property = "imageUrl", jdbcType = JdbcType.VARCHAR),
            @Result(column = "IF_PRIOR", property = "prior", jdbcType = JdbcType.TINYINT),
            @Result(column = "CREATE_TIME", property = "createTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "UPDATE_TIME", property = "updateTime", jdbcType = JdbcType.TIMESTAMP)
    })
    List<ProductImage> selectSubImagesByProductId(long productId);

    @Select({
            "select",
            "PRODUCT_IMAGE_ID, PRODUCT_ID, IMAGE_URL, IF_PRIOR, CREATE_TIME, UPDATE_TIME",
            "from t_product_image"
    })
    @Results({
            @Result(column = "PRODUCT_IMAGE_ID", property = "id", jdbcType = JdbcType.BIGINT, id = true),
            @Result(column = "PRODUCT_ID", property = "productId", jdbcType = JdbcType.BIGINT),
            @Result(column = "IMAGE_URL", property = "imageUrl", jdbcType = JdbcType.VARCHAR),
            @Result(column = "IF_PRIOR", property = "prior", jdbcType = JdbcType.TINYINT),
            @Result(column = "CREATE_TIME", property = "createTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "UPDATE_TIME", property = "updateTime", jdbcType = JdbcType.TIMESTAMP)
    })
    List<ProductImage> selectAll();

    @Update({
            "update t_product_image",
            "set PRODUCT_ID = #{productId,jdbcType=BIGINT},",
            "IMAGE_URL = #{imageUrl,jdbcType=VARCHAR},",
            "IF_PRIOR = #{prior,jdbcType=TINYINT},",
            "CREATE_TIME = #{createTime,jdbcType=TIMESTAMP},",
            "UPDATE_TIME = #{updateTime,jdbcType=TIMESTAMP}",
            "where PRODUCT_IMAGE_ID = #{id,jdbcType=BIGINT}"
    })
    int updateById(ProductImage record);
}