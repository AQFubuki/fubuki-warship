package com.fubuki.warship.service;

import com.fubuki.warship.model.request.CreateOrderReq;

public interface OrderService {
    String create(CreateOrderReq createOrderReq);
}
