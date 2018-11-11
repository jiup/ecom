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

package com.codeager.plugin.impl;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.codeager.plugin.MailSender;

import javax.mail.SendFailedException;

/**
 * @author Jiupeng Zhang
 * @since 12/12/2017
 */
public class MailgunMailSender implements MailSender {
    private String url;
    private String key;
    private String from;

    public void setUrl(String url) {
        this.url = url;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    @Override
    public void sendText(String to, String subject, String text) throws SendFailedException {
        this.sendText(from, to, subject, text);
    }

    @Override
    public void sendHtml(String to, String subject, String html) throws SendFailedException {
        this.sendHtml(from, to, subject, html);
    }

    public void sendText(String from, String to, String subject, String text) throws SendFailedException {
        HttpResponse<JsonNode> request;
        try {
            request = Unirest.post(url)
                    .basicAuth("api", key)
                    .queryString("from", from)
                    .queryString("to", to)
                    .queryString("subject", subject)
                    .queryString("text", text)
                    .asJson();
        } catch (UnirestException e) {
            throw new SendFailedException(e.getMessage());
        }
        if (request.getStatus() != 200) {
            throw new SendFailedException(request.getBody().toString());
        }
    }

    public void sendHtml(String from, String to, String subject, String html) throws SendFailedException {
        HttpResponse<JsonNode> request;
        try {
            request = Unirest.post(url)
                    .basicAuth("api", key)
                    .queryString("from", from)
                    .queryString("to", to)
                    .queryString("subject", subject)
                    .queryString("html", html)
                    .asJson();
        } catch (UnirestException e) {
            throw new SendFailedException(e.getMessage());
        }
        if (request.getStatus() != 200) {
            throw new SendFailedException(request.getBody().toString());
        }
    }
}
