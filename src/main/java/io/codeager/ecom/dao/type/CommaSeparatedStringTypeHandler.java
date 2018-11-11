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

package io.codeager.ecom.dao.type;

import com.google.common.base.Joiner;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;
import org.apache.ibatis.type.TypeHandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Jiupeng Zhang
 * @since 02/25/2018
 */
@MappedTypes(String[].class)
public class CommaSeparatedStringTypeHandler implements TypeHandler<String[]> {
    private static final String SEPARATOR = ",";

    @Override
    public void setParameter(PreparedStatement preparedStatement, int i, String[] strings, JdbcType jdbcType) throws SQLException {
        if (strings != null) {
            preparedStatement.setString(i, Joiner.on(SEPARATOR).skipNulls().join(strings));
        }
    }

    @Override
    public String[] getResult(ResultSet resultSet, String s) throws SQLException {
        String result = resultSet.getString(s);
        return result != null ? result.split(SEPARATOR) : null;
    }

    @Override
    public String[] getResult(ResultSet resultSet, int i) throws SQLException {
        String result = resultSet.getString(i);
        return result != null ? result.split(SEPARATOR) : null;
    }

    @Override
    public String[] getResult(CallableStatement callableStatement, int i) throws SQLException {
        String result = callableStatement.getString(i);
        return result != null ? result.split(SEPARATOR) : null;
    }
}
