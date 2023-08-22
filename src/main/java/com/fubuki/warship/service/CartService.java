package com.fubuki.warship.service;

import com.fubuki.warship.model.vo.CartVO;

import java.util.List;

public interface CartService {
    List<CartVO> list(Long userId);

    List<CartVO> add(Long userId, Long productId, Integer count);

}
