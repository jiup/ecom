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
import com.codeager.ecom.domain.Specification;
import com.codeager.ecom.domain.StockKeepingUnit;
import com.codeager.ecom.dto.form.InventoryForm;
import com.codeager.ecom.dto.form.InventoryInitForm;
import com.codeager.ecom.dto.form.SpecificationInitForm;
import com.codeager.ecom.service.ProductService;
import com.codeager.ecom.service.SpecificationService;
import com.codeager.ecom.service.StockKeepingUnitService;
import com.codeager.portal.annotation.Authenticate;
import com.codeager.portal.domain.Administrator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.codeager.ecom.util.Routing.ADMIN_BASE;
import static com.codeager.ecom.util.Routing.redirect;
import static com.codeager.portal.Role.ADMIN;
import static com.codeager.portal.Role.EDITOR;

/**
 * @author Jiupeng Zhang
 * @since 03/03/2018
 */
@Controller
@RequestMapping(ADMIN_BASE)
public class InventoryAdminController {
    private StockKeepingUnitService stockKeepingUnitService;
    private ProductService productService;
    private SpecificationService specificationService;

    @Autowired
    public void setStockKeepingUnitService(StockKeepingUnitService stockKeepingUnitService) {
        this.stockKeepingUnitService = stockKeepingUnitService;
    }

    @Autowired
    public void setProductService(ProductService productService) {
        this.productService = productService;
    }

    @Autowired
    public void setSpecificationService(SpecificationService specificationService) {
        this.specificationService = specificationService;
    }

