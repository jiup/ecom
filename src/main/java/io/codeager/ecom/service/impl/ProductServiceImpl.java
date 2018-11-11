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

package io.codeager.ecom.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.codeager.ecom.dao.mapper.ProductDetailMapper;
import io.codeager.ecom.dao.mapper.ProductImageMapper;
import io.codeager.ecom.dao.mapper.ProductLabelMapper;
import io.codeager.ecom.dao.mapper.ProductMapper;
import io.codeager.ecom.domain.*;
import io.codeager.ecom.dto.form.ProductForm;
import io.codeager.ecom.dto.form.ProductInitForm;
import io.codeager.ecom.dto.view.ProductView;
import io.codeager.ecom.service.ProductService;
import io.codeager.ecom.service.SpecificationService;
import io.codeager.ecom.service.StockKeepingUnitService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;

/**
 * @author Jiupeng Zhang
 * @since 02/25/2018
 */
@Service
public class ProductServiceImpl implements ProductService {
    private ProductMapper productMapper;
    private ProductLabelMapper productLabelMapper;
    private ProductDetailMapper productDetailMapper;
    private ProductImageMapper productImageMapper;

    private StockKeepingUnitService stockKeepingUnitService;
    private SpecificationService specificationService;

    @Autowired
    public void setProductMapper(ProductMapper productMapper) {
        this.productMapper = productMapper;
    }

    @Autowired
    public void setProductLabelMapper(ProductLabelMapper productLabelMapper) {
        this.productLabelMapper = productLabelMapper;
    }

    @Autowired
    public void setProductDetailMapper(ProductDetailMapper productDetailMapper) {
        this.productDetailMapper = productDetailMapper;
    }

    @Autowired
    public void setProductImageMapper(ProductImageMapper productImageMapper) {
        this.productImageMapper = productImageMapper;
    }

    @Autowired
    public void setStockKeepingUnitService(StockKeepingUnitService stockKeepingUnitService) {
        this.stockKeepingUnitService = stockKeepingUnitService;
    }

    @Autowired
    public void setSpecificationService(SpecificationService specificationService) {
        this.specificationService = specificationService;
    }

    @Override
    public Page<ProductLabel> getLabels(int pageNum, int pageSize) {
        return PageHelper.startPage(pageNum, pageSize, true).doSelectPage(() -> productLabelMapper.selectAll());
    }

    @Override
    public Page<Product> getProducts(int pageNum, int pageSize) {
        return PageHelper.startPage(pageNum, pageSize, true).doSelectPage(() -> productMapper.selectAll());
    }

    @Override
    public List<Product> getProductsByKeyword(String keyword) {
        return productMapper.selectByKeyword(keyword);
    }

    @Override
    public List<Product> getProductsByCategoryId(int categoryId) {
        return productMapper.selectByCategoryId(categoryId);
    }

    @Override
    public Product getProductByProductId(long productId) {
        return productMapper.selectById(productId);
    }

    @Override
    public ProductDetail getDetailByProductId(long productId) {
        return productDetailMapper.selectByProductId(productId);
    }

    @Override
    public ProductImage getMainImageByProductId(long productId) {
        return productImageMapper.selectMainImageByProductId(productId);
    }

    @Override
    public List<ProductImage> getSubImagesByProductId(long productId) {
        return productImageMapper.selectSubImagesByProductId(productId);
    }

    @Override
    public ProductView getProductViewById(long productId) {
        ProductView productView = new ProductView();
        ProductLabel productLabel = productLabelMapper.selectByProductId(productId);
        if (productLabel == null) {
            return null;
        }
        ProductDetail productDetail = productDetailMapper.selectByProductId(productId);
        if (productDetail != null && productDetail.iskVPairs()) {
            productView.setAttrs(productDetail.getContent());
        }
        productView.setId(productLabel.getId());
        productView.setName(productLabel.getName());
        productView.setOverview(productLabel.getOverview());
        productView.setMainImage(productLabel.getMainImage());
        productView.setSubImages(productLabel.getSubImages());

        return productView;
    }

