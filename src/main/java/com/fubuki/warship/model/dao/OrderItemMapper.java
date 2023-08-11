package com.fubuki.warship.model.dao;

import com.fubuki.warship.model.pojo.OrderItem;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemMapper {
    int deleteByPrimaryKey(Long id);

    int insert(OrderItem row);

    int insertSelective(OrderItem row);

    OrderItem selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(OrderItem row);

    int updateByPrimaryKey(OrderItem row);
}