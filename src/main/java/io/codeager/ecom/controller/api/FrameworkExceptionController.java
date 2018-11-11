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

import io.codeager.ecom.dto.view.$;
import io.codeager.ecom.dto.view.ResponseCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * @author Jiupeng Zhang
 * @since 12/22/2017
 */
@RestControllerAdvice(basePackageClasses = FrameworkExceptionController.class)
@Component("restfulBasicExceptionController")
public class FrameworkExceptionController extends ResponseEntityExceptionHandler {
    private static final Logger LOG = LoggerFactory.getLogger(FrameworkExceptionController.class);

//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
//        e.printStackTrace();
//        LOG.warn("{}", e.getMessage());
//        return $.restful().code(ResponseCode.BAD_REQUEST).asResponseEntity();
//    }
//
//    @ExceptionHandler(NoHandlerFoundException.class)
//    public ResponseEntity runtimeExceptionHandler(RuntimeException e) {
//        e.printStackTrace();
//        LOG.warn("{}", e.getMessage());
//        return $.restful().code(ResponseCode.NOT_FOUND).asResponseEntity();
//    }
//
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity exceptionHandler(Exception e) {
//        e.printStackTrace();
//        LOG.warn("{}", e.getMessage());
//        return $.restful().code(ResponseCode.INTERNAL_SERVER_ERROR).asResponseEntity();
//    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        LOG.error("caught spring internal: {}", ex);
//        return super.handleExceptionInternal(ex, body, headers, status, request); // 400-null returned
        return ResponseEntity.status(status)
                .body($.restful()
                        .from(status)
                        .code(ResponseCode.INTERNAL_SERVER_ERROR.getCode())
                        .message("dev: spring internal exception")
                        .data().put("调试信息", "看到这个错误请找我，谢谢")
                        .put("error", ex).ready()
                        .asRestResponse());
    }
}
