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

package io.codeager.portal.util;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONObject;

/**
 * @author Jiupeng Zhang
 * @since 03/11/2018
 */
public class IdentityLocator {
    public static String getCityByIp(String ipAddress) {
        try {
            HttpResponse<JsonNode> resp = Unirest.get("http://ip-api.com/json/".concat(ipAddress)).asJson();
            JSONObject jsonObject = resp.getBody().getObject();
            if ("success".equals(jsonObject.get("status"))) {
                return jsonObject.getString("city");
            }
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getBrowserNameByUserAgent(String userAgent) {
        try {
            HttpResponse<JsonNode> resp = Unirest.get("http://www.useragentstring.com/")
                    .queryString("uas", userAgent)
                    .queryString("getJSON", "all")
                    .asJson();
            JSONObject jsonObject = resp.getBody().getObject();
            return jsonObject.getString("agent_name");
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        return null;
    }
}
