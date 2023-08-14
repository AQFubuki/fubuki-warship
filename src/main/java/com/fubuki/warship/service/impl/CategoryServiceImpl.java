package com.fubuki.warship.service.impl;

import com.fubuki.warship.exception.WarshipException;
import com.fubuki.warship.exception.WarshipExceptionEnum;
import com.fubuki.warship.model.dao.CategoryMapper;
import com.fubuki.warship.model.pojo.Category;
import com.fubuki.warship.model.request.AddCategoryReq;
import com.fubuki.warship.model.request.UpdateCategoryReq;
import com.fubuki.warship.service.CategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl implements CategoryService {
    public final CategoryMapper categoryMapper;

    @Autowired
    public CategoryServiceImpl(CategoryMapper categoryMapper) {
        this.categoryMapper = categoryMapper;
    }

    @Override
    public void addCategory(AddCategoryReq addCategoryReq) {
        Category category = new Category();
        BeanUtils.copyProperties(addCategoryReq, category);
        Category oddCategory = categoryMapper.selectByName(addCategoryReq.getName());
        if (oddCategory != null) {
            throw new WarshipException(WarshipExceptionEnum.NAME_EXISTED);
        }
        int count = categoryMapper.insertSelective(category);
        if (count == 0) {
            throw new WarshipException(WarshipExceptionEnum.INSERT_FAILED);
        }
    }

    @Override
    public void update(UpdateCategoryReq updateCategoryReq) {
        Category category = new Category();
        BeanUtils.copyProperties(updateCategoryReq, category);
        System.out.println(category);
        System.out.println(updateCategoryReq);
        if (updateCategoryReq.getName() != null) {
            Category oddCategory = categoryMapper.selectByName(updateCategoryReq.getName());
            //名称相同而ID不同，则否决修改
            if (oddCategory != null && !oddCategory.getId().equals(category.getId())) {
                throw new WarshipException(WarshipExceptionEnum.NAME_EXISTED);
            }
        }
        int count = categoryMapper.updateByPrimaryKeySelective(category);
        if (count == 0) {
            throw new WarshipException(WarshipExceptionEnum.UPDATE_FAILED);
        }
    }
}
