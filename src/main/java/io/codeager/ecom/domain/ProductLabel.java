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

package com.codeager.ecom.domain;

import com.google.common.base.MoreObjects;
import com.google.gson.annotations.Expose;

/**
 * @author Jiupeng Zhang
 * @since 02/25/2018
 */
public class ProductLabel {
    @Expose
    private long id;
    @Expose
    private String name;
    @Expose
    private String overview;
    @Expose
    private String mainImage;
    @Expose
    private String[] subImages;

    public long getId() {
        return id;
    }

    public ProductLabel setId(long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public ProductLabel setName(String name) {
        this.name = name;
        return this;
    }

    public String getOverview() {
        return overview;
    }

    public ProductLabel setOverview(String overview) {
        this.overview = overview;
        return this;
    }

    public String getMainImage() {
        return mainImage;
    }

    public ProductLabel setMainImage(String mainImage) {
        this.mainImage = mainImage;
        return this;
    }

    public String[] getSubImages() {
        return subImages;
    }

    public ProductLabel setSubImages(String[] subImages) {
        this.subImages = subImages;
        return this;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("name", name)
                .add("overview", overview)
                .add("mainImage", mainImage)
                .add("subImages", subImages)
                .toString();
    }
}
