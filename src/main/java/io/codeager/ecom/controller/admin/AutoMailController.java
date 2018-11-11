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

import com.google.common.base.Strings;
import com.codeager.ecom.controller.FormValidationException;
import com.codeager.ecom.domain.AutoMail;
import com.codeager.ecom.domain.MailTemplate;
import com.codeager.ecom.dto.form.MailTemplateForm;
import com.codeager.ecom.service.AutoMailService;
import com.codeager.ecom.service.ImageUploadService;
import com.codeager.portal.annotation.Authenticate;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import static com.codeager.ecom.util.Routing.ADMIN_BASE;
import static com.codeager.ecom.util.Routing.redirect;
import static com.codeager.portal.Role.ADMIN;
import static com.codeager.portal.Role.EDITOR;

/**
 * @author Jiupeng Zhang
 * @since 01/06/2018
 */
@Controller("autoMailController")
@RequestMapping(ADMIN_BASE)
public class AutoMailController {
    private final static Set<String> ACCEPTED_IMAGE_CONTENT_TYPE = new HashSet<String>() {{
        add("image/jpeg");
        add("image/png");
    }};

    private ImageUploadService imageUploadService;
    private AutoMailService autoMailService;

    @GetMapping("/mail-template")
    @Authenticate({ADMIN, EDITOR})
    public String mailTemplatePage(Model model) {
        model.addAttribute("previewHtml", Strings.nullToEmpty(autoMailService.loadParsedHtml()));
        model.addAttribute("template", ObjectUtils.defaultIfNull(autoMailService.loadTemplate(), new MailTemplate()));
        return "mail-template";
    }

    @PostMapping("/mail-template")
    @Authenticate({ADMIN, EDITOR})
    public String saveMailTemplate(@RequestParam(required = false) boolean overwriteImage, @Valid MailTemplateForm form, BindingResult bindingResult) {
        MultipartFile imageFile = form.getImageFile();
        boolean uploadImageFile = false;

        if (imageFile != null && !imageFile.getOriginalFilename().equals("") && imageFile.getSize() > 0) {
            if (!ACCEPTED_IMAGE_CONTENT_TYPE.contains(imageFile.getContentType())) {
                bindingResult.addError(new FieldError(MailTemplateForm.class.getSimpleName(), "imageFile", "only accept PNG/JPG"));
            }

            if (imageFile.getSize() > 2 * 1024 * 1024) {
                bindingResult.addError(new FieldError(MailTemplateForm.class.getSimpleName(), "imageFile", "exceeds 2MB"));
            }

            uploadImageFile = true;
        }

        if (bindingResult.hasErrors()) {
            throw new FormValidationException(bindingResult, "/mail-template");
        }

        String imageUrl = null;
        if (uploadImageFile) {
            try {
                imageUrl = imageUploadService.upload(imageFile.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        autoMailService.save(form, overwriteImage, Strings.nullToEmpty(imageUrl));

        return redirect(ADMIN_BASE, "/mail-template");
    }

    @GetMapping("/mail-sender")
    @Authenticate({ADMIN})
    public String mailSenderView(
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "1") int pageNum,
            Model model
    ) {
        model.addAttribute("page", autoMailService.getAll(pageNum, pageSize));
        return "mail-sender";
    }

    @GetMapping("/mail-sender/delete/{id}")
    @Authenticate({ADMIN, EDITOR})
    public String deleteContact(
            @PathVariable("id") long id,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "1") int pageNum
    ) {
        AutoMail autoMail = new AutoMail();
        autoMail.setId(id);
        autoMailService.delete(autoMail);
        return redirect(ADMIN_BASE, "/mail-sender?pageSize=" + pageSize + "&pageNum=" + pageNum);
    }

    @PostMapping("/mail-sender")
    @Authenticate({ADMIN})
    public String sendMail(
            // prepare for mail-template extension
            @RequestParam(required = false) long tid,
            @RequestParam String group,
            @RequestParam long[] ids
    ) {
        autoMailService.send(tid, group, ids);
        return redirect(ADMIN_BASE, "/mail-sender");
    }

    @GetMapping("/automail/unsubscribe/{token}")
    @ResponseBody
    public String unsubscribeAutomail(@PathVariable("token") String token) {
        autoMailService.unsubscribeByToken(token);
        return "You have successfully unsubscribed this email!";
    }

    @Autowired
    public void setImageUploadService(ImageUploadService imageUploadService) {
        this.imageUploadService = imageUploadService;
    }

    @Autowired
    public void setAutoMailService(AutoMailService autoMailService) {
        this.autoMailService = autoMailService;
    }
}
