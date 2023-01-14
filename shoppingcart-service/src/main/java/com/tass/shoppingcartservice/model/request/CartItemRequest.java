package com.tass.shoppingcartservice.model.request;

import com.tass.shoppingcartservice.entities.ShoppingCart;
import lombok.Data;

@Data
public class CartItemRequest {

    private int quantity;
    private ShoppingCart shoppingCart;
    private Long product;
}
