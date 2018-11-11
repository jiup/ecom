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

package com.codeager.ecom.service.impl;

import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.codeager.plugin.MailSender;
import com.codeager.plugin.Plugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.concurrent.Executors;

/**
 * @author Jiupeng Zhang
 * @since 12/14/2017
 */
public abstract class BaseMailService extends Plugin<MailSender> {
    protected static final Logger LOG = LoggerFactory.getLogger(BaseMailService.class);
    protected static final ListeningExecutorService MAIL_SERVICE_EXECUTOR;

    static {
        MAIL_SERVICE_EXECUTOR = MoreExecutors.listeningDecorator(Executors.newCachedThreadPool());
    }

    protected BaseMailService(Optional<MailSender> optionalFutureHolder) {
        super(optionalFutureHolder);
    }
}
