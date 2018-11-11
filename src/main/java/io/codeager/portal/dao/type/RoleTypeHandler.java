/*
 * Copyright 2017 Jiupeng Zhang
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

package com.codeager.portal.dao.type;

import com.codeager.portal.Role;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;
import org.apache.ibatis.type.TypeHandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Jiupeng Zhang
 * @since 11/26/2017
 */
@MappedTypes(Role.class)
public class RoleTypeHandler implements TypeHandler<Role> {
    @Override
    public void setParameter(PreparedStatement preparedStatement, int i, Role role, JdbcType jdbcType) throws SQLException {
        preparedStatement.setInt(i, role.getCode());
    }

    @Override
    public Role getResult(ResultSet resultSet, String s) throws SQLException {
        return Role.fromCode(resultSet.getInt(s));
    }

    @Override
    public Role getResult(ResultSet resultSet, int i) throws SQLException {
        return Role.fromCode(resultSet.getInt(i));
    }

    @Override
    public Role getResult(CallableStatement callableStatement, int i) throws SQLException {
        return Role.fromCode(callableStatement.getInt(i));
    }
}
