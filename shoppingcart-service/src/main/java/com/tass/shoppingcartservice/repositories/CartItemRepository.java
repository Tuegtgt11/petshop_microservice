package com.tass.shoppingcartservice.repositories;

import com.tass.shoppingcartservice.entities.CartItem;
import com.tass.shoppingcartservice.entities.CartItemId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CartItemRepository extends JpaRepository<CartItem, CartItemId> {
    List<CartItem> findAllByShoppingCartId(Long shoppingCartId);

    void deleteAllByShoppingCartId(Long shoppingCartId);


}
