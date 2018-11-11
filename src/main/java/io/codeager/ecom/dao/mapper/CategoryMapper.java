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

import io.codeager.ecom.domain.Category;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface CategoryMapper {
    @Delete({
            "delete from t_category",
            "where CATEGORY_ID = #{id,jdbcType=INTEGER}"
    })
    int deleteByPrimaryKey(int id);

    @Insert({
            "insert into t_category (PATH, CATEGORY_NAME, ",
            "PROMPT, IF_HIDDEN, ",
            "CREATE_TIME, UPDATE_TIME)",
            "values (#{path,jdbcType=VARCHAR}, #{name,jdbcType=VARCHAR}, ",
            "#{prompt,jdbcType=VARCHAR}, #{hidden,jdbcType=TINYINT}, ",
            "#{createTime,jdbcType=TIMESTAMP}, #{updateTime,jdbcType=TIMESTAMP})"
    })
    @SelectKey(statement = "SELECT LAST_INSERT_ID()", keyProperty = "id", before = false, resultType = int.class)
    int insert(Category record);

    @Select({
            "select",
            "CATEGORY_ID, PATH, CATEGORY_NAME, PROMPT, IF_HIDDEN, CREATE_TIME, UPDATE_TIME",
            "from t_category",
            "where CATEGORY_ID = #{id,jdbcType=INTEGER}"
    })
    @Results({
            @Result(column = "CATEGORY_ID", property = "id", jdbcType = JdbcType.INTEGER, id = true),
            @Result(column = "PATH", property = "path", jdbcType = JdbcType.VARCHAR),
            @Result(column = "CATEGORY_NAME", property = "name", jdbcType = JdbcType.VARCHAR),
            @Result(column = "PROMPT", property = "prompt", jdbcType = JdbcType.VARCHAR),
            @Result(column = "IF_HIDDEN", property = "hidden", jdbcType = JdbcType.TINYINT),
            @Result(column = "CREATE_TIME", property = "createTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "UPDATE_TIME", property = "updateTime", jdbcType = JdbcType.TIMESTAMP)
    })
    Category selectByPrimaryKey(int id);

    @Select({
            "select",
            "CATEGORY_ID, PATH, CATEGORY_NAME, PROMPT, IF_HIDDEN, CREATE_TIME, UPDATE_TIME",
            "from t_category",
            "where CATEGORY_NAME = #{name}"
    })
    @Results({
            @Result(column = "CATEGORY_ID", property = "id", jdbcType = JdbcType.INTEGER, id = true),
            @Result(column = "PATH", property = "path", jdbcType = JdbcType.VARCHAR),
            @Result(column = "CATEGORY_NAME", property = "name", jdbcType = JdbcType.VARCHAR),
            @Result(column = "PROMPT", property = "prompt", jdbcType = JdbcType.VARCHAR),
            @Result(column = "IF_HIDDEN", property = "hidden", jdbcType = JdbcType.TINYINT),
            @Result(column = "CREATE_TIME", property = "createTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "UPDATE_TIME", property = "updateTime", jdbcType = JdbcType.TIMESTAMP)
    })
    Category selectByCategoryName(String name);

    @Select({
            "select",
            "CATEGORY_ID, PATH, CATEGORY_NAME, PROMPT, IF_HIDDEN, CREATE_TIME, UPDATE_TIME",
            "from t_category",
            "order by PATH asc"
    })
    @Results({
            @Result(column = "CATEGORY_ID", property = "id", jdbcType = JdbcType.INTEGER, id = true),
            @Result(column = "PATH", property = "path", jdbcType = JdbcType.VARCHAR),
            @Result(column = "CATEGORY_NAME", property = "name", jdbcType = JdbcType.VARCHAR),
            @Result(column = "PROMPT", property = "prompt", jdbcType = JdbcType.VARCHAR),
            @Result(column = "IF_HIDDEN", property = "hidden", jdbcType = JdbcType.TINYINT),
            @Result(column = "CREATE_TIME", property = "createTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "UPDATE_TIME", property = "updateTime", jdbcType = JdbcType.TIMESTAMP)
    })
    List<Category> selectAll();

    @Select({
            "select",
            "CATEGORY_ID, PATH, CATEGORY_NAME, PROMPT, IF_HIDDEN, CREATE_TIME, UPDATE_TIME",
            "from t_category",
            "where PATH like concat(#{path}, '/%')"
    })
    @Results({
            @Result(column = "CATEGORY_ID", property = "id", jdbcType = JdbcType.INTEGER, id = true),
            @Result(column = "PATH", property = "path", jdbcType = JdbcType.VARCHAR),
            @Result(column = "CATEGORY_NAME", property = "name", jdbcType = JdbcType.VARCHAR),
            @Result(column = "PROMPT", property = "prompt", jdbcType = JdbcType.VARCHAR),
            @Result(column = "IF_HIDDEN", property = "hidden", jdbcType = JdbcType.TINYINT),
            @Result(column = "CREATE_TIME", property = "createTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "UPDATE_TIME", property = "updateTime", jdbcType = JdbcType.TIMESTAMP)
    })
    List<Category> selectChildrenByPath(String path);

    @Update({
            "update t_category",
            "set PATH = #{path,jdbcType=VARCHAR},",
            "CATEGORY_NAME = #{name,jdbcType=VARCHAR},",
            "PROMPT = #{prompt,jdbcType=VARCHAR},",
            "IF_HIDDEN = #{hidden,jdbcType=TINYINT},",
            "CREATE_TIME = #{createTime,jdbcType=TIMESTAMP},",
            "UPDATE_TIME = #{updateTime,jdbcType=TIMESTAMP}",
            "where CATEGORY_ID = #{id,jdbcType=INTEGER}"
    })
    int updateByPrimaryKey(Category record);

    @Update({
            "update t_category",
            "set PATH = replace(PATH, #{oldParentPath}, #{newParentPath})",
            "where PATH like concat(#{oldParentPath}, '/%')"
    })
    int updatePath(@Param("oldParentPath") String oldParentPath, @Param("newParentPath") String newParentPath);
}