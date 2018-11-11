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

package com.codeager.plugin.aspect;

import com.codeager.plugin.Plugin;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * @author Jiupeng Zhang
 * @since 12/14/2017
 */
@Aspect
@Component
public class PluginWeaver {
    @Pointcut("target(com.codeager.plugin.Plugin)")
    public void pluginExecution() {
    }

    @Around("pluginExecution()")
    public Object aroundPluginExecution(ProceedingJoinPoint joinPoint) throws Throwable {
        Plugin plugin = (Plugin) joinPoint.getTarget();
        if (plugin.isConnected()) {
            return joinPoint.proceed();
        }
        return null;
    }
}