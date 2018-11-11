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

package com.codeager.ecom.service;

import com.github.pagehelper.Page;
import com.codeager.ecom.domain.AutoMail;
import com.codeager.ecom.domain.MailTemplate;
import com.codeager.ecom.dto.form.MailTemplateForm;

/**
 * @author Jiupeng Zhang
 * @since 01/08/2018
 */
public interface AutoMailService {
    String loadParsedHtml();
    MailTemplate loadTemplate();
    void save(MailTemplateForm form, boolean overwriteImage, String imageUrl);
    void delete(MailTemplateForm form);
    void send(long templateId, String group, long[] receiverIds);
    void send(AutoMail mail);
    void resend(AutoMail mail);
    void delete(AutoMail mail);
    void unsubscribeByToken(String token);
    Page<AutoMail> getAll(int pageNum, int pageSize);
}
