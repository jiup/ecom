/*
 * Copyright 2017 Jiupeng Zhang
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

import io.codeager.ecom.dto.view.$;
import io.codeager.ecom.dto.view.RegularRestResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import static io.codeager.ecom.util.Routing.*;

/**
 * @author Jiupeng Zhang
 * @since 12/22/2017
 */
@RestController("basicController")
@RequestMapping(API_BASE)
public class BasicController {
    @GetMapping({INDEX, HOME})
    @ApiIgnore
    public ResponseEntity indexView() {
        return $.restful(RegularRestResponse.WELCOME).asResponseEntity();
    }
}
