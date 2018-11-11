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

package io.codeager.ecom.controller.api;

import io.codeager.ecom.controller.LimitExceededException;
import io.codeager.ecom.dto.view.$;
import io.codeager.ecom.dto.view.ResponseCode;
import io.codeager.ecom.dto.view.RestResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author Jiupeng Zhang
 * @since 12/16/2017
 */
@RestControllerAdvice(basePackageClasses = LimitExceededExceptionController.class)
@Component("limitExceededExceptionController")
public class LimitExceededExceptionController {
    private static final Logger LOG = LoggerFactory.getLogger(LimitExceededExceptionController.class);

    @ExceptionHandler(LimitExceededException.class)
    @ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
    public ResponseEntity<RestResponse> limitExceededExceptionHandler(LimitExceededException e) {
        try {
            return $.restful()
                    .code(ResponseCode.REQUEST_OUT_OF_LIMIT)
                    .data().put("describe", e.getMessage()).ready()
                    .asResponseEntity();
        } finally {
            LOG.warn("Request limited at {}: {}", e.getMessage(), e.getPath());
        }
    }
}
