package com.fubuki.warship.model.dao;

import com.fubuki.warship.model.pojo.Cart;

public interface CartMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Cart row);

    int insertSelective(Cart row);

    Cart selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Cart row);

    int updateByPrimaryKey(Cart row);
}