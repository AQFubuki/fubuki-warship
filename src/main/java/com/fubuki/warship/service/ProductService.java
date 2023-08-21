package com.fubuki.warship.service;

import com.fubuki.warship.model.pojo.Product;
import com.fubuki.warship.model.request.AddProductReq;
import com.fubuki.warship.model.request.ProductListReq;
import com.fubuki.warship.model.request.UpdateProductReq;
import com.github.pagehelper.PageInfo;

public interface ProductService {
    void add(AddProductReq addProductReq);

    void update(UpdateProductReq updateProductReq);

    void delete(Long id);

    void batchUpdateSellStatus(Long[] ids, Integer sellStatus);

    PageInfo listForAdmin(Integer pageNum, Integer pageSize);

    Product detail(Long id);

    PageInfo list(ProductListReq productListReq);
}
