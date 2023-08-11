package com.fubuki.warship.model.dao;

import com.fubuki.warship.model.pojo.Category;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Category row);

    int insertSelective(Category row);

    Category selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Category row);

    int updateByPrimaryKey(Category row);
}