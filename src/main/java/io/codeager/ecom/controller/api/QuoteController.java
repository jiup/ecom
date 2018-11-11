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

import com.google.common.util.concurrent.RateLimiter;
import io.codeager.ecom.controller.LimitExceededException;
import io.codeager.ecom.dto.form.QuoteRequestForm;
import io.codeager.ecom.dto.view.$;
import io.codeager.ecom.dto.view.ErrorView;
import io.codeager.ecom.dto.view.ResponseCode;
import io.codeager.ecom.dto.view.RestResponse;
import io.codeager.ecom.service.QuoteMailService;
import io.codeager.ecom.util.BotDetectUtils;
import io.codeager.ecom.util.Constant;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import static io.codeager.ecom.util.Routing.API_BASE;
import static io.codeager.ecom.util.Routing.QUOTE;

/**
 * @author Jiupeng Zhang
 * @since 12/29/2017
 */
@RestController("quoteController")
@RequestMapping(API_BASE)
public class QuoteController {
    private static final RateLimiter CAPTCHA_REQUEST_LIMITER = RateLimiter.create(Constant.CAPTCHA_REQUEST_LIMIT_RATE);
    private static final RateLimiter MAIL_REQUEST_LIMITER = RateLimiter.create(Constant.MAIL_REQUEST_LIMIT_RATE);

    private QuoteMailService quoteMailService;

    @GetMapping(QUOTE)
    public ResponseEntity<RestResponse> quoteRequestView(
            @ApiParam(required = true, allowableValues = "captcha", value = "get needed resources")
            @RequestParam(required = false) String get,
            @ApiParam(value = "value [20, 500]", defaultValue = "100")
            @RequestParam(name = "w", defaultValue = "100") int width,
            @ApiParam(value = "value [10, 190]", defaultValue = "50")
            @RequestParam(name = "h", defaultValue = "50") int height,
            HttpServletRequest request) {

        // undefined request parameter
        if (!"captcha".equals(get)) {
            return $.restful()
                    .code(ResponseCode.NOT_FOUND)
                    .data().putErrors(new ErrorView().setPath(request.getRequestURI())).ready()
                    .asResponseEntity();
        }

        // rate limit controlling
        if (!CAPTCHA_REQUEST_LIMITER.tryAcquire()) {
            throw new LimitExceededException(CAPTCHA_REQUEST_LIMITER, "CAPTCHA_REQUEST_LIMITER", request.getRequestURI());
        }

        return $.restful()
                .code(ResponseCode.OK)
                .data()
                .child("captcha")
                .put("id", QuoteRequestForm.CAPTCHA_ID)
                .put("html", BotDetectUtils.getCustomizedHtml(request, QuoteRequestForm.CAPTCHA_ID, width, height))
                .ready()
                .asResponseEntity();
    }


    @PostMapping(QUOTE)
    public ResponseEntity<RestResponse> requestQuote(HttpServletRequest request, @Valid QuoteRequestForm quoteRequestForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return $.restful()
                    .code(ResponseCode.INVALID_PARAMETER)
                    .data().putFieldErrors(bindingResult.getFieldErrors()).ready()
                    .asResponseEntity();
        }

        // invalid verify code
        if (!BotDetectUtils.isValid(request, QuoteRequestForm.CAPTCHA_ID, quoteRequestForm.getCaptcha())) {
            return $.restful().code(ResponseCode.INVALID_CAPTCHA).asResponseEntity();
        }

        // rate limit controlling
        if (!MAIL_REQUEST_LIMITER.tryAcquire()) {
            throw new LimitExceededException(MAIL_REQUEST_LIMITER, "QUOTE_MAIL_REQUEST_LIMITER", request.getRequestURI());
        }

        quoteMailService.send(quoteRequestForm);
        return $.restful().code(ResponseCode.OK).asResponseEntity();
    }

    @Autowired
    public void setQuoteMailService(QuoteMailService quoteMailService) {
        this.quoteMailService = quoteMailService;
    }
}
