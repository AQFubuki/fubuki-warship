package com.fubuki.warship.service;

import com.fubuki.warship.model.request.CreateOrderReq;
import com.fubuki.warship.model.vo.OrderVO;
import com.github.pagehelper.PageInfo;

public interface OrderService {
   

    String create(CreateOrderReq createOrderReq);

    OrderVO detail(String orderNo);

    PageInfo list(Integer pageNum, Integer pageSize);

    void cancel(String orderNo);

    String qrcode(String orderNo);

    PageInfo listForAdmin(Integer pageNum, Integer pageSize);

    void deliver(String orderNo);

    void finish(String orderNo);

    void pay(String orderNo);
}
