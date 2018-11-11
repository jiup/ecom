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

package io.codeager.ecom.controller.api;

import com.github.pagehelper.Page;
import io.codeager.ecom.domain.ProductLabel;
import io.codeager.ecom.dto.view.$;
import io.codeager.ecom.dto.view.ProductView;
import io.codeager.ecom.dto.view.ResponseCode;
import io.codeager.ecom.dto.view.RestResponse;
import io.codeager.ecom.service.ProductService;
import io.codeager.ecom.util.Routing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static io.codeager.ecom.util.Routing.API_BASE;

/**
 * @author Jiupeng Zhang
 * @since 01/25/2018
 */
@RestController("productController")
@RequestMapping(API_BASE)
public class ProductController {
    private ProductService productService;

    @Autowired
    public void setProductService(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping(Routing.PRODUCT)
    public ResponseEntity<RestResponse> productView(@PathVariable("id") long id) {

        ProductView productView = productService.getProductViewById(id);
        if (productView == null) {
            return $.restful().code(ResponseCode.NOT_FOUND).asResponseEntity();
        }

        return $.restful().code(ResponseCode.OK).data().put("product", productView).ready().asResponseEntity();
    }

    @GetMapping(Routing.PRODUCTS)
    public ResponseEntity<RestResponse> productLabelView(
            @RequestParam(defaultValue = "0") int pageSize,
            @RequestParam(defaultValue = "1") int pageNum
    ) {
        Page<ProductLabel> productLabelPage = productService.getLabels(pageNum, pageSize);

        return $.restful().code(ResponseCode.OK).data()
                .put("products", productLabelPage)
                .put("pageNum", productLabelPage.getPageNum())
                .put("pageSize", productLabelPage.getPageSize())
                .put("total", productLabelPage.getTotal())
                .ready().asResponseEntity();
    }
}
