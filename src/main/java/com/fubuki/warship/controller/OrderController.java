package com.fubuki.warship.controller;

import com.fubuki.warship.common.ApiRestResponse;
import com.fubuki.warship.filter.UserFilter;
import com.fubuki.warship.model.request.CreateOrderReq;
import com.fubuki.warship.model.request.ProductListReq;
import com.fubuki.warship.model.vo.CartVO;
import com.fubuki.warship.model.vo.OrderVO;
import com.fubuki.warship.service.OrderService;
import com.github.pagehelper.PageInfo;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
//@RequestMapping("/order")
public class OrderController {
    private final OrderService orderService;
@Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }
    @PostMapping("/order/create")
    @Operation(summary = "创建订单")
    public ApiRestResponse create(@RequestBody @Valid CreateOrderReq createOrderReq) {
        String orderNo = orderService.create(createOrderReq);
        return ApiRestResponse.success(orderNo);
    }
    @GetMapping("/order/detail")
    @Operation(summary = "订单详情")
    public ApiRestResponse detail(@RequestParam String orderNo) {
        OrderVO orderVO=orderService.detail(orderNo);
        return ApiRestResponse.success(orderVO);
    }
    @Operation(summary = "订单列表")
    @GetMapping("/order/list")
    public ApiRestResponse list(@RequestParam Integer pageNum
    ,@RequestParam Integer pageSize) {
        PageInfo list = orderService.list(pageNum,pageSize);
        return ApiRestResponse.success(list);
    }
}
