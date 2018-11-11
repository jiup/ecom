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

import com.google.common.util.concurrent.RateLimiter;

/**
 * @author Jiupeng Zhang
 * @since 12/16/2017
 */
public class LimitExceededException extends RuntimeException {
    private RateLimiter limiter;
    private String path = "/?";

    public LimitExceededException(RateLimiter limiter) {
        this(limiter, "QPS > " + limiter.getRate());
    }

    public LimitExceededException(RateLimiter limiter, String identifier) {
        super(identifier);
        this.limiter = limiter;
    }

    public LimitExceededException(RateLimiter limiter, String identifier, String path) {
        this(limiter, identifier);
        this.path = path;
    }

    public RateLimiter getLimiter() {
        return limiter;
    }

    public String getPath() {
        return path;
    }
}
