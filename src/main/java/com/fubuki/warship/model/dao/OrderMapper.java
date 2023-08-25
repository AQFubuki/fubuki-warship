package com.fubuki.warship.model.dao;

import com.fubuki.warship.model.pojo.Order;
import com.fubuki.warship.model.vo.OrderItemVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Order row);

    int insertSelective(Order row);

    Order selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Order row);

    int updateByPrimaryKey(Order row);

    Order selectByOrderNo(@Param("orderNo")String orderNo);

    List<Order> selectByUserId(@Param("userId")Long userId);

    List<Order> selectAllForAdmin();
}