package com.fubuki.warship.model.query;

import java.util.List;

/**
 * 描述：     查询商品列表的Query
 */
public class ProductListQuery {

    private String keyword;

    private List<Long> categoryIds;

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public List<Long> getCategoryIds() {
        return categoryIds;
    }

    public void setCategoryIds(List<Long> categoryIds) {
        this.categoryIds = categoryIds;
    }
}
