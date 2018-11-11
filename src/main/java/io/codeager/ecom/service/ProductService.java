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

package io.codeager.ecom.service;

import com.github.pagehelper.Page;
import io.codeager.ecom.domain.Product;
import io.codeager.ecom.domain.ProductDetail;
import io.codeager.ecom.domain.ProductImage;
import io.codeager.ecom.domain.ProductLabel;
import io.codeager.ecom.dto.form.ProductForm;
import io.codeager.ecom.dto.form.ProductInitForm;
import io.codeager.ecom.dto.view.ProductView;

import java.util.List;

/**
 * @author Jiupeng Zhang
 * @since 02/25/2018
 */
public interface ProductService {
    Page<ProductLabel> getLabels(int pageNum, int pageSize);
    Page<Product> getProducts(int pageNum, int pageSize);
    List<Product> getProductsByKeyword(String keyword);
    List<Product> getProductsByCategoryId(int categoryId);
    Product getProductByProductId(long productId);
    ProductDetail getDetailByProductId(long productId);
    ProductImage getMainImageByProductId(long productId);
    List<ProductImage> getSubImagesByProductId(long productId);
    ProductView getProductViewById(long productId);
    Product initProduct(ProductInitForm productInitForm);
    void deleteById(long productId);
    void resetImageByImageId(long imageId);
    void deleteImageByImageId(long imageId);
    void addSubImage(long productId, String imageUrl);
    void updateImageUrlByImageId(String imageUrl, long imageId);
    void updateProductForm(ProductForm form);
    void toggleProductDisplayStatusByProductId(long productId);
}
