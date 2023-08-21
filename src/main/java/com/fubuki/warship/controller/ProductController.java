package com.fubuki.warship.controller;

import com.fubuki.warship.common.ApiRestResponse;
import com.fubuki.warship.model.pojo.Product;
import com.fubuki.warship.model.request.ProductListReq;
import com.fubuki.warship.service.ProductService;
import com.github.pagehelper.PageInfo;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 描述：     前台商品Controller
 */
@RestController
public class ProductController {


    private final ProductService productService;
    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }


    @Operation(summary = "商品详情")
    @GetMapping("product/detail")
    public ApiRestResponse detail(@RequestParam Long id) {
        Product product = productService.detail(id);
        return ApiRestResponse.success(product);
    }

    @Operation(summary = "商品列表")
    @GetMapping("product/list")
    public ApiRestResponse list(ProductListReq productListReq) {
        PageInfo list = productService.list(productListReq);
        return ApiRestResponse.success(list);
    }
}
