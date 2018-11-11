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

package com.codeager.ecom.dto.view;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.annotations.Expose;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.reflect.TypeUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;

import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.util.*;

/**
 * @author Jiupeng Zhang
 * @since 12/21/2017
 */
public class RestResponse implements Serializable {
    @Expose
    private int status;
    @Expose
    private int code;
    @Expose
    private String message;
    @Expose
    private Map<String, Object> data;

    private RestResponse(int status, int code, String message, Map<String, Object> data) {
        this.status = status;
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public static class Builder {
        static final Map<String, Object> EMPTY_DATA = ImmutableMap.of();

        private int status;
        private int code;
        private String message;
        private Map<String, Object> data;

        private Builder() {
        }

        static Builder newInstance() {
            return new Builder();
        }

        public Builder from(Map<String, Object> data) {
            this.data = ObjectUtils.cloneIfPossible(data);
            return this;
        }

        public Builder from(HttpStatus httpStatus) {
            this.status = httpStatus.value();
            this.message = httpStatus.getReasonPhrase();
            return this;
        }

        public Builder from(RestResponse restResponse) {
            this.status = restResponse.status;
            this.code = restResponse.code;
            this.message = restResponse.message;
            this.data = restResponse.data;
            return this;
        }

        public Builder from(HttpServletResponse response) {
            this.status = response.getStatus();
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder status(int status) {
            this.status = status;
            return this;
        }

        public Builder status(HttpStatus httpStatus) {
            this.status = httpStatus.value();
            return this;
        }

        public Builder code(int code) {
            this.code = code;
            return this;
        }

        public Builder code(ResponseCode responseCode) {
            this.status = responseCode.getStatus().value();
            this.code = responseCode.getCode();
            this.message = responseCode.getMessage();
            return this;
        }

        public DataVisitor data() {
            data = ObjectUtils.defaultIfNull(data, Maps.newHashMap());
            return new DataVisitor(this, null, data);
        }

        public ResponseEntity<RestResponse> asResponseEntity() {
            setDefaultValues();
            return new ResponseEntity<>(
                    new RestResponse(status, code, message, data),
                    HttpStatus.valueOf(status)
            );
        }

        public RestResponse asRestResponse() {
            setDefaultValues();
            return new RestResponse(status, code, message, data);
        }

        private void setDefaultValues() {
            status = status != 0 ? status : HttpStatus.OK.value();
            code = code != 0 ? code : ResponseCode.OK.getCode();
            message = Strings.nullToEmpty(message);
            data = ObjectUtils.defaultIfNull(data, EMPTY_DATA);
        }
    }

    public static class DataVisitor {
        private Builder builder;
        private DataVisitor parent;
        private Map<String, Object> data;

        private DataVisitor() {
        }

        private DataVisitor(Builder builder, DataVisitor parent, Map<String, Object> data) {
            this();
            this.builder = builder;
            this.parent = parent;
            this.data = data;
        }

        public DataVisitor put(String key, Object value) {
            if (value instanceof Collection) {
                data.put(key, ((Collection) value).toArray());
            } else {
                data.put(key, value);
            }
            return this;
        }

        public DataVisitor putIfAbsent(String key, Object value) {
            data.putIfAbsent(key, value);
            return this;
        }

        public DataVisitor putErrors(List<ErrorView> errors) {
            Object obj = data.get("errors");
            if (obj != null && obj instanceof List) {
                @SuppressWarnings("unchecked")
                List<ErrorView> list = (List<ErrorView>) obj;
                list.addAll(errors);
            } else {
                data.put("errors", errors);
            }
            return this;
        }

        public DataVisitor putFieldErrors(List<FieldError> fieldErrors) {
            List<ErrorView> errorViews = Lists.newArrayList();
            for (FieldError fieldError : fieldErrors) {
                ErrorView errorView = new ErrorView();
                errorView.setType("field_error");
                errorView.setField(fieldError.getField());
                errorView.setDescribe(fieldError.getDefaultMessage());
                errorViews.add(errorView);
            }
            return putErrors(errorViews);
        }

        public DataVisitor putErrors(ErrorView... errors) {
            return putErrors(Arrays.asList(errors));
        }

        public DataVisitor remove(String key) {
            data.remove(key);
            return this;
        }

        public DataVisitor clear() {
            data.clear();
            return this;
        }

        public DataVisitor parent() {
            if (parent == null) {
                throw new RuntimeException("unexpected parent access: root");
            }
            return parent;
        }

        public DataVisitor child(String key) {
            return child(key, false);
        }

        public DataVisitor child(String key, boolean overwrite) {
            if (!overwrite && data.containsKey(key)) {
                Object obj = data.get(key);
                if (TypeUtils.isInstance(obj, Map.class)) {
                    @SuppressWarnings("unchecked")
                    Map<String, Object> castIntermediate = (Map) Collections.checkedMap((Map) obj, Object.class, Object.class);
                    return new DataVisitor(builder, this, castIntermediate);
                } else {
                    throw new RuntimeException("unexpected child access: duplicate key with non-map object");
                }
            }
            Map<String, Object> subData = Maps.newHashMap();
            data.put(key, subData);
            return new DataVisitor(builder, this, subData);
        }

        public Builder ready() {
            return builder;
        }
    }
}
