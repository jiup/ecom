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
import io.codeager.ecom.domain.Specification;
import io.codeager.ecom.dto.form.SpecificationInitForm;

import java.util.Set;

/**
 * @author Jiupeng Zhang
 * @since 03/05/2018
 */
public interface SpecificationService {
    Page<Specification> getSpecificationsBySkuId(int pageNum, int pageSize, long skuId);
    Specification getSpecificationBySkuIdAndName(long skuId, String name);
    Set<Specification> getDistinctSpecificationsByCategoryId(long categoryId);
    Specification addSpecification(int categoryId, long skuId, SpecificationInitForm form);
    void deleteById(long id);
    void updateCategoryIdAndGroupStatusBySkuId(int categoryId, long skuId);
    void flipGroupStatus(int categoryId, String attrName);
}
