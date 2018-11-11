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

import org.springframework.http.HttpStatus;

/**
 * @author Jiupeng Zhang
 * @since 12/21/2017
 */
public enum ResponseCode {
    OK(HttpStatus.OK, 0, "ok"),
    BAD_REQUEST(HttpStatus.BAD_REQUEST, 14000, "Bad Request"),
    INVALID_ACCESS_TOKEN(HttpStatus.BAD_REQUEST, 14001, "invalid access_token"),
    INVALID_PARAMETER(HttpStatus.BAD_REQUEST, 14002, "invalid parameter"),
    API_UNAUTHORIZED(HttpStatus.UNAUTHORIZED, 14010, "Unauthorized"),
    FORBIDDEN(HttpStatus.FORBIDDEN, 14030, "Access Denied"),
    INVALID_CAPTCHA(HttpStatus.FORBIDDEN, 14031, "invalid verify_code"),
    NOT_FOUND(HttpStatus.NOT_FOUND, 14040, "Not Found"),
    TOKEN_EXPIRED(HttpStatus.GONE, 14100, "token expired"),
    ACCESS_TOKEN_EXPIRED(HttpStatus.GONE, 14101, "access_token expired"),
    REQUEST_OUT_OF_LIMIT(HttpStatus.GONE, 14290, "request out of limit"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 15000, "Internal Server Error"),
    NOT_IMPLEMENTED(HttpStatus.NOT_IMPLEMENTED, 15010, "Not Implemented");

    private final HttpStatus status;
    private final int code;
    private final String message;

    ResponseCode(HttpStatus status, int code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
