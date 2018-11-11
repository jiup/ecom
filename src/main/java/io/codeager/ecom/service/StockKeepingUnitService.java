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

package com.codeager.ecom.service;

import com.github.pagehelper.Page;
import com.codeager.ecom.domain.StockKeepingUnit;
import com.codeager.ecom.dto.form.InventoryForm;
import com.codeager.ecom.dto.form.InventoryInitForm;

/**
 * @author Jiupeng Zhang
 * @since 03/03/2018
 */
public interface StockKeepingUnitService {
    Page<StockKeepingUnit> getStockKeepingUnits(int pageNum, int pageSize);
    Page<StockKeepingUnit> getStockKeepingUnitsByProductId(int pageNum, int pageSize, long productId);
    StockKeepingUnit initInventory(InventoryInitForm form);
    void toggleFrozenStatus(long id);
    StockKeepingUnit getStockKeepingUnitById(long id);
    void updateInventoryFormById(InventoryForm form, long id);
    void deleteStockKeepingUnitById(long id);
    StockKeepingUnit getBySerialNumberAndProductId(String serialNumber, long productId);
}
