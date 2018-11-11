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

package com.codeager.plugin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.ParameterizedType;
import java.util.Optional;

/**
 * @author Jiupeng Zhang
 * @since 12/14/2017
 */
public abstract class Plugin<T> {
    private static final Logger LOG = LoggerFactory.getLogger(Plugin.class);

    protected T futureHolder;

    protected Plugin(Optional<T> optionalFutureHolder) {
        if (optionalFutureHolder.isPresent()) {
            this.futureHolder = optionalFutureHolder.get();
            String genericTypeName = getGenericTypeName();
            if (futureHolder != null && genericTypeName != null) {
                LOG.info("Find plugin '{}' - {}", getName(), futureHolder.getClass().getTypeName());
            }
        }
    }

    public boolean isConnected() {
        return futureHolder != null;
    }

    public String getName() {
        return getGenericTypeName();
    }

    private String getGenericTypeName() {
        return getClass().getGenericSuperclass() instanceof ParameterizedType ?
                ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0].getTypeName() : null;
    }
}
