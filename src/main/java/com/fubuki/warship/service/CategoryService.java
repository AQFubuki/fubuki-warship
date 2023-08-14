package com.fubuki.warship.service;

import com.fubuki.warship.model.request.AddCategoryReq;
import com.fubuki.warship.model.request.UpdateCategoryReq;

public interface CategoryService {
    void addCategory(AddCategoryReq addCategoryReq);

    void update(UpdateCategoryReq updateCategoryReq);
}
