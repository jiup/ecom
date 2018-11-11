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

package io.codeager.ecom.dto.form;

import com.google.common.base.MoreObjects;
import io.codeager.ecom.util.Pattern;
import io.swagger.annotations.ApiParam;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import java.io.Serializable;

/**
 * @author Jiupeng Zhang
 * @since 12/18/2017
 */
public class ContactForm implements Serializable {
    public static final String CAPTCHA_ID = "contact";

    @NotEmpty
    @Length(max = 32)
    @ApiParam(
            required = true,
            value = "length [0, 32]"
    )
    private String firstName;

    @Length(max = 32)
    @ApiParam("length [0, 32]")
    private String lastName;

    @NotEmpty
    @Length(max = 64)
    @Email(regexp = Pattern.EMAIL)
    @ApiParam(
            required = true,
            value = "length [0, 64]"
    )
    private String email;

    @Length(max = 255)
    @ApiParam(
            hidden = true,
            value = "length [0, 255]"
    )
    private String subject;

    @NotEmpty
    @Length(max = 1024)
    @ApiParam(
            required = true,
            value = "length [0, 1024]"
    )
    private String message;

    @NotEmpty
    @ApiParam(
            required = true,
            value = "Field 'BDC_VCID_contact" +
                    "' and 'BDC_BackWorkaround_contact" +
                    "' are also required in request"
    )
    private String captcha;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .omitNullValues()
                .add("firstName", firstName)
                .add("lastName", lastName)
                .add("email", email)
                .add("subject", subject)
                .add("message", message)
                .add("captcha", captcha)
                .toString();
    }
}
