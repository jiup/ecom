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

/**
 * @author Jiupeng Zhang
 * @since 12/25/2017
 */

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import springfox.documentation.service.ApiListing;

import java.lang.reflect.Type;

/**
 * @author Indra Basak
 * @since 11/23/2017
 */
public class SpringfoxApiListingJsonAdaptor
        implements JsonSerializer<ApiListing> {

    @Override
    public JsonElement serialize(ApiListing apiListing, Type type, JsonSerializationContext context) {
        final JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("apiVersion", apiListing.getApiVersion());
        jsonObject.addProperty("basePath", apiListing.getBasePath());
        jsonObject.addProperty("resourcePath", apiListing.getResourcePath());

        final JsonElement produces = context.serialize(apiListing.getProduces());
        jsonObject.add("produces", produces);

        final JsonElement consumes = context.serialize(apiListing.getConsumes());
        jsonObject.add("consumes", consumes);

        jsonObject.addProperty("host", apiListing.getHost());

        final JsonElement protocols = context.serialize(apiListing.getProtocols());
        jsonObject.add("protocols", protocols);

        final JsonElement securityReferences = context.serialize(apiListing.getSecurityReferences());
        jsonObject.add("securityReferences", securityReferences);

        final JsonElement apis = context.serialize(apiListing.getApis());
        jsonObject.add("apis", apis);

        final JsonElement models = context.serialize(apiListing.getModels());
        jsonObject.add("models", models);

        jsonObject.addProperty("description", apiListing.getDescription());
        jsonObject.addProperty("position", apiListing.getPosition());

        final JsonElement tags = context.serialize(apiListing.getTags());
        jsonObject.add("tags", tags);

        return jsonObject;
    }
}