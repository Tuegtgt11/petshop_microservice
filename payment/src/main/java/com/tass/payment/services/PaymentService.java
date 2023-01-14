package com.tass.payment.services;

import com.tass.common.model.constans.ORDER;
import com.tass.common.model.dto.order.OrderDTO;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {
    public void handleEventOrder(OrderDTO orderDTO){
            // handle event order

        if (orderDTO.getStatus() == ORDER.STATUS.CREATED){
            this.handleOrderEventCreated(orderDTO);
            return;
        }

        if (orderDTO.getStatus() == ORDER.STATUS.SUCCESS){
            this.handleOrderEventSuccess(orderDTO);
            return;
        }
        if (orderDTO.getStatus() == ORDER.STATUS.FAIL){
            this.handleOrderEventFail(orderDTO);
            return;
        }
    }

    public void handleOrderEventCreated(OrderDTO orderDTO){}
    public void handleOrderEventSuccess(OrderDTO orderDTO){}
    public void handleOrderEventFail(OrderDTO orderDTO){


        // to do

        // send message nguoc toppic order -handle
    }
}
