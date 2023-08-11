package com.fubuki.warship.model.dao;

import com.fubuki.warship.model.pojo.Product;

public interface ProductMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Product row);

    int insertSelective(Product row);

    Product selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Product row);

    int updateByPrimaryKey(Product row);
}