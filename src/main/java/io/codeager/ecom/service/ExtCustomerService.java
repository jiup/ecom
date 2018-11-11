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
import com.codeager.ecom.domain.ExtCustomer;
import com.codeager.ecom.dto.form.ExtCustomerForm;

/**
 * @author Jiupeng Zhang
 * @since 01/11/2018
 */
public interface ExtCustomerService {
    void unsubscribeById(long id);
    void delete(long id);
    ExtCustomer get(long id);
    void add(ExtCustomerForm form);
    void updateById(ExtCustomerForm form, long id);
    Page<ExtCustomer> getAll(int pageNum, int pageSize);
}
