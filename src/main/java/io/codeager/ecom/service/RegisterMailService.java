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

package com.codeager.ecom.service;

import javax.mail.SendFailedException;

/**
 * @author Jiupeng Zhang
 * @since 12/15/2017
 */
public interface RegisterMailService {
    void send(String toAddress) throws SendFailedException;
    void sendNewAdminAccountInform(String toAddress, String displayName, String loginName, String initPassword) throws SendFailedException;
}
