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

package com.codeager.ecom.dto.form;

import com.google.common.base.MoreObjects;
import com.codeager.ecom.util.Pattern;
import io.swagger.annotations.ApiParam;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * @author Jiupeng Zhang
 * @since 12/29/2017
 */
public class QuoteRequestForm {
    public static final String CAPTCHA_ID = "quote_request";

    @NotEmpty
    @Length(max = 32)
    @ApiParam(
            required = true,
            value = "length [0, 32]"
    )
    private String firstName;

    @NotEmpty
    @Length(max = 32)
    @ApiParam(
            required = true,
            value = "length [0, 32]"
    )
    private String lastName;

    @Length(max = 64)
    @ApiParam("length [0, 64]")
    private String companyName;

    @Length(max = 128)
    @ApiParam("length [0, 128]")
    private String companyAddressLineOne;

    @NotEmpty
    @Length(max = 32)
    @ApiParam(
            required = true,
            value = "length [0, 32]"
    )
    private String companyAddressCity;

    @NotEmpty
    @Length(max = 32)
    @ApiParam(
            required = true,
            value = "length [0, 32]"
    )
    private String companyAddressState;

    @NotEmpty
    @Length(max = 32)
    @ApiParam(
            required = true,
            value = "length [0, 32]"
    )
    private String companyAddressZipCode;

    @NotEmpty
    @Length(max = 64)
    @Email(regexp = Pattern.EMAIL)
    @ApiParam(
            required = true,
            value = "length [0, 64]"
    )
    private String email;

    @Length(max = 20)
    @ApiParam("trust any input values")
    private String phoneNumber;

    @ApiParam(
            required = true,
            defaultValue = "false",
            value = "double radio box, like: [x] Phone  [ ] Email, " +
                    "shows after user input a phone number, " +
                    "'email' is default, post true if item-phone is selected"
    )
    private Boolean preferCallOrText;

    @NotEmpty
    @Length(max = 255)
    @ApiParam(
            required = true,
            value = "length [0, 255], shows dropdown list (get from a separate request), " +
                    "if chose 'other', let the user name it :)"
    )
    private String aboutProduct;

    @Length(max = 1024)
    @ApiParam(
            value = "length [0, 1024]"
    )
    private String description;

    @Min(1)
    @Max(1_000_000_000)
    @ApiParam(
            value = "integer required, value [1, 1000000000], prefer auto format user input to ##,###,###"
    )
    private Integer estimateQuantity;

    @NotEmpty
    @ApiParam(
            required = true,
            value = "Field 'BDC_VCID_quote_request" +
                    "' and 'BDC_BackWorkaround_quote_request" +
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

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyAddressLineOne() {
        return companyAddressLineOne;
    }

    public void setCompanyAddressLineOne(String companyAddressLineOne) {
        this.companyAddressLineOne = companyAddressLineOne;
    }

    public String getCompanyAddressCity() {
        return companyAddressCity;
    }

    public void setCompanyAddressCity(String companyAddressCity) {
        this.companyAddressCity = companyAddressCity;
    }

    public String getCompanyAddressState() {
        return companyAddressState;
    }

    public void setCompanyAddressState(String companyAddressState) {
        this.companyAddressState = companyAddressState;
    }

    public String getCompanyAddressZipCode() {
        return companyAddressZipCode;
    }

    public void setCompanyAddressZipCode(String companyAddressZipCode) {
        this.companyAddressZipCode = companyAddressZipCode;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Boolean getPreferCallOrText() {
        return preferCallOrText;
    }

    public void setPreferCallOrText(Boolean preferCallOrText) {
        this.preferCallOrText = preferCallOrText;
    }

    public String getAboutProduct() {
        return aboutProduct;
    }

    public void setAboutProduct(String aboutProduct) {
        this.aboutProduct = aboutProduct;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getEstimateQuantity() {
        return estimateQuantity;
    }

    public void setEstimateQuantity(Integer estimateQuantity) {
        this.estimateQuantity = estimateQuantity;
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
                .add("companyName", companyName)
                .add("companyAddressLineOne", companyAddressLineOne)
                .add("companyAddressCity", companyAddressCity)
                .add("companyAddressState", companyAddressState)
                .add("companyAddressZipCode", companyAddressZipCode)
                .add("email", email)
                .add("phoneNumber", phoneNumber)
                .add("preferCallOrText", preferCallOrText)
                .add("aboutProduct", aboutProduct)
                .add("description", description)
                .add("estimateQuantity", estimateQuantity)
                .toString();
    }
}
