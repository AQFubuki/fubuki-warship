package com.fubuki.warship.model.dao;

import com.fubuki.warship.model.pojo.OrderItem;
import com.fubuki.warship.model.vo.CartVO;
import com.fubuki.warship.model.vo.OrderItemVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemMapper {
    int deleteByPrimaryKey(Long id);

    int insert(OrderItem row);

    int insertSelective(OrderItem row);

    OrderItem selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(OrderItem row);

    int updateByPrimaryKey(OrderItem row);
    List<OrderItemVO> selectOrderItemVOList(@Param("orderNo") String orderNo);
}