    @Override
    @Transactional
    public Product initProduct(ProductInitForm productInitForm) {
        Product product = new Product();
        BeanUtils.copyProperties(productInitForm, product);
        product.setCreateTime(ZonedDateTime.now());
        product.setUpdateTime(ZonedDateTime.now());
        try {
            product.setCategoryId(Integer.parseInt(productInitForm.getCategoryId()));
        } catch (Exception e) {
        }

        productMapper.insert(product);
        product.setSortIndex(10 * product.getId());
        productMapper.updateById(product);

        ProductImage productImage = new ProductImage();
        productImage.setProductId(product.getId());
        productImage.setPrior(true);
        productImage.setCreateTime(ZonedDateTime.now());
        productImage.setUpdateTime(ZonedDateTime.now());
        productImage.setImageUrl("//static.codeager.io/ii104n9yeb.png");
        productImageMapper.insert(productImage);

        ProductDetail productDetail = new ProductDetail();
        productDetail.setProductId(product.getId());
        productDetail.setkVPairs(true);
        productDetail.setCreateTime(ZonedDateTime.now());
        productDetail.setUpdateTime(ZonedDateTime.now());
        productDetailMapper.insert(productDetail);
        return product;
    }

    @Override
    public void deleteById(long productId) {
        productMapper.deleteById(productId);
    }

    @Override
    public void resetImageByImageId(long imageId) {
        ProductImage image = productImageMapper.selectById(imageId);
        if (image != null) {
            image.setImageUrl("//static.codeager.io/ii104n9yeb.png");
            image.setUpdateTime(ZonedDateTime.now());
            productImageMapper.updateById(image);
        }
    }

    @Override
    public void deleteImageByImageId(long imageId) {
        productImageMapper.deleteById(imageId);
    }

    @Override
    public void addSubImage(long productId, String imageUrl) {
        ProductImage image = new ProductImage();
        image.setProductId(productId);
        image.setImageUrl(imageUrl);
        image.setPrior(false);
        image.setCreateTime(ZonedDateTime.now());
        image.setUpdateTime(ZonedDateTime.now());
        productImageMapper.insert(image);
    }

    @Override
    @Transactional
    public void updateImageUrlByImageId(String imageUrl, long imageId) {
        ProductImage image = productImageMapper.selectById(imageId);
        if (image == null) {
            return;
        }

        image.setImageUrl(imageUrl);
        image.setUpdateTime(ZonedDateTime.now());
        productImageMapper.updateById(image);
    }

    @Override
    @Transactional
    public void updateProductForm(ProductForm form) {
        Product product = productMapper.selectById(form.getId());
        if (product == null) {
            return;
        }
        ProductDetail productDetail = productDetailMapper.selectByProductId(form.getId());
        if (productDetail == null) {
            return;
        }
        if (form.getCategoryId() != product.getCategoryId()) {
            for (StockKeepingUnit sku : stockKeepingUnitService.getStockKeepingUnitsByProductId(1, 0, form.getId())) {
                specificationService.updateCategoryIdAndGroupStatusBySkuId(form.getCategoryId(), sku.getId());
            }
        }
        product.setCategoryId(form.getCategoryId());
        product.setProductName(form.getProductName());
        product.setOverview(form.getOverview());
        product.setSortIndex(form.getSortIndex());
        product.setAdminNote(form.getNotes());
        product.setUpdateTime(ZonedDateTime.now());
        productDetail.setContent(form.getDetails());
        productDetail.setkVPairs(!form.isRichText());
        productDetail.setUpdateTime(ZonedDateTime.now());
        productMapper.updateById(product);
        productDetailMapper.updateById(productDetail);
    }

    @Override
    @Transactional
    public void toggleProductDisplayStatusByProductId(long productId) {
        Product product = productMapper.selectById(productId);
        if (product != null) {
            product.setDeleted(!product.isDeleted());
            product.setUpdateTime(ZonedDateTime.now());
            productMapper.updateById(product);
        }
    }
}
