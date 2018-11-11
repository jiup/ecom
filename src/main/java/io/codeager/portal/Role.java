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

package io.codeager.portal;

/**
 * @author Jiupeng Zhang
 * @since 11/25/2017
 */
public enum Role {
    SUPER("Super Administrator", 127),
    ADMIN("Administrator", 63),
    EDITOR("Editor", 15),
    MEMBER("Member", 7),
    BANNED("Member [Banned]", 3),
    GUEST("Guest", 0);

    private String name;
    private int code;

    Role(String name, int code) {
        this.name = name;
        this.code = code;
    }

    public static Role fromCode(int code) {
        for (Role role : Role.values()) {
            if (role.code == code) {
                return role;
            }
        }
        return GUEST;
    }

    public String getName() {
        return name;
    }

    public int getCode() {
        return code;
    }

    public boolean hasPermission(Role role) {
        return role == GUEST || this == role || contains(role);
    }

    private boolean contains(Role role) {
        switch (this) {
            case SUPER:
                switch (role) {
                    case ADMIN:
                    case EDITOR:
                        return true;
                }
            case MEMBER:
                switch (role) {
                    case BANNED:
                        return true;
                }
        }
        return false;
    }

}
