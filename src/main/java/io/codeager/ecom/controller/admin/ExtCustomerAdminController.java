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

package io.codeager.ecom.controller.admin;

import com.github.pagehelper.Page;
import io.codeager.ecom.domain.ExtCustomer;
import io.codeager.ecom.dto.form.ExtCustomerForm;
import io.codeager.ecom.service.ExtCustomerService;
import io.codeager.portal.annotation.Authenticate;
import io.codeager.portal.domain.Administrator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import static io.codeager.ecom.util.Routing.ADMIN_BASE;
import static io.codeager.ecom.util.Routing.redirect;
import static io.codeager.portal.Role.ADMIN;
import static io.codeager.portal.Role.EDITOR;

/**
 * @author Jiupeng Zhang
 * @since 01/11/2018
 */
@Controller
@RequestMapping(ADMIN_BASE)
public class ExtCustomerAdminController {
    private ExtCustomerService extCustomerService;

    @Autowired
    public void setExtCustomerService(ExtCustomerService extCustomerService) {
        this.extCustomerService = extCustomerService;
    }

    @GetMapping("/customer-ext")
    @Authenticate({ADMIN, EDITOR})
    public String extCustomerPage(
            @RequestParam(defaultValue = "0") int pageSize,
            @RequestParam(defaultValue = "1") int pageNum,
            Administrator administrator,
            Model model
    ) {
        Page<ExtCustomer> page = extCustomerService.getAll(pageNum, pageSize);
        model.addAttribute("page", page);
        model.addAttribute("administrator", administrator);
        return "customer-ext";
    }

    @GetMapping("/customer-ext/delete/{id}")
    @Authenticate({ADMIN})
    public String deleteExtCustomer(
            @PathVariable("id") long id
    ) {
        extCustomerService.delete(id);
        return redirect(ADMIN_BASE, "/customer-ext");
    }

    @PostMapping("/customer-ext/update/{id}")
    @Authenticate({ADMIN})
    public String updateExtCustomer(@PathVariable("id") long id, ExtCustomerForm form) {
        extCustomerService.updateById(form, id);
        return redirect(ADMIN_BASE, "/customer-ext");
    }

    @PostMapping("/customer-ext/add")
    @Authenticate({ADMIN, EDITOR})
    public String addExtCustomer(ExtCustomerForm form) {
        extCustomerService.add(form);
        return redirect(ADMIN_BASE, "/customer-ext");
    }
}
