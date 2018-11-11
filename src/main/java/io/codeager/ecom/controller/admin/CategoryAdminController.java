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

import com.codeager.ecom.controller.FormValidationException;
import com.codeager.ecom.domain.Product;
import com.codeager.ecom.dto.view.FancyTreeNode;
import com.codeager.ecom.service.CategoryService;
import com.codeager.ecom.service.ProductService;
import com.codeager.portal.annotation.Authenticate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.List;

import static com.codeager.ecom.util.Routing.ADMIN_BASE;
import static com.codeager.ecom.util.Routing.redirect;
import static com.codeager.portal.Role.ADMIN;

/**
 * @author Jiupeng Zhang
 * @since 03/09/2018
 */
@Controller
@RequestMapping(ADMIN_BASE)
public class CategoryAdminController {
    private CategoryService categoryService;
    private GsonHttpMessageConverter gsonHttpMessageConverter;
    private ProductService productService;

    @Autowired
    public void setCategoryService(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Autowired
    public void setGsonHttpMessageConverter(GsonHttpMessageConverter gsonHttpMessageConverter) {
        this.gsonHttpMessageConverter = gsonHttpMessageConverter;
    }

    @Autowired
    public void setProductService(ProductService productService) {
        this.productService = productService;
    }

    @RequestMapping("/categories.json")
    @ResponseBody
    @Authenticate({ADMIN})
    public List<FancyTreeNode> getFancyTreeJson() {
        return categoryService.getAsFancyTreeMap();
    }

    @GetMapping("/categories")
    @Authenticate({ADMIN})
    public String categoryManagementPage(Model model) {
        model.addAttribute("fancytree", gsonHttpMessageConverter.getGson().toJson(categoryService.getAsFancyTreeMap()));
        return "categories";
    }

    @PostMapping("/categories/add")
    @Authenticate({ADMIN})
    public String addNewCategory(int parent, String name, String prompt) {
        if (categoryService.getByName(name) != null) {
            throw new FormValidationException("Category already exists", "/categories");
        }

        if (categoryService.addCategory(parent, name, prompt) == null) {
            throw new FormValidationException("Parent node not exists", "/categories");
        }
        return redirect(ADMIN_BASE, "/categories");
    }

    @GetMapping("/categories/rename/{id}")
    @Authenticate({ADMIN})
    public String renameCategory(@PathVariable("id") int id, String name) {
        try {
            name = new String(name.getBytes("ISO-8859-1"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        if (categoryService.getByName(name) != null) {
            throw new FormValidationException("Duplicate category name", "/categories");
        }

        categoryService.renameById(name, id);
        return redirect(ADMIN_BASE, "/categories");
    }

    @GetMapping("/categories/toggleStatus/{id}")
    @Authenticate({ADMIN})
    public String toggleCategoryStatus(@PathVariable("id") int id) {
        categoryService.toggleHidden(id);
        return redirect(ADMIN_BASE, "/categories");
    }

    @GetMapping("/categories/move/{from}/{to}")
    @Authenticate({ADMIN})
    public String moveCategory(@PathVariable("from") int from, @PathVariable("to") int to) {
        categoryService.moveParentByIds(from, to);
        return redirect(ADMIN_BASE, "/categories");
    }

    @GetMapping("/categories/delete/{id}")
    @Authenticate({ADMIN})
    public String deleteCategory(@PathVariable("id") int id) {
        if (categoryService.getChildrenById(id).size() > 0) {
            throw new FormValidationException("Please remove all the sub-categories first", "/categories");
        }

        List<Product> products = productService.getProductsByCategoryId(id);
        if (products.size() > 0) {
            StringBuilder builder = new StringBuilder();
            products.forEach(product -> builder.append("SPU%23").append(product.getId()).append("%0D"));
            throw new FormValidationException("This category was referenced by the following products:%0D%0D"
                    + builder.toString() + "%0DYou must disassociate them first.", "/categories");
        }

        categoryService.deleteById(id);
        return redirect(ADMIN_BASE, "/categories");
    }
}
