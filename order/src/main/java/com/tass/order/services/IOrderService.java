package com.tass.order.services;

import com.tass.common.model.BaseResponseV2;
import com.tass.order.entities.Order;
import org.springframework.stereotype.Repository;

@Repository
public interface IOrderService {
    BaseResponseV2<Order> placeOrder(Long orderId);
}
