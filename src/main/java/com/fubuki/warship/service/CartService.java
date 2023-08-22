package com.fubuki.warship.service;

import com.fubuki.warship.model.vo.CartVO;

import java.util.List;

public interface CartService {
    List<CartVO> list(Long userId);

    List<CartVO> add(Long userId, Long productId, Integer count);

    List<CartVO> update(Long userId, Long productId, Integer count);

    List<CartVO> delete(Long userId, Long productId);

    List<CartVO> select(Long userId, Long productId, Integer selected);

    List<CartVO> selectAll(Long userId, Integer selected);

    List<CartVO> selectOrNot(Long userId, Long productId, Integer selected);

    List<CartVO> selectAllOrNot(Long userId, Integer selected);
}
