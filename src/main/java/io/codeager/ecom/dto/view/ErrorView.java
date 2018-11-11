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

package io.codeager.ecom.dto.view;

import com.google.common.base.MoreObjects;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author Jiupeng Zhang
 * @since 12/22/2017
 */
public class ErrorView {
    @Expose
    private String name;
    @Expose
    private String type = "client_error";
    @Expose
    private String path;
    @Expose
    @SerializedName("cause_by")
    private String causeBy;
    @Expose
    private String field;
    @Expose
    private String advice;
    @Expose
    private List<ErrorView> sub;
    private String describe;

    public String getName() {
        return name;
    }

    public ErrorView setName(String name) {
        this.name = name;
        return this;
    }

    public String getType() {
        return type;
    }

    public ErrorView setType(String type) {
        this.type = type;
        return this;
    }

    public String getPath() {
        return path;
    }

    public ErrorView setPath(String path) {
        this.path = path;
        return this;
    }

    public String getDescribe() {
        return describe;
    }

    public ErrorView setDescribe(String describe) {
        this.describe = describe;
        return this;
    }

    public String getCauseBy() {
        return causeBy;
    }

    public ErrorView setCauseBy(String causeBy) {
        this.causeBy = causeBy;
        return this;
    }

    public String getField() {
        return field;
    }

    public ErrorView setField(String field) {
        this.field = field;
        return this;
    }

    public String getAdvice() {
        return advice;
    }

    public ErrorView setAdvice(String advice) {
        this.advice = advice;
        return this;
    }

    public List<ErrorView> getSub() {
        return sub;
    }

    public ErrorView setSub(List<ErrorView> sub) {
        this.sub = sub;
        return this;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .omitNullValues()
                .add("name", name)
                .add("type", type)
                .add("path", path)
                .add("describe", describe)
                .add("causeBy", causeBy)
                .add("field", field)
                .add("advice", advice)
                .add("sub", sub)
                .toString();
    }
}
