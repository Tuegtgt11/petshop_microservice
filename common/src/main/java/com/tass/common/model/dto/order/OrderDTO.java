package com.tass.common.model.dto.order;

import lombok.Data;

import java.io.Serializable;

@Data
public class OrderDTO implements Serializable {
    private long shoppingCartId;
    private long productId;
    private int qty;

    public OrderDTO(){

    }
}
