package com.tass.payment.listener;

import com.tass.common.model.dto.order.OrderDTO;
import com.tass.common.utils.JsonHelper;
import com.tass.payment.services.PaymentService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class RedisMessageSubscriber implements MessageListener {

    @Autowired
    PaymentService paymentService;
    @Override
    public void onMessage(Message message, byte[] pattern) {
        String msg = message.toString();

        OrderDTO orderDTO = JsonHelper.getObject(msg , OrderDTO.class);

        paymentService.handleEventOrder(orderDTO);

        log.info("PAYMENT-SERVICE order event created {}" , msg);
    }
}
