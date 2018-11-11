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
import io.codeager.ecom.dto.form.MemberRegisterForm;
import io.codeager.ecom.dto.view.$;
import io.codeager.ecom.dto.view.ResponseCode;
import io.codeager.ecom.dto.view.RestResponse;
import io.codeager.ecom.util.Pattern;
import io.codeager.portal.Role;
import io.codeager.portal.annotation.Authenticate;
import io.codeager.portal.util.Constant;
import io.swagger.annotations.ApiParam;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import static io.codeager.ecom.util.Routing.*;

/**
 * @author Jiupeng Zhang
 * @since 12/11/2017
 */
@RestController("memberController")
@RequestMapping(API_BASE)
public class MemberController {
    private static final RateLimiter FORMAT_CHECK_REQUEST_LIMITER;

    static {
        FORMAT_CHECK_REQUEST_LIMITER = RateLimiter.create(io.codeager.ecom.util.Constant.FORMAT_CHECK_REQUEST_LIMIT_RATE);
    }

    @PutMapping(MEMBER_LOGIN)
    public ResponseEntity<RestResponse> login(
            HttpServletRequest request,
            @ApiParam("post-submit if required")
            @RequestParam(value = "captcha", required = false) String captcha,
            @ApiParam(required = true, value = "length [0, 32]")
            @PathVariable("username") String username,
            @ApiParam(required = true, value = "length [8, 64]")
            @RequestParam("password") String password) {

        if (!username.matches(Pattern.USERNAME) || !password.matches(Pattern.PASSWORD))
            return $.restful().code(ResponseCode.FORBIDDEN).asResponseEntity();

        // todo: decide if captcha verify needed based on user_access history (record in DB)
        // todo: update user rights to membership if valid
        return $.restful().code(ResponseCode.NOT_IMPLEMENTED).asResponseEntity();
    }

    @DeleteMapping(MEMBER_LOGOUT)
    @Authenticate(Role.MEMBER)
    public ResponseEntity<RestResponse> logout(
            @ApiParam(hidden = true)
            @CookieValue(value = Constant.AUTH_COOKIE_NAME, required = false) String accessToken,
            @ApiParam(required = true, value = "length [0, 32]")
            @PathVariable("username") String username) {

        // todo: validate session and then logout
        return $.restful().code(ResponseCode.NOT_IMPLEMENTED).asResponseEntity();
    }

    @PostMapping(MEMBER_SIGN_UP)
    public ResponseEntity<RestResponse> register(
            HttpServletRequest request,
            @ApiParam("post-submit if required")
            @RequestParam(value = "captcha", required = false) String captcha,
            @ApiParam(hidden = true)
            @CookieValue(value = Constant.AUTH_COOKIE_NAME, required = false) String accessToken,
            @Valid MemberRegisterForm registerForm,
            BindingResult bindingResult) {

        if (bindingResult.hasFieldErrors()) {
            return $.restful()
                    .code(ResponseCode.INVALID_PARAMETER)
                    .data().putFieldErrors(bindingResult.getFieldErrors()).ready()
                    .asResponseEntity();
        }

        // todo: design table 'membership' for registration
        return $.restful().code(ResponseCode.NOT_IMPLEMENTED).asResponseEntity();
    }

    @PostMapping(SIGN_UP_CHECK_USERNAME)
    public ResponseEntity<RestResponse> checkUsername(@RequestParam(value = "value", required = false) String value) {
        checkRequestLimit(SIGN_UP_CHECK_USERNAME);

        if (value == null || value.length() == 0)
            return $.restful().code(ResponseCode.FORBIDDEN)
                    .message("Username is required").asResponseEntity();

        if (value.length() > 32)
            return $.restful().code(ResponseCode.FORBIDDEN).code(14031)
                    .message("Username is too long (maximum is 32 characters)").asResponseEntity();

        if (!value.matches(Pattern.USERNAME))
            return $.restful().code(ResponseCode.FORBIDDEN).code(14032)
                    .message("Username may only contain alphanumeric characters or single hyphens, " +
                            "and cannot begin or end with a hyphen").asResponseEntity();

        if (value.length() < 5)
            return $.restful().code(ResponseCode.INVALID_PARAMETER)
                    .message("Username is already taken").asResponseEntity();

        // todo: check validity of a username and if the user exists (case insensitive)
        return $.restful().code(ResponseCode.OK).asResponseEntity();
    }

