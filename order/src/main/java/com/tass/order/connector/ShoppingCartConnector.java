package com.tass.order.connector;

import com.tass.common.model.dto.shopping.ShoppingCartDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@FeignClient(value = "shopingcart-service" , url ="${tass.shoppingcart.address}")

public interface ShoppingCartConnector {
    @GetMapping("/{id}")
    Optional<ShoppingCartDTO> getShoppingCartById(@PathVariable Long id);
}
