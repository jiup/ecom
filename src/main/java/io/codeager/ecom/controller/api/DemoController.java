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

package com.codeager.ecom.controller.api;

import com.captcha.botdetect.web.servlet.Captcha;
import com.google.common.util.concurrent.RateLimiter;
import com.codeager.ecom.controller.LimitExceededException;
import com.codeager.ecom.dto.form.ContactForm;
import com.codeager.ecom.dto.form.QuoteRequestForm;
import com.codeager.ecom.dto.view.$;
import com.codeager.ecom.dto.view.ResponseCode;
import com.codeager.ecom.dto.view.RestResponse;
import com.codeager.ecom.util.BotDetectUtils;
import com.codeager.ecom.util.Constant;
import com.codeager.plugin.MailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import springfox.documentation.annotations.ApiIgnore;

import javax.mail.SendFailedException;
import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;

import static com.codeager.ecom.util.Routing.API_BASE;

/**
 * @author Jiupeng Zhang
 * @since 12/20/2017
 */
@Controller
@RequestMapping(API_BASE)
public class DemoController {
    // time-sync is important for version check
    private static final boolean VERSION_CHECK = false;
    private static final int VERSION_TOLERANCE_IN_SECONDS = 10;

    private static final RateLimiter CAPTCHA_REQUEST_LIMITER =
            RateLimiter.create(Constant.CAPTCHA_REQUEST_LIMIT_RATE);

    @GetMapping("/demo/contact")
    public String contactFormDemo(/*@RequestParam(value = "v", required = false) String version, */Model model, HttpServletRequest request) {
        if (!CAPTCHA_REQUEST_LIMITER.tryAcquire(500, TimeUnit.MILLISECONDS)) {
            throw new LimitExceededException(CAPTCHA_REQUEST_LIMITER);
        }

//        if (StringUtils.isEmpty(version) || version.length() != 10) {
//            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//            return null;
//        }
//        if (VERSION_CHECK) {
//            String accept = String.valueOf(System.currentTimeMillis() / 1000 - VERSION_TOLERANCE_IN_SECONDS);
//
//            if (StringUtils.compare(version, accept) < 0) {
//                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
//                return null;
//            }
//        }
        Captcha captcha = BotDetectUtils.load(request, ContactForm.CAPTCHA_ID);
        model.addAttribute("captchaHtml", BotDetectUtils.getCustomizedHtml(captcha));
        return "contact_demo";
    }

    @GetMapping("/demo/quote")
    public String quoteRequestFormDemo(Model model, HttpServletRequest request) {
        if (!CAPTCHA_REQUEST_LIMITER.tryAcquire(500, TimeUnit.MILLISECONDS)) {
            throw new LimitExceededException(CAPTCHA_REQUEST_LIMITER);
        }

        Captcha captcha = BotDetectUtils.load(request, QuoteRequestForm.CAPTCHA_ID);
        model.addAttribute("captchaHtml", BotDetectUtils.getCustomizedHtml(captcha));
        return "quote_demo";
    }

    @Autowired
    private MailSender mailSender;
    @Autowired
    private TemplateEngine templateEngine;

    @GetMapping("demo/ad_email")
    @ApiIgnore
    public ResponseEntity<RestResponse> sendAdEmailDemo() {
        try {
            mailSender.sendHtml("1519712304@qq.com", "Test AD Mails", templateEngine.process("templates/ad_01", new Context()));
        } catch (SendFailedException e) {
            e.printStackTrace();
        }
        return $.restful().code(ResponseCode.OK).asResponseEntity();
    }
}