    @PostMapping(SIGN_UP_CHECK_PASSWORD)
    public ResponseEntity<RestResponse> checkPassword(
            @RequestParam(value = "value", required = false) String value,
            @RequestParam(defaultValue = "true") boolean detailed) {

        checkRequestLimit(SIGN_UP_CHECK_PASSWORD);

        if (detailed) {
            if (value == null || value.length() == 0)
                return $.restful().code(ResponseCode.FORBIDDEN)
                        .message("Password is required").asResponseEntity();

            if (value.length() < 8)
                return $.restful().code(ResponseCode.FORBIDDEN).code(14031)
                        .message("Password needs at least 8 characters").asResponseEntity();

            if (value.length() > 64)
                return $.restful().code(ResponseCode.FORBIDDEN).code(14031)
                        .message("Password is too long (maximum is 64 characters)").asResponseEntity();

            if (!Pattern.AT_LEAST_ONE_DIGIT.matcher(value).find())
                return $.restful().code(ResponseCode.FORBIDDEN).code(14033)
                        .message("Password should contains at least one digit").asResponseEntity();

            if (!Pattern.AT_LEAST_ONE_LOWERCASE_ALPHA.matcher(value).find() || !Pattern.AT_LEAST_ONE_UPPERCASE_ALPHA.matcher(value).find())
                return $.restful().code(ResponseCode.FORBIDDEN).code(14034)
                        .message("Password should contains at least one lower alpha char and one upper alpha char").asResponseEntity();

            if (!Pattern.AT_LEAST_ONE_SPECIAL_CHAR.matcher(value).find())
                return $.restful().code(ResponseCode.FORBIDDEN).code(14035)
                        .message("Password requires at least one special char").asResponseEntity();

            if (!Pattern.NO_SPACE.matcher(value).find())
                return $.restful().code(ResponseCode.FORBIDDEN).code(14036)
                        .message("Password should not contains any spaces").asResponseEntity();
        }

        if (!value.matches(Pattern.PASSWORD))
            return $.restful().code(ResponseCode.FORBIDDEN)
                    .message("Password is invalid").asResponseEntity();

        // todo: check validity of a password
        return $.restful().code(ResponseCode.OK).asResponseEntity();
    }

    @PostMapping(SIGN_UP_CHECK_EMAIL)
    public ResponseEntity<RestResponse> checkEmail(@RequestParam(value = "value", required = false) String value) {
        checkRequestLimit(SIGN_UP_CHECK_EMAIL);

        if (value == null || value.length() == 0)
            return $.restful().code(ResponseCode.FORBIDDEN)
                    .message("Email is required").asResponseEntity();

        if (value.length() > 64)
            return $.restful().code(ResponseCode.FORBIDDEN).code(14031)
                    .message("Email is too long (maximum is 64 characters)").asResponseEntity();

        if (!value.matches(Pattern.EMAIL) || false)
            return $.restful().code(ResponseCode.FORBIDDEN)
                    .message("Email is invalid or already taken").asResponseEntity();

        // todo: check validity of a email and if this email occupied
        return $.restful().code(ResponseCode.OK).asResponseEntity();
    }

    private void checkRequestLimit(String path) {
        if (!FORMAT_CHECK_REQUEST_LIMITER.tryAcquire()) {
            throw new LimitExceededException(FORMAT_CHECK_REQUEST_LIMITER, "FORMAT_CHECK_REQUEST_LIMITER", path);
        }
    }

}
