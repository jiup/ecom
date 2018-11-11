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

package com.codeager.ecom.controller.admin;

import com.codeager.ecom.domain.Specification;
import com.codeager.ecom.service.CategoryService;
import com.codeager.ecom.service.SpecificationService;
import com.codeager.portal.annotation.Authenticate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.Set;

import static com.codeager.ecom.util.Routing.ADMIN_BASE;
import static com.codeager.ecom.util.Routing.redirect;
import static com.codeager.portal.Role.ADMIN;
import static com.codeager.portal.Role.EDITOR;

/**
 * @author Jiupeng Zhang
 * @since 03/10/2018
 */
@Controller
@RequestMapping(ADMIN_BASE)
public class SpecificationFilterAdminController {
    private CategoryService categoryService;
    private SpecificationService specificationService;

    @Autowired
    public void setCategoryService(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Autowired
    public void setSpecificationService(SpecificationService specificationService) {
        this.specificationService = specificationService;
    }

    @GetMapping("/filter")
    @Authenticate({ADMIN, EDITOR})
    public String productFilterPage(@RequestParam(defaultValue = "0") int cat, Model model) {
        model.addAttribute("categories", categoryService.getPathFilledCategories());
        if (cat > 0) {
            Set<Specification> specs = specificationService.getDistinctSpecificationsByCategoryId(cat);
//          cancel category parent extends on specs
//            for (Category category : categoryService.getChildrenById(cat)) {
//                specs.addAll(specificationService.getDistinctSpecificationsByCategoryId(category.getId()));
//            }
            model.addAttribute("specs", specs);
        } else {
            model.addAttribute("specs", Arrays.asList());
        }
        return "filter";
    }

    @PostMapping("/filter/group/flip")
    @Authenticate({ADMIN, EDITOR})
    public String flipGroupStatus(int cat, String[] targets) {
        for (String attrName : targets) {
            specificationService.flipGroupStatus(cat, attrName);
        }
        return redirect(ADMIN_BASE, "/filter?cat=" + cat);
    }
}
