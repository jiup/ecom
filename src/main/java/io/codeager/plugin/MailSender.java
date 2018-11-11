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

package io.codeager.plugin;

import javax.mail.SendFailedException;

/**
 * @author Jiupeng Zhang
 * @since 12/12/2017
 */
public interface MailSender {
    void sendText(String to, String subject, String text) throws SendFailedException;

    void sendHtml(String to, String subject, String html) throws SendFailedException;
}
