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

package io.codeager.ecom.controller.admin;

import io.codeager.ecom.controller.CookiePassingException;
import io.codeager.ecom.controller.FormValidationException;
import io.codeager.ecom.dto.form.AdminLoginForm;
import io.codeager.ecom.service.RegisterMailService;
import io.codeager.portal.AuthenticationHandler;
import io.codeager.portal.Role;
import io.codeager.portal.annotation.Authenticate;
import io.codeager.portal.domain.Administrator;
import io.codeager.portal.service.AdministratorService;
import io.codeager.portal.util.Constant;
import io.codeager.portal.util.IdentityLocator;
import io.codeager.portal.util.TokenHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.mail.SendFailedException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;

import static io.codeager.ecom.util.Routing.*;
import static io.codeager.portal.Role.*;

/**
 * @author Jiupeng Zhang
 * @since 11/28/2017
 */
@Controller("administratorController")
@RequestMapping(ADMIN_BASE)
public class AdministratorController {
    private AdministratorService administratorService;
    private RegisterMailService registerMailService;

    @Autowired
    public void setAdministratorService(AdministratorService administratorService) {
        this.administratorService = administratorService;
    }

    @Autowired
    public void setRegisterMailService(RegisterMailService registerMailService) {
        this.registerMailService = registerMailService;
    }

    @ModelAttribute("adminLogin")
    public AdminLoginForm getLoginForm() {
        return new AdminLoginForm();
    }

    @GetMapping(ADMIN_LOGIN)
    @Authenticate(value = GUEST, explicit = true, handler = AdministratorAuthenticationHandler.class)
    public String loginPage() {
        return "login";
    }

    @PostMapping(ADMIN_LOGIN)
    @Authenticate(value = GUEST, explicit = true, handler = AdministratorAuthenticationHandler.class)
    public String login(
            @ModelAttribute("adminLogin") @Valid AdminLoginForm form, BindingResult bindingResult,
            @CookieValue(value = Constant.AUTH_COOKIE_NAME, required = false) String authCookie,
            @RequestParam(required = false) String forward,
            HttpServletRequest request
    ) {
        if (authCookie == null)
            throw new CookiePassingException("login form submitted without authCookie, might be a spider");

        if (bindingResult.hasErrors()) {
            throw new FormValidationException(bindingResult, ADMIN_LOGIN);
        }

        Administrator administrator = administratorService.login(authCookie, form.getUsername(), form.getPassword());
        request.getSession().invalidate();

        if (forward != null) {
            return redirect(forward);
        }
        // change password after first login
        if (administrator.getLoginCount() == 1) {
            return redirect(ADMIN_BASE, "/account/password");
        }
        return redirect(ADMIN_BASE, HOME);
    }

    @GetMapping({INDEX, HOME})
    @Authenticate({ADMIN, EDITOR})
    public String homePage() {
        return "index";
    }

    @PostMapping(ADMIN_LOGOUT)
    public String logout(
            @CookieValue(value = Constant.AUTH_COOKIE_NAME, required = false) String authCookie,
            HttpServletRequest request
    ) {
        if (authCookie == null)
            throw new CookiePassingException();

        administratorService.logout(authCookie);
        request.getSession().invalidate();
        return redirect(ADMIN_BASE, ADMIN_LOGIN);
    }

    @GetMapping("/accounts")
    @Authenticate(value = {SUPER, ADMIN}, explicit = true)
    public String accountsPage(Administrator administrator, Model model, HttpServletRequest request) {
        if (administrator == null) {
            return redirect(ADMIN_BASE, "/accounts");
        }

        model.addAttribute("administrator", administrator);
        List<Administrator> accounts = administratorService.getAllEditors();
        switch (administrator.getRole()) {
            case SUPER:
                accounts.addAll(administratorService.getAllAdministrators());
                model.addAttribute("accounts", accounts);
                break;
            case ADMIN:
                model.addAttribute("accounts", accounts);
                break;
        }
        request.getSession().invalidate();
        return "accounts";
    }

    @GetMapping("/account/{id}/reset")
    @Authenticate(value = {SUPER, ADMIN}, explicit = true)
    public String resetPassword(Administrator administrator, @PathVariable("id") long id) {
        Administrator target = administratorService.getById(id);
        if (target == null) {
            return redirect(ADMIN_BASE, "/accounts");
        }

        if (administrator.getRole() == ADMIN && target.getRole() == ADMIN) {
            throw new FormValidationException("Out of permission.", "/accounts");
        }

        String newRandomPassword = TokenHelper.createToken(8, false);
        administratorService.kickOffById(target.getId());
        administratorService.updatePasswordById(target, newRandomPassword);

        try {
            registerMailService.sendNewAdminAccountInform(
                    target.getEmailAddress(),
                    target.getDisplayName(),
                    target.getLoginName(),
                    newRandomPassword);
        } catch (SendFailedException e) {
            e.printStackTrace();
        }
        return redirect(ADMIN_BASE, "/accounts?resp=Password reset email has been successfully sent to the account's mailbox.");
    }

