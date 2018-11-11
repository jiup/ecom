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

package com.codeager.ecom.service;

import com.codeager.ecom.domain.Category;
import com.codeager.ecom.dto.view.FancyTreeNode;

import java.util.List;

/**
 * @author Jiupeng Zhang
 * @since 03/09/2018
 */
public interface CategoryService {
    List<FancyTreeNode> getAsFancyTreeMap();
    Category getByName(String name);
    Category addCategory(int parent, String name, String prompt);
    void renameById(String newName, int id);
    Category toggleHidden(int id);
    List<Category> getChildrenById(int id);
    void deleteById(int id);
    void moveParentByIds(int id, int newParentId);
    List<Category> getPathFilledCategories();
}
