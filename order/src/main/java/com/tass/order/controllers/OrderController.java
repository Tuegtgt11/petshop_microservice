package com.tass.order.controllers;

import com.tass.common.model.BaseResponseV2;
import com.tass.common.myenums.OrderStatus;
import com.tass.order.connector.ShoppingCartConnector;
import com.tass.order.entities.Order;
import com.tass.order.entities.OrderDetail;
import com.tass.order.services.BaseService;
import com.tass.order.services.OrderDetailService;
import com.tass.order.services.OrderService;
import com.tass.order.spec.User.Specifications;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/order")
public class OrderController extends BaseService {

    @Autowired
    private OrderService orderService;


    @Autowired
    private OrderDetailService orderDetailService;

    @Autowired
    ShoppingCartConnector shoppingCartConnector;




    @RequestMapping(method = RequestMethod.POST, path = "/checkout")
    public BaseResponseV2<?> placeOrder(@RequestParam long shoppingCartId) {

        if (shoppingCartId > 0) {
            return new BaseResponseV2<>(orderService.placeOrder(shoppingCartId));
//                BaseResponseV2<Order> result = orderService.handleEventOrder(shoppingCartId);
        } else {
            return new BaseResponseV2<>("Fails");
        }
    }

    @RequestMapping( method = RequestMethod.POST,path = "/getmyorder")
    public Page<Order> searchOrder(
            @RequestParam(value = "status", required = false) OrderStatus status,
            @RequestParam(value = "userId", required = false) long userId,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "limit", defaultValue = "10") int limit
    ) {
        Specification<Order> specification = Specifications.OrderSpec(status,userId,page,limit);
        return orderService.searchAllForAdmin(specification,page,limit);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> findByOrderId(@PathVariable Long id) {
        List<OrderDetail> order = orderDetailService.findByOrder(id);
        return ResponseEntity.ok(order);
    }
}
