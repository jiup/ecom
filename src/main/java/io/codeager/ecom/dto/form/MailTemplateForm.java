/*
 * Copyright 2018 Jiupeng Zhang
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
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

/**
 * @author Jiupeng Zhang
 * @since 01/07/2018
 */
public class MailTemplateForm implements Serializable {

    @NotEmpty
    @Length(max = 255)
    private String subject;

    private MultipartFile imageFile;

    @NotEmpty
    @Length(max = 255)
    private String title;

    @NotEmpty
    @Length(max = 1024)
    private String content;

    private boolean hideUnsubscribeButton;

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public MultipartFile getImageFile() {
        return imageFile;
    }

    public void setImageFile(MultipartFile imageFile) {
        this.imageFile = imageFile;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isHideUnsubscribeButton() {
        return hideUnsubscribeButton;
    }

    public void setHideUnsubscribeButton(boolean hideUnsubscribeButton) {
        this.hideUnsubscribeButton = hideUnsubscribeButton;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("subject", subject)
                .add("imageFile", imageFile)
                .add("title", title)
                .add("content", content)
                .add("hideUnsubscribeButton", hideUnsubscribeButton)
                .toString();
    }
}
