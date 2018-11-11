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
import io.codeager.ecom.dao.mapper.SpecificationMapper;
import io.codeager.ecom.domain.Specification;
import io.codeager.ecom.dto.form.SpecificationInitForm;
import io.codeager.ecom.service.SpecificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.Set;

/**
 * @author Jiupeng Zhang
 * @since 03/05/2018
 */
@Service
public class SpecificationServiceImpl implements SpecificationService {
    private SpecificationMapper specificationMapper;

    @Autowired
    public void setSpecificationMapper(SpecificationMapper specificationMapper) {
        this.specificationMapper = specificationMapper;
    }

    @Override
    public Page<Specification> getSpecificationsBySkuId(int pageNum, int pageSize, long skuId) {
        return PageHelper.startPage(pageNum, pageSize, true).doSelectPage(() -> specificationMapper.selectBySkuId(skuId));
    }

    @Override
    public Specification getSpecificationBySkuIdAndName(long skuId, String name) {
        return specificationMapper.selectBySkuIdAndAttrName(skuId, name);
    }

    @Override
    public Set<Specification> getDistinctSpecificationsByCategoryId(long categoryId) {
        return specificationMapper.selectDistinctNameAndUomAndGroupStatusByCategoryId(categoryId);
    }

    @Override
    @Transactional
    public Specification addSpecification(int categoryId, long skuId, SpecificationInitForm form) {
        Specification specification = new Specification();
        specification.setCategoryId(categoryId);
        specification.setSkuId(skuId);
        specification.setName(form.getName());
        specification.setValue(form.getValue());
        specification.setUnitsOfMeasurement(form.getUom());
        specification.setCreateTime(ZonedDateTime.now());

        Specification specification0 = specificationMapper.selectOneByCategoryIdAndAttrName(categoryId, form.getName());
        if (specification0 != null) {
            specification.setName(specification0.getName());
            specification.setUnitsOfMeasurement(specification0.getUnitsOfMeasurement());
            specification.setGrouped(specification0.isGrouped());
        }

        specificationMapper.insert(specification);
        return specification;
    }

    @Override
    public void deleteById(long id) {
        specificationMapper.deleteByPrimaryKey(id);
    }

    @Override
    @Transactional
    public void updateCategoryIdAndGroupStatusBySkuId(int categoryId, long skuId) {
        for (Specification specification : specificationMapper.selectBySkuId(skuId)) {
            Specification tmp = specificationMapper.selectOneByCategoryIdAndAttrName(categoryId, specification.getName());
            if (tmp != null) {
                specification.setUnitsOfMeasurement(tmp.getUnitsOfMeasurement());
                specification.setGrouped(tmp.isGrouped());
                specificationMapper.updateByPrimaryKey(specification);
            } else {
                specification.setGrouped(false);
                specificationMapper.updateByPrimaryKey(specification);
            }
        }
        specificationMapper.updateCategoryIdsBySkuId(categoryId, skuId);
    }

    @Override
    public void flipGroupStatus(int categoryId, String attrName) {
        specificationMapper.flipGroupStatusByAttrNameAndCategoryId(attrName, categoryId);
    }
}
