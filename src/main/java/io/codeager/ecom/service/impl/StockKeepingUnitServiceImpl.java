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
import io.codeager.ecom.dao.mapper.ProductMapper;
import io.codeager.ecom.dao.mapper.SpecificationMapper;
import io.codeager.ecom.dao.mapper.StockKeepingUnitMapper;
import io.codeager.ecom.domain.StockKeepingUnit;
import io.codeager.ecom.dto.form.InventoryForm;
import io.codeager.ecom.dto.form.InventoryInitForm;
import io.codeager.ecom.service.StockKeepingUnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

/**
 * @author Jiupeng Zhang
 * @since 03/03/2018
 */
@Service
public class StockKeepingUnitServiceImpl implements StockKeepingUnitService {
    private StockKeepingUnitMapper stockKeepingUnitMapper;
    private ProductMapper productMapper;
    private SpecificationMapper specificationMapper;

    @Autowired
    public void setStockKeepingUnitMapper(StockKeepingUnitMapper stockKeepingUnitMapper) {
        this.stockKeepingUnitMapper = stockKeepingUnitMapper;
    }

    @Autowired
    public void setProductMapper(ProductMapper productMapper) {
        this.productMapper = productMapper;
    }

    @Autowired
    public void setSpecificationMapper(SpecificationMapper specificationMapper) {
        this.specificationMapper = specificationMapper;
    }

    @Override
    public Page<StockKeepingUnit> getStockKeepingUnits(int pageNum, int pageSize) {
        return PageHelper.startPage(pageNum, pageSize, true).doSelectPage(() -> stockKeepingUnitMapper.selectAll());
    }

    @Override
    public Page<StockKeepingUnit> getStockKeepingUnitsByProductId(int pageNum, int pageSize, long productId) {
        return PageHelper.startPage(pageNum, pageSize, true).doSelectPage(() -> stockKeepingUnitMapper.selectByProductId(productId));
    }

    @Override
    @Transactional
    public StockKeepingUnit initInventory(InventoryInitForm form) {
        if (productMapper.selectById(form.getProductId()) == null) {
            return null;
        }

        StockKeepingUnit stockKeepingUnit = new StockKeepingUnit();
        stockKeepingUnit.setProductId(form.getProductId());
        stockKeepingUnit.setSerialNumber(form.getSkuSerialNumber());
        stockKeepingUnit.setQuantity(form.getQuantity());
        stockKeepingUnit.setListPrice(new BigDecimal(form.getListPrice()));
        stockKeepingUnit.setPrice(new BigDecimal(form.getPrice()));
        stockKeepingUnit.setCreateTime(ZonedDateTime.now());
        stockKeepingUnit.setUpdateTime(ZonedDateTime.now());
        stockKeepingUnit.setFrozen(true);

        stockKeepingUnitMapper.insert(stockKeepingUnit);
        return stockKeepingUnit;
    }

    @Override
    public void toggleFrozenStatus(long id) {
        StockKeepingUnit stockKeepingUnit = stockKeepingUnitMapper.selectByPrimaryKey(id);
        if (stockKeepingUnit != null) {
            stockKeepingUnit.setFrozen(!stockKeepingUnit.isFrozen());
            stockKeepingUnit.setUpdateTime(ZonedDateTime.now());
            stockKeepingUnitMapper.updateByPrimaryKey(stockKeepingUnit);
        }
    }

    @Override
    public StockKeepingUnit getStockKeepingUnitById(long id) {
        return stockKeepingUnitMapper.selectByPrimaryKey(id);
    }

    @Override
    public void updateInventoryFormById(InventoryForm form, long id) {
        StockKeepingUnit stockKeepingUnit = stockKeepingUnitMapper.selectByPrimaryKey(id);
        if (stockKeepingUnit == null) {
            return;
        }
        stockKeepingUnit.setSerialNumber(form.getExtSku());
        stockKeepingUnit.setQuantity(form.getQuantity());
        stockKeepingUnit.setListPrice(new BigDecimal(form.getListPrice()));
        stockKeepingUnit.setPrice(new BigDecimal(form.getPrice()));
        stockKeepingUnit.setUpdateTime(ZonedDateTime.now());
        stockKeepingUnitMapper.updateByPrimaryKey(stockKeepingUnit);
    }

    @Override
    @Transactional
    public void deleteStockKeepingUnitById(long id) {
        stockKeepingUnitMapper.deleteByPrimaryKey(id);
        specificationMapper.deleteBySkuId(id);
    }

    @Override
    public StockKeepingUnit getBySerialNumberAndProductId(String serialNumber, long productId) {
        return stockKeepingUnitMapper.selectBySeriesNumberAndProductId(serialNumber, productId);
    }
}
