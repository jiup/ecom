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

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import springfox.documentation.service.ResourceListing;

import java.lang.reflect.Type;

/**
 * @author Indra Basak
 * @since 11/23/2017
 */
public class SpringfoxResourceListingJsonAdaptor
        implements JsonSerializer<ResourceListing> {

    @Override
    public JsonElement serialize(ResourceListing resourceListing, Type type, JsonSerializationContext context) {
        final JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("apiVersion", resourceListing.getApiVersion());

        final JsonElement apis = context.serialize(resourceListing.getApis());
        jsonObject.add("apis", apis);

        final JsonElement securityScheme =
                context.serialize(resourceListing.getSecuritySchemes());
        jsonObject.add("securityScheme", securityScheme);

        final JsonElement info = context.serialize(resourceListing.getInfo());
        jsonObject.add("info", info);

        return jsonObject;
    }
}