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

import com.github.pagehelper.Page;
import com.codeager.ecom.domain.QuoteRequest;
import com.codeager.ecom.service.QuoteAdminService;
import com.codeager.portal.annotation.Authenticate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static com.codeager.ecom.util.Routing.ADMIN_BASE;
import static com.codeager.ecom.util.Routing.redirect;
import static com.codeager.portal.Role.ADMIN;
import static com.codeager.portal.Role.EDITOR;

/**
 * @author Jiupeng Zhang
 * @since 01/02/2018
 */
@Controller("quoteAdminController")
@RequestMapping(ADMIN_BASE)
public class QuoteAdminController {
    private QuoteAdminService quoteAdminService;

    @GetMapping("/quote")
    @Authenticate({ADMIN, EDITOR})
    public String quoteAdminView(
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "1") int pageNum,
            Model model
    ) {
        Page<QuoteRequest> quoteRequestPage = quoteAdminService.getAll(pageNum, pageSize);
        model.addAttribute("page", quoteRequestPage);
        return "quote";
    }

    @GetMapping("/quote/delete/{id}")
    @Authenticate({ADMIN, EDITOR})
    public String deleteContact(
            @PathVariable("id") long id,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "1") int pageNum
    ) {
        quoteAdminService.delete(id);
        return redirect(ADMIN_BASE, "/quote?pageSize=" + pageSize + "&pageNum=" + pageNum);
    }

    @Autowired
    public void setQuoteAdminService(QuoteAdminService quoteAdminService) {
        this.quoteAdminService = quoteAdminService;
    }
}
