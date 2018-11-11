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

package com.codeager.plugin.impl;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.codeager.plugin.ImageUploader;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;

/**
 * @author Jiupeng Zhang
 * @since 01/08/2018
 */
public class PictshareImageUploader implements ImageUploader {
    private static final Logger LOG = LoggerFactory.getLogger(PictshareImageUploader.class);

    private static final String UPLOAD_CODE = "ad03e452af3ulqdv";
    private static final String DELETE_CODE = "utr7c6cdf3rtsa8q";

    @Override
    public String upload(InputStream imageFile) {
        HttpResponse<String> response;
        JsonNode jsonNode;

        try {
            response = Unirest.post("https://static.codeager.io/backend.php")
                    .header("accept", "application/json")
                    .field("postimage", imageFile, "imageFile")
                    .field("upload_code", UPLOAD_CODE)
                    .asString();

            try {
                jsonNode = new JsonNode(response.getBody());
            } catch (JSONException e) {
                LOG.error("PICTSHARE RESPONSE:\n{}", response.getBody());
                throw new PictshareImageUploadException("unexpected response type != json: " + response.getBody());
            }

        } catch (UnirestException e) {
            e.printStackTrace();
            throw new PictshareImageUploadException(e.getMessage());
        }

        if (response != null) {
            JSONObject jsonObject = jsonNode.getObject();
            if ("OK".equals(jsonObject.get("status"))) {
                return jsonObject.getString("url");
            } else {
                System.err.println(jsonObject.toString(4));
                throw new PictshareImageUploadException(
                        "image server returned " + response.getStatus() +
                                ", with status text \"" + jsonObject.getString("status") + "\"");
            }
        } else {
            throw new PictshareImageUploadException("image server responses nothing");
        }
    }

    @Override
    public void delete(String imageName) {
        String ret = null;
        try {
            ret = Unirest.get("https://static.codeager.io/delete_" + DELETE_CODE + "/" + imageName).asString().getBody();
        } catch (UnirestException e) {
            System.err.println("ERROR BODY:\n" + ret);
            e.printStackTrace();
        }
    }

    public static class PictshareImageUploadException extends RuntimeException {
        public PictshareImageUploadException(String message) {
            super(message);
        }
    }
}
