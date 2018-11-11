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

import org.springframework.web.filter.CharacterEncodingFilter;

import javax.servlet.ServletContext;

/**
 * @author Jiupeng Zhang
 * @since 12/26/2017
 */
public class EncodingUtils {
    public static void join(ServletContext servletContext) {
        CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter("UTF-8", true);
        servletContext
                .addFilter("characterEncodingFilter", characterEncodingFilter)
                .addMappingForUrlPatterns(null, false, "/*");
    }
}
