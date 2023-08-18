package com.fubuki.warship.service;

import com.fubuki.warship.model.request.AddProductReq;
import com.fubuki.warship.model.request.UpdateProductReq;

public interface ProductService {
    void add(AddProductReq addProductReq);

    void update(UpdateProductReq updateProductReq);

    void delete(Long id);

    void batchUpdateSellStatus(Long[] ids, Integer sellStatus);
}
