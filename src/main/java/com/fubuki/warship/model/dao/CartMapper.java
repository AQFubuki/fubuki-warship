package com.fubuki.warship.model.dao;

import com.fubuki.warship.model.pojo.Cart;
import com.fubuki.warship.model.vo.CartVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Cart row);

    int insertSelective(Cart row);

    Cart selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Cart row);

    int updateByPrimaryKey(Cart row);
    List<Cart> selectByUserId(@Param("userId") Long userId);
    List<CartVO> selectCartVOList(@Param("userId") Long userId);

    Cart selectCartByUserIdAndProductId(@Param("userId") Long userId
            , @Param("productId")Long productId);

    int selectOrNot(@Param("userId") Long userId
            , @Param("productId")Long productId
   , @Param("selected") Integer selected);
}