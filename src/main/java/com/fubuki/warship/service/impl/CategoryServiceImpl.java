package com.fubuki.warship.service.impl;

import com.fubuki.warship.exception.WarshipException;
import com.fubuki.warship.exception.WarshipExceptionEnum;
import com.fubuki.warship.model.dao.CategoryMapper;
import com.fubuki.warship.model.pojo.Category;
import com.fubuki.warship.model.request.AddCategoryReq;
import com.fubuki.warship.model.request.UpdateCategoryReq;
import com.fubuki.warship.model.vo.CategoryVO;
import com.fubuki.warship.service.CategoryService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

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

    @Override
    public void delete(Long id) {
        Category categoryOld=categoryMapper.selectByPrimaryKey(id);
        if(categoryOld==null){
            throw new WarshipException(WarshipExceptionEnum.DELETE_FAILED);
        }
        int count = categoryMapper.deleteByPrimaryKey(id);
        if (count == 0) {
            throw new WarshipException(WarshipExceptionEnum.DELETE_FAILED);
        }
    }

    @Override
    public PageInfo listForAdmin(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize, "type, parentId, order_num, id");
        List<Category> categoryList = categoryMapper.selectList();
        PageInfo pageInfo = new PageInfo(categoryList);
        return pageInfo;
    }

    @Override
    //@Cacheable(value = "listCategoryForCustomer")
    public List<CategoryVO> listCategoryForCustomer() {
        ArrayList<CategoryVO> categoryVOList = new ArrayList<>();
        recursivelyFindCategories(categoryVOList, 0l);
        return categoryVOList;
    }

    private void recursivelyFindCategories(List<CategoryVO> categoryVOList, Long parentId) {
        //递归获取所有子类别，并组合成为一个“目录树”
        List<Category> categoryList = categoryMapper.selectCategoriesByParentId(parentId);
//        1：刚创建出来的List长度为0，但不为null
//        2：CollectionUtils.isEmpty()方法可以检查null和长度为0的list
        if (!CollectionUtils.isEmpty(categoryList)) {
            for (int i = 0; i < categoryList.size(); i++) {
                Category category = categoryList.get(i);
                CategoryVO categoryVO = new CategoryVO();
                BeanUtils.copyProperties(category, categoryVO);
                categoryVOList.add(categoryVO);
                recursivelyFindCategories(categoryVO.getChildCategory(),
                        categoryVO.getId());
            }
        }
    }
}
