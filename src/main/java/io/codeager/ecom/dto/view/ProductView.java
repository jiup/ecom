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

package io.codeager.ecom.dto.view;

import com.google.common.base.MoreObjects;
import com.google.gson.annotations.Expose;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Jiupeng Zhang
 * @since 02/25/2018
 */
public class ProductView {
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
    @Expose
    private AttributeItem[] attrs;

    public long getId() {
        return id;
    }

    public ProductView setId(long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public ProductView setName(String name) {
        this.name = name;
        return this;
    }

    public String getOverview() {
        return overview;
    }

    public ProductView setOverview(String overview) {
        this.overview = overview;
        return this;
    }

    public String getMainImage() {
        return mainImage;
    }

    public ProductView setMainImage(String mainImage) {
        this.mainImage = mainImage;
        return this;
    }

    public String[] getSubImages() {
        return subImages;
    }

    public ProductView setSubImages(String[] subImages) {
        this.subImages = subImages;
        return this;
    }

    public AttributeItem[] getAttrs() {
        return attrs;
    }

    public ProductView setAttrs(AttributeItem[] attrs) {
        this.attrs = attrs;
        return this;
    }

    /**
     * Example:
     * <p>
     * #key_0
     * value_line_0
     * value_line_1
     * <p>
     * #key_1
     * value_line_0
     * <p>
     * AttributeItem{name=key_0, value=value_line_0+\n+value_line_1}
     * AttributeItem{name=key_1, value=value_line_0}
     */
    public ProductView setAttrs(String src) {
        List<AttributeItem> attributeItems = new ArrayList<>();
        if (src == null) {
            attrs = new AttributeItem[]{};
            return this;
        }

        StringBuilder builder;
        String[] lines = src.split("\n");
        for (int i = 0; i < lines.length; ) {
            if (lines[i].startsWith("#")) {
                AttributeItem item = new AttributeItem();
                item.setName(lines[i].substring(1));
                builder = new StringBuilder();
                for (i++; i < lines.length; ) {
                    if (!lines[i].startsWith("#")) {
                        builder.append(lines[i]).append('\n');
                        i++;
                    } else {
                        break;
                    }
                }
                item.setValue(StringUtils.removeEnd(builder.toString(), "\n"));
                attributeItems.add(item);
            } else {
                i++;
            }
        }
        attrs = attributeItems.toArray(new AttributeItem[]{});
        return this;
    }

    static class AttributeItem implements Serializable {
        @Expose
        private String name;
        @Expose
        private String value;

        public AttributeItem setName(String name) {
            this.name = name;
            return this;
        }

        public AttributeItem setValue(String value) {
            this.value = value;
            return this;
        }

        public String getName() {
            return name;
        }

        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return MoreObjects.toStringHelper(this)
                    .add("name", name)
                    .add("value", value)
                    .toString();
        }
    }
}
