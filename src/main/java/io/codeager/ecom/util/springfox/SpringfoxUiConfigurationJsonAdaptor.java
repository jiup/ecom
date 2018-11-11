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

package io.codeager.ecom.util.springfox;

import com.google.gson.*;
import springfox.documentation.swagger.web.UiConfiguration;

import java.lang.reflect.Type;

/**
 * @author Indra Basak
 * @since 11/23/2017
 */
public class SpringfoxUiConfigurationJsonAdaptor
        implements JsonSerializer<UiConfiguration> {

    @Override
    public JsonElement serialize(UiConfiguration config, Type type, JsonSerializationContext jsonSerializationContext) {
        final JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("validatorUrl", config.getValidatorUrl());
        jsonObject.addProperty("docExpansion", config.getDocExpansion());
        jsonObject.addProperty("apisSorter", config.getApisSorter());
        jsonObject.addProperty("defaultModelRendering",
                config.getDefaultModelRendering());

        JsonArray array = new JsonArray();
        for (String value : config.getSupportedSubmitMethods()) {
            array.add(new JsonPrimitive(value));
        }

        jsonObject.add("supportedSubmitMethods", array);
        jsonObject.addProperty("jsonEditor", config.isJsonEditor());
        jsonObject.addProperty("showRequestHeaders", config.isShowRequestHeaders());
        jsonObject.addProperty("requestTimeout", config.getRequestTimeout());

        return jsonObject;
    }
}