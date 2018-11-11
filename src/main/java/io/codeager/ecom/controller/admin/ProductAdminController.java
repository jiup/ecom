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
import com.codeager.ecom.controller.FormValidationException;
import com.codeager.ecom.domain.Product;
import com.codeager.ecom.domain.ProductDetail;
import com.codeager.ecom.domain.ProductImage;
import com.codeager.ecom.dto.form.ProductForm;
import com.codeager.ecom.dto.form.ProductInitForm;
import com.codeager.ecom.service.CategoryService;
import com.codeager.ecom.service.ImageUploadService;
import com.codeager.ecom.service.ProductService;
import com.codeager.portal.annotation.Authenticate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.codeager.ecom.util.Routing.ADMIN_BASE;
import static com.codeager.ecom.util.Routing.redirect;
import static com.codeager.portal.Role.ADMIN;
import static com.codeager.portal.Role.EDITOR;

/**
 * @author Jiupeng Zhang
 * @since 02/26/2018
 */
@Controller
@RequestMapping(ADMIN_BASE)
public class ProductAdminController {
    private final static Set<String> ACCEPTED_IMAGE_CONTENT_TYPE = new HashSet<String>() {{
        add("image/jpeg");
        add("image/png");
    }};

    private ProductService productService;
    private ImageUploadService imageUploadService;
    private CategoryService categoryService;

    @Autowired
    public void setProductService(ProductService productService) {
        this.productService = productService;
    }

    @Autowired
    public void setImageUploadService(ImageUploadService imageUploadService) {
        this.imageUploadService = imageUploadService;
    }

    @Autowired
    public void setCategoryService(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/products")
    @Authenticate({ADMIN, EDITOR})
    public String productManagementPage(
            @RequestParam(defaultValue = "0") int pageSize,
            @RequestParam(defaultValue = "1") int pageNum,
            Model model
    ) {
        Page<Product> page = productService.getProducts(pageNum, pageSize);
        model.addAttribute("page", page);
        model.addAttribute("categories", categoryService.getPathFilledCategories());
        return "products";
    }

    @PostMapping("/product/add")
    @Authenticate({ADMIN, EDITOR})
    public String addProduct(ProductInitForm form) {
        Product product = productService.initProduct(form);
        return redirect(ADMIN_BASE, "/product/" + product.getId());
    }

    @PostMapping("/product/{id}/update")
    @Authenticate({ADMIN, EDITOR})
    public String updateProduct(
            @PathVariable("id") long id,
            ProductForm form
    ) {
        form.setId(id);
        productService.updateProductForm(form);
        return redirect(ADMIN_BASE, "/product/" + id);
    }

    @GetMapping("/product/{id}/toggleDisplayStatus")
    @Authenticate({ADMIN, EDITOR})
    public String toggleDisplayStatus(@PathVariable("id") long id) {
        productService.toggleProductDisplayStatusByProductId(id);
        return redirect(ADMIN_BASE, "/product/" + id);
    }

    @GetMapping("/product/{id}")
    @Authenticate({ADMIN, EDITOR})
    public String productPage(@PathVariable("id") long id, Model model) {
        Product product = productService.getProductByProductId(id);
        ProductImage mainImage = productService.getMainImageByProductId(id);
        List<ProductImage> subImages = productService.getSubImagesByProductId(id);
        ProductDetail productDetail = productService.getDetailByProductId(id);

        if (product == null || mainImage == null || subImages == null || productDetail == null) {
            return redirect(ADMIN_BASE, "/products");
        }

        model.addAttribute("product", product);
        model.addAttribute("mainImage", mainImage);
        model.addAttribute("subImages", subImages);
        model.addAttribute("detail", productDetail);
        model.addAttribute("categories", categoryService.getPathFilledCategories());
        return "product";
    }

    @GetMapping("/product/{productId}/image/{imageId}/reset")
    @Authenticate({ADMIN, EDITOR})
    public String resetImage(
            @PathVariable("productId") long productId,
            @PathVariable("imageId") long imageId
    ) {
        productService.resetImageByImageId(imageId);
        return redirect(ADMIN_BASE, "/product/" + productId);
    }

    @GetMapping("/product/{productId}/image/{imageId}/delete")
    @Authenticate({ADMIN, EDITOR})
    public String deleteImage(
            @PathVariable("productId") long productId,
            @PathVariable("imageId") long imageId
    ) {
        productService.deleteImageByImageId(imageId);
        return redirect(ADMIN_BASE, "/product/" + productId);
    }

    @PostMapping("/product/{productId}/image/add")
    @Authenticate({ADMIN, EDITOR})
    public String addSubImage(
            @PathVariable("productId") long productId,
            MultipartFile imageFile
    ) {
        productService.addSubImage(productId, uploadImage(imageFile, "/product/" + productId));
        return redirect(ADMIN_BASE, "/product/" + productId);
    }

    @PostMapping("/product/{productId}/image/{imageId}/replace")
    @Authenticate({ADMIN, EDITOR})
    public String replaceImage(
            @PathVariable("productId") long productId,
            @PathVariable("imageId") long imageId,
            MultipartFile imageFile
    ) {
        productService.updateImageUrlByImageId(uploadImage(imageFile, "/product/" + productId), imageId);
        return redirect(ADMIN_BASE, "/product/" + productId);
    }

    private String uploadImage(MultipartFile imageFile, String redirectPath) {
        if (imageFile != null && !imageFile.getOriginalFilename().equals("") && imageFile.getSize() > 0) {
            if (!ACCEPTED_IMAGE_CONTENT_TYPE.contains(imageFile.getContentType())) {
                throw new FormValidationException("File only accept PNG/JPG", redirectPath);
            }

            if (imageFile.getSize() > 2 * 1024 * 1024) {
                throw new FormValidationException("File exceeds 2MB", redirectPath);
            }

            try {
                String path = imageUploadService.upload(imageFile.getInputStream());
                return path.startsWith("http://") ? path.substring(5) : path;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }
}
