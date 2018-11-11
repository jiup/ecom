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

import com.google.common.base.Splitter;
import com.codeager.ecom.dao.mapper.CategoryMapper;
import com.codeager.ecom.domain.Category;
import com.codeager.ecom.dto.view.FancyTreeNode;
import com.codeager.ecom.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Jiupeng Zhang
 * @since 03/09/2018
 */
@Service
public class CategoryServiceImpl implements CategoryService {
    private CategoryMapper mapper;

    @Autowired
    public void setMapper(CategoryMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public List<FancyTreeNode> getAsFancyTreeMap() {
        FancyTreeNode root = new FancyTreeNode("0");
        List<Category> categories = mapper.selectAll();
        categories.forEach(category -> {
            List<String> path = Splitter.on('/').omitEmptyStrings().splitToList(category.getPath());
            FancyTreeNode p = root;
            for (int i = 0; i < path.size(); i++) {
                FancyTreeNode node = new FancyTreeNode(path.get(i));
                int nodeIndex = p.getChildren().indexOf(node);
                if (nodeIndex != -1) {
                    p = p.getChildren().get(nodeIndex);
                } else {
                    p.getChildren().add(node);
                    p = node;
                }
                if (i == path.size() - 1) {
                    p.setTitle(category.getName());
                    if (category.isHidden()) {
                        p.setExtraClasses("isHidden");
                    }
                }
            }
        });
        return root.getChildren();
    }

    @Override
    public Category getByName(String name) {
        return mapper.selectByCategoryName(name);
    }

    @Override
    @Transactional
    public Category addCategory(int parent, String name, String prompt) {
        Category parentCategory = mapper.selectByPrimaryKey(parent);
        if (parent > 0 && parentCategory == null) {
            return null;
        }
        String parentPath = parent > 0 ? parentCategory.getPath() : "";

        Category category = new Category();
        category.setName(name);
        category.setPrompt(prompt);
        category.setCreateTime(ZonedDateTime.now());
        category.setUpdateTime(ZonedDateTime.now());
        mapper.insert(category);

        category.setPath(parentPath.concat("/") + category.getId());
        mapper.updateByPrimaryKey(category);

        return category;
    }

    @Override
    @Transactional
    public void renameById(String newName, int id) {
        Category category = mapper.selectByPrimaryKey(id);
        if (category == null) {
            return;
        }

        category.setName(newName);
        category.setUpdateTime(ZonedDateTime.now());
        mapper.updateByPrimaryKey(category);
    }

    @Override
    public Category toggleHidden(int id) {
        Category category = mapper.selectByPrimaryKey(id);
        if (category == null) {
            return null;
        }

        category.setHidden(!category.isHidden());
        category.setUpdateTime(ZonedDateTime.now());
        mapper.updateByPrimaryKey(category);
        return category;
    }

    @Override
    public List<Category> getChildrenById(int id) {
        Category category = mapper.selectByPrimaryKey(id);
        return category != null ? mapper.selectChildrenByPath(category.getPath()) : Arrays.asList();
    }

    @Override
    public void deleteById(int id) {
        mapper.deleteByPrimaryKey(id);
    }

    @Override
    @Transactional
    public void moveParentByIds(int id, int newParentId) {
        Category target = mapper.selectByPrimaryKey(id);
        Category newParent = mapper.selectByPrimaryKey(newParentId);
        if (target == null && newParent == null) {
            return;
        }

        // move target's children nodes
        mapper.updatePath(target.getPath(), newParent.getPath().concat("/").concat(target.getPath().substring(target.getPath().lastIndexOf('/') + 1)));

        // update target's path
        target.setPath(newParent.getPath().concat("/").concat(target.getPath().substring(target.getPath().lastIndexOf('/') + 1)));
        mapper.updateByPrimaryKey(target);
    }

    @Override
    public List<Category> getPathFilledCategories() {
        Map<Integer, String> id2NameMap = new HashMap<>();
        List<Category> categories = mapper.selectAll();
        for (Category category : categories) {
            id2NameMap.put(category.getId(), category.getName());
        }
        for (Category category : categories) {
            category.setPath(Splitter.on("/").omitEmptyStrings().splitToList(category.getPath())
                    .stream().map(id -> id2NameMap.get(Integer.parseInt(id))).reduce("", (p0, p1) -> p0.concat("/").concat(p1)));
        }
        return categories;
    }
}
