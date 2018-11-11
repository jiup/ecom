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

package com.codeager.ecom.util;

/**
 * @author Jiupeng Zhang
 * @since 12/18/2017
 */
public final class Pattern {
    public static final String EMAIL = "^[a-zA-Z0-9._%+\\-]+@[a-zA-Z0-9.\\-]+\\.[a-zA-Z]{2,6}$";

    // max 32, case insensitive in url, like github
    public static final String USERNAME = "^[a-zA-Z\\d](?:[a-zA-Z\\d]|-(?=[a-zA-Z\\d])){1,31}$";

    // At least 8 chars
    // Contains at least one digit
    // Contains at least one lower alpha char and one upper alpha char
    // Contains at least one char within a set of special chars (@#%$^ etc.)
    // Does not contain space, tab, etc.
    public static final String PASSWORD = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+/\\\\=])(?=\\S+$).{8,}$";
    public static final java.util.regex.Pattern AT_LEAST_ONE_DIGIT = java.util.regex.Pattern.compile("(?=.*[0-9])");
    public static final java.util.regex.Pattern AT_LEAST_ONE_LOWERCASE_ALPHA = java.util.regex.Pattern.compile("(?=.*[a-z])");
    public static final java.util.regex.Pattern AT_LEAST_ONE_UPPERCASE_ALPHA = java.util.regex.Pattern.compile("(?=.*[A-Z])");
    public static final java.util.regex.Pattern AT_LEAST_ONE_SPECIAL_CHAR = java.util.regex.Pattern.compile("(?=.*[@#$%^&+/\\\\=])");
    public static final java.util.regex.Pattern NO_SPACE = java.util.regex.Pattern.compile("(?=\\S+$)");
}
