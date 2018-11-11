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

package com.codeager.portal.service;

import com.codeager.portal.domain.Administrator;

import java.util.List;

/**
 * @author Jiupeng Zhang
 * @since 12/11/2017
 */
public interface AdministratorService {
    Administrator login(String token, String loginName, String password);
    void logout(String token);
    Administrator getById(long id);
    Administrator getByLoginName(String loginName);
    List<Administrator> getAllLoggedAdministrators();
    List<Administrator> getAll();
    List<Administrator> getAllEditors();
    List<Administrator> getAllAdministrators();
    void updateAdministratorById(Administrator administrator);
    boolean isRightPassword(Administrator administrator, String password);
    void updatePasswordById(Administrator administrator, String password);
    void deleteById(long id);
    boolean containsLoginName(String loginName);
    void addNewAdministrator(String loginName, String displayName, String emailAddress, String password);
    void addNewEditor(String loginName, String displayName, String emailAddress, String password);
    void kickOffById(long id);
}
