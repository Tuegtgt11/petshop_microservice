package com.tass.order.services;

import com.tass.common.model.dto.shopping.ShoppingCartDTO;
import com.tass.common.utils.JsonHelper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class RedisMessageSubscriber implements MessageListener {

    @Autowired
    OrderService orderService;
    @Override
    public void onMessage(Message message, byte[] pattern) {
        String msg = message.toString();

        ShoppingCartDTO shoppingCartDTO = JsonHelper.getObject(msg , ShoppingCartDTO.class);

//        orderService.handleEventOrder(shoppingCartDTO);

        log.info("PAYMENT-SERVICE order event created {}" , msg);
    }
}