    @GetMapping("/inventory")
    @Authenticate({ADMIN, EDITOR})
    public String inventoryPage(
            @RequestParam(defaultValue = "0") int pageSize,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "0") long spu,
            @RequestParam(defaultValue = "") String keyword,
            Administrator administrator,
            Model model
    ) {

        model.addAttribute("administrator", administrator);
        model.addAttribute("page", spu == 0 ?
                stockKeepingUnitService.getStockKeepingUnits(pageNum, pageSize) :
                stockKeepingUnitService.getStockKeepingUnitsByProductId(pageNum, pageSize, spu));
        if (spu == 0) {
            if (keyword.trim().length() > 0) {
                List<Product> resultList = productService.getProductsByKeyword(keyword);
                model.addAttribute("resultList", resultList);
            } else {
                model.addAttribute("resultList", Arrays.asList());
            }
        } else {
            model.addAttribute("resultList", Arrays.asList(productService.getProductByProductId(spu)));
        }

        return "inventory";
    }

    @GetMapping("/inventory/{sku}/specs")
    @Authenticate({ADMIN, EDITOR})
    public String specsPage(
            @RequestParam(defaultValue = "0") int pageSize,
            @RequestParam(defaultValue = "1") int pageNum,
            @PathVariable("sku") long skuId,
            Model model
    ) {
        StockKeepingUnit sku = stockKeepingUnitService.getStockKeepingUnitById(skuId);
        if (sku == null) {
            return redirect(ADMIN_BASE, "/inventory");
        }
        Product product = productService.getProductByProductId(sku.getProductId());
        if (product == null) {
            return redirect(ADMIN_BASE, "/inventory");
        }

        Page<Specification> page = specificationService.getSpecificationsBySkuId(pageNum, pageSize, skuId);
        Set<Specification> options = specificationService.getDistinctSpecificationsByCategoryId(product.getCategoryId());
        Set<Specification> dup = new HashSet<>();
        page.forEach(spec -> {
            Specification specification = new Specification();
            specification.setName(spec.getName());
            specification.setUnitsOfMeasurement(spec.getUnitsOfMeasurement());
            dup.add(specification);
        });
        options.removeAll(dup);

        model.addAttribute("sku", sku);
        model.addAttribute("product", product);
        model.addAttribute("page", page);
        model.addAttribute("options", options);
        return "inventory-specs";
    }

    @PostMapping("/inventory/{sku}/specs/add")
    @Authenticate({ADMIN, EDITOR})
    public String addSpecification(SpecificationInitForm form, @PathVariable("sku") long skuId) {
        Specification specification = specificationService.getSpecificationBySkuIdAndName(skuId, form.getName());
        if (specification != null) {
            throw new FormValidationException("Specification already exists", "/inventory/" + skuId + "/specs");
        }
        StockKeepingUnit sku = stockKeepingUnitService.getStockKeepingUnitById(skuId);
        if (sku == null) {
            return redirect(ADMIN_BASE, "/inventory");
        }
        Product product = productService.getProductByProductId(sku.getProductId());
        if (product == null) {
            return redirect(ADMIN_BASE, "/inventory");
        }

        specificationService.addSpecification(product.getCategoryId(), skuId, form);

        return redirect(ADMIN_BASE, "/inventory/" + skuId + "/specs");
    }

    @GetMapping("/inventory/{sku}/specs/{spec}/delete")
    @Authenticate({ADMIN})
    public String toggleSKUStatus(@PathVariable("sku") long skuId, @PathVariable("spec") long specId) {
        specificationService.deleteById(specId);

        return redirect(ADMIN_BASE, "/inventory/" + skuId + "/specs");
    }

    @GetMapping("/inventory/{sku}/toggleStatus")
    @Authenticate({ADMIN})
    public String toggleSKUStatus(@PathVariable("sku") long sku) {
        stockKeepingUnitService.toggleFrozenStatus(sku);

        return redirect(ADMIN_BASE, "/inventory/" + sku);
    }

    @GetMapping("/inventory/{sku}")
    @Authenticate({ADMIN})
    public String skuPage(@PathVariable("sku") long id, Model model) {
        StockKeepingUnit sku = stockKeepingUnitService.getStockKeepingUnitById(id);

        if (sku == null) {
            return redirect(ADMIN_BASE, "/inventory");
        }

        model.addAttribute("sku", sku);
        return "inventory-edit";
    }

    @PostMapping("/inventory/{sku}/update")
    @Authenticate({ADMIN})
    public String updateSku(@PathVariable("sku") long id, @Valid InventoryForm form, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new FormValidationException(bindingResult, "/inventory/" + id);
        }

        StockKeepingUnit stockKeepingUnit = stockKeepingUnitService.getStockKeepingUnitById(id);
        if (form.getExtSku() != null && !stockKeepingUnit.getSerialNumber().equals(form.getExtSku()) &&
                stockKeepingUnitService.getBySerialNumberAndProductId(form.getExtSku(), stockKeepingUnit.getProductId()) != null) {
            throw new FormValidationException("Duplicate EXT_SKU%23", "/inventory/" + id);
        }

        try {
            if (form.getPrice() != null && form.getPrice().length() > 0) {
                new BigDecimal(form.getPrice());
            } else {
                form.setPrice("0");
            }
            if (form.getListPrice() != null && form.getListPrice().length() > 0) {
                new BigDecimal(form.getListPrice());
            } else {
                form.setListPrice(form.getPrice());
            }
        } catch (Exception e) {
            throw new FormValidationException("Invalid Price", "/inventory/" + id);
        }

        stockKeepingUnitService.updateInventoryFormById(form, id);
        return redirect(ADMIN_BASE, "/inventory/" + id);
    }

    @GetMapping("/inventory/{sku}/delete")
    @Authenticate({ADMIN})
    public String deleteSku(@PathVariable("sku") long id) {
        StockKeepingUnit stockKeepingUnit = stockKeepingUnitService.getStockKeepingUnitById(id);
        if (stockKeepingUnit == null) {
            return redirect(ADMIN_BASE, "/inventory");
        }
        stockKeepingUnitService.deleteStockKeepingUnitById(id);
        return redirect(ADMIN_BASE, "/inventory?spu=" + stockKeepingUnit.getProductId());
    }

    @PostMapping("/inventory/add")
    @Authenticate({ADMIN})
    public String addInventory(@Valid InventoryInitForm form, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new FormValidationException(bindingResult, "/inventory");
        }

        try {
            if (form.getPrice() != null && form.getPrice().length() > 0) {
                new BigDecimal(form.getPrice());
            } else {
                form.setPrice("0");
            }
            if (form.getListPrice() != null && form.getListPrice().length() > 0) {
                new BigDecimal(form.getListPrice());
            } else {
                form.setListPrice(form.getPrice());
            }
        } catch (Exception e) {
            throw new FormValidationException("Bad Price Format", "/inventory");
        }

        if (stockKeepingUnitService.getBySerialNumberAndProductId(form.getSkuSerialNumber(), form.getProductId()) != null) {
            throw new FormValidationException("Duplicate EXT_SKU%23", "/inventory?spu=" + form.getProductId());
        }

        StockKeepingUnit stockKeepingUnit = stockKeepingUnitService.initInventory(form);
        if (stockKeepingUnit == null) {
            throw new FormValidationException("Product (SPU=" + form.getProductId() + ") not exists", "/inventory");
        }

        return redirect(ADMIN_BASE, "/inventory/" + stockKeepingUnit.getId());
    }
}
