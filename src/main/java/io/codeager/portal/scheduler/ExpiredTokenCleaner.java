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

package com.codeager.portal.scheduler;

import com.codeager.portal.service.UserTokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * @author Jiupeng Zhang
 * @since 11/27/2017
 */
@Component("userAccessCleaner")
public class ExpiredTokenCleaner {
    private static final Logger LOG = LoggerFactory.getLogger(ExpiredTokenCleaner.class);

    private UserTokenService userTokenService;

    @Autowired
    public void setUserTokenService(UserTokenService userTokenService) {
        this.userTokenService = userTokenService;
    }

    @PostConstruct
    public void cleanExpireTokensWhenStartup() {
        cleanExpireTokens();
    }

    @PreDestroy
    public void cleanExpireTokensWhenDestroy() {
        cleanExpireTokens();
    }

    @Scheduled(cron = "${scheduler.cleaner.userAccess}")
    public void cleanExpireTokens() {
        LOG.info("Auto-cleaned {} expired token(s)", userTokenService.deleteExpiredToken());
    }
}