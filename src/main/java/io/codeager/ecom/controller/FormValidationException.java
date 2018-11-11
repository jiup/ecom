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

package com.codeager.ecom.controller;

import org.springframework.validation.BindingResult;

/**
 * @author Jiupeng Zhang
 * @since 12/18/2017
 */
public class FormValidationException extends RuntimeException {
    private BindingResult bindingResult;
    private String bindingMessage;
    private String redirectPath;

    public FormValidationException(BindingResult bindingResult) {
        this(bindingResult, null);
    }

    public FormValidationException(BindingResult bindingResult, String redirectPath) {
        this.redirectPath = redirectPath;
        this.bindingResult = bindingResult;
    }

    public FormValidationException(String bindingMessage, String redirectPath) {
        this.redirectPath = redirectPath;
        this.bindingMessage = bindingMessage;
    }

    public BindingResult getBindingResult() {
        return bindingResult;
    }

    public String getBindingMessage() {
        return bindingMessage;
    }

    public String getRedirectPath() {
        return redirectPath;
    }
}