    @PostMapping("/account/new/{accountType}")
    @Authenticate(value = {SUPER, ADMIN}, explicit = true)
    public String addAccount(
            Administrator administrator,
            String loginName,
            String displayName,
            String emailAddress,
            @PathVariable("accountType") String accountType) {

        if (administratorService.containsLoginName(loginName)) {
            throw new FormValidationException("Duplicate login name.", "/accounts");
        }

        String newRandomPassword = TokenHelper.createToken(8, false);
        switch (accountType) {
            case "editor":
                administratorService.addNewEditor(loginName, displayName, emailAddress, newRandomPassword);
                break;
            case "administrator":
                if (administrator.getRole() == ADMIN) {
                    throw new FormValidationException("Out of permission.", "/accounts");
                }
                administratorService.addNewAdministrator(loginName, displayName, emailAddress, newRandomPassword);
                break;
        }

        try {
            registerMailService.sendNewAdminAccountInform(emailAddress, displayName, loginName, newRandomPassword);
        } catch (SendFailedException e) {
            e.printStackTrace();
        }
        return redirect(ADMIN_BASE, "/accounts");
    }

    @GetMapping("/account/{id}/delete")
    @Authenticate(value = {SUPER, ADMIN}, explicit = true)
    public String deleteAccount(Administrator administrator, @PathVariable("id") long id) {
        Administrator target = administratorService.getById(id);
        if (target == null) {
            return redirect(ADMIN_BASE, "/accounts");
        }

        if (administrator.getRole() == ADMIN && target.getRole() == ADMIN) {
            throw new FormValidationException("Out of permission.", "/accounts");
        }

        administratorService.kickOffById(id);
        administratorService.deleteById(id);
        return redirect(ADMIN_BASE, "/accounts");
    }

    @GetMapping("/account")
    @Authenticate({ADMIN, EDITOR})
    public String myAccountPage(Administrator administrator, Model model) {
        if (administrator == null) {
            return redirect(ADMIN_BASE, "/login");
        }
        model.addAttribute("administrator", administrator);
        return "account";
    }

    @GetMapping("/account/record")
    @Authenticate({ADMIN, EDITOR})
    public String accountRecordPage(Administrator administrator, Model model) {
        if (administrator == null) {
            return redirect(ADMIN_BASE, "/login");
        }
        model.addAttribute("administrator", administrator);
        model.addAttribute("city", IdentityLocator.getCityByIp(administrator.getLastLoginIp()));
        model.addAttribute("browser", IdentityLocator.getBrowserNameByUserAgent(administrator.getLastLoginUserAgent()));
        return "account-record";
    }

    @GetMapping("/account/password")
    @Authenticate({ADMIN, EDITOR})
    public String changePasswordPage() {
        return "account-psw";
    }

    @PostMapping("/account")
    @Authenticate({ADMIN, EDITOR})
    public String updateAccount(
            Administrator administrator,
            String displayName,
            String emailAddress,
            HttpServletRequest request
    ) {
        if (administrator.getDisplayName().equals(displayName)
                && administrator.getEmailAddress().equals(emailAddress)) {
            throw new FormValidationException("No changes found.", "/account");
        }
        administrator.setDisplayName(displayName);
        administrator.setEmailAddress(emailAddress);
        administrator.setUpdateTime(ZonedDateTime.now());
        administratorService.updateAdministratorById(administrator);
        request.getSession().invalidate();
        return redirect(ADMIN_BASE, "/account");
    }

    @PostMapping("/account/password")
    @Authenticate({ADMIN, EDITOR})
    public String changePassword(
            @CookieValue(value = Constant.AUTH_COOKIE_NAME, required = false) String authCookie,
            Administrator administrator,
            String oldPassword,
            String newPassword,
            HttpServletRequest request
    ) {
        if (newPassword.length() < 8) {
            throw new FormValidationException("Password needs at least 8 characters", "/account/password");
        }

        if (!administratorService.isRightPassword(administrator, oldPassword)) {
            throw new FormValidationException("Wrong old password", "/account/password");
        }

        administratorService.updatePasswordById(administrator, newPassword);
        request.getSession().removeAttribute("user");
        request.getSession().removeAttribute("onlineAdminUsers");
        administratorService.logout(authCookie);

        return redirect(ADMIN_BASE, ADMIN_LOGIN + "?resp=Password changed successfully, please login again.");
    }

    @ExceptionHandler(LoggedException.class)
    public String adminIntrudeLoginExceptionHandler() {
        return redirect(ADMIN_BASE, HOME);
    }

    @Component
    static class AdministratorAuthenticationHandler implements AuthenticationHandler {
        @Override
        public boolean handleIntrude(HttpServletRequest request, Role currentRole, Role[] requiredRoles) {
            if (currentRole != GUEST && Arrays.asList(requiredRoles).contains(GUEST)) {
                throw new LoggedException();
            }
            return false;
        }
    }

    static class LoggedException extends RuntimeException {
    }
}
