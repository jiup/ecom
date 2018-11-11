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

package io.codeager.ecom.util;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;
import ch.qos.logback.core.util.StatusPrinter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author Jiupeng Zhang
 * @since 12/24/2017
 */
public class LogbackUtils {
    private static final String LOGBACK_CONFIG_PATH = "conf/logback.xml";
    private static final LoggerContext CONTEXT = (LoggerContext) LoggerFactory.getILoggerFactory();
    private static final Logger LOG = LoggerFactory.getLogger(LogbackUtils.class);

    static {
        JoranConfigurator configurator = new JoranConfigurator();
        InputStream logbackConfig = null;
        try {
            logbackConfig = LogbackUtils.class.getClassLoader().getResourceAsStream(LOGBACK_CONFIG_PATH);
            if (logbackConfig == null) {
                LOG.info("Logback load failure, fallback to default");
            } else {
                CONTEXT.reset();
                configurator.setContext(CONTEXT);
                configurator.doConfigure(logbackConfig);
                LOG.info("Logback loaded from '{}'", "classpath:" + LOGBACK_CONFIG_PATH);
            }
        } catch (JoranException e) {
            // StatusPrinter will handle this
        } finally {
            if (logbackConfig != null) {
                try {
                    logbackConfig.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        StatusPrinter.printIfErrorsOccured(CONTEXT);
    }

    public static void join(ServletContext servletContext) {
        servletContext.setInitParameter("logbackDisableServletContainerInitializer", "true");
    }
}
