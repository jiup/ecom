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
import io.codeager.ecom.dao.mapper.ExtCustomerMapper;
import io.codeager.ecom.domain.ExtCustomer;
import io.codeager.ecom.dto.form.ExtCustomerForm;
import io.codeager.ecom.service.ExtCustomerService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;

/**
 * @author Jiupeng Zhang
 * @since 01/11/2018
 */
@Service
public class ExtCustomerServiceImpl implements ExtCustomerService {
    private ExtCustomerMapper extCustomerMapper;

    @Autowired
    public void setExtCustomerMapper(ExtCustomerMapper extCustomerMapper) {
        this.extCustomerMapper = extCustomerMapper;
    }

    @Override
    public void unsubscribeById(long id) {
        extCustomerMapper.unsubscribeById(id);
    }

    @Override
    public void delete(long id) {
        extCustomerMapper.softDeleteById(id);
    }

    @Override
    public ExtCustomer get(long id) {
        return extCustomerMapper.selectById(id);
    }

    @Override
    public void add(ExtCustomerForm form) {
        ExtCustomer extCustomer = new ExtCustomer();
        BeanUtils.copyProperties(form, extCustomer);
        extCustomer.setUpdateTime(ZonedDateTime.now());
        extCustomerMapper.insert(extCustomer);
    }

    @Override
    public void updateById(ExtCustomerForm form, long id) {
        ExtCustomer extCustomer = new ExtCustomer();
        BeanUtils.copyProperties(form, extCustomer);
        extCustomer.setId(id);
        extCustomer.setUpdateTime(ZonedDateTime.now());
        extCustomerMapper.updateById(extCustomer);
    }

    @Override
    public Page<ExtCustomer> getAll(int pageNum, int pageSize) {
        return PageHelper.startPage(pageNum, pageSize, true).doSelectPage(() -> extCustomerMapper.selectAll());
    }
}
