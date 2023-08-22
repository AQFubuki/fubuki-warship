package com.fubuki.warship.controller;

import com.fubuki.warship.common.ApiRestResponse;
import com.fubuki.warship.filter.UserFilter;
import com.fubuki.warship.model.vo.CartVO;
import com.fubuki.warship.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cart")
public class CartController {
    private final CartService cartService;
    @Autowired
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/list")
    @Operation(summary = "购物车列表")
    public ApiRestResponse list() {
        //内部获取用户ID，防止横向越权
        List<CartVO> cartList = cartService.list(UserFilter.currentUser.getId());
        return ApiRestResponse.success(cartList);
    }
    @PostMapping("/add")
    @Operation(summary = "添加商品到购物车")
    public ApiRestResponse add(@RequestParam Long productId
            , @RequestParam Integer count) {
        List<CartVO> cartVOList = cartService.add(UserFilter.currentUser.getId(), productId, count);
        return ApiRestResponse.success(cartVOList);
    }
    @PostMapping("/update")
    @Operation(summary = "更新商品数量")
    public ApiRestResponse update(@RequestParam Long productId
            , @RequestParam Integer count) {
        List<CartVO> cartVOList = cartService.update(UserFilter.currentUser.getId(), productId, count);
        return ApiRestResponse.success(cartVOList);
    }
    @PostMapping("/delete")
    @Operation(summary = "删除购物车商品")
    public ApiRestResponse delete(@RequestParam Long productId) {
        List<CartVO> cartVOList = cartService.delete(UserFilter.currentUser.getId(), productId);
        return ApiRestResponse.success(cartVOList);
    }
    @PostMapping("/select")
    @Operation(summary = "选中/不选中某个商品")
    public ApiRestResponse select(@RequestParam Long productId,
                                  @RequestParam Integer selected) {
        //List<CartVO> cartVOList = cartService.select(UserFilter.currentUser.getId(), productId,selected);
        List<CartVO> cartVOList = cartService.selectOrNot(UserFilter.currentUser.getId(), productId, selected);
        return ApiRestResponse.success(cartVOList);
    }
    @PostMapping("/selectAll")
    @Operation(summary = "全选中/不选中某个商品")
    public ApiRestResponse selectAll(@RequestParam Integer selected) {
        //List<CartVO> cartVOList = cartService.selectAll(UserFilter.currentUser.getId(),selected);
        List<CartVO> cartVOList = cartService.selectAllOrNot(UserFilter.currentUser.getId(), selected);
        return ApiRestResponse.success(cartVOList);
    }
}
