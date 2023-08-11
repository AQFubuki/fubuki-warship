package com.fubuki.warship.model.dao;

import com.fubuki.warship.model.pojo.Order;

public interface OrderMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Order row);

    int insertSelective(Order row);

    Order selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Order row);

    int updateByPrimaryKey(Order row);
}