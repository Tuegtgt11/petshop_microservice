package com.tass.shoppingcartservice.controllers;

import com.tass.common.model.ApplicationException;
import com.tass.common.model.BaseResponseV2;
import com.tass.common.model.userauthen.UserDTO;
import com.tass.shoppingcartservice.entities.ShoppingCart;
import com.tass.shoppingcartservice.repositories.ShoppingCartRepository;
import com.tass.shoppingcartservice.services.BaseService;
import com.tass.shoppingcartservice.services.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;

@RestController
@RequestMapping("/shopping")

public class ShoppingCartController extends BaseService {
    @Autowired
    ShoppingCartService shoppingCartService;

    @Autowired
    ShoppingCartRepository shoppingCartRepository;

    @RequestMapping(method = RequestMethod.POST, path = "/add")
    public BaseResponseV2 addShoppingCart(@Param("productId") long productId, @Param("quantity") int quantity)throws ApplicationException {
        UserDTO userDTO = getUserDTO();

        BaseResponseV2 shoppingCart = shoppingCartService.addToShoppingCart(userDTO.getUserId(), productId, quantity);
        return  new BaseResponseV2(shoppingCart);
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> findAll(Principal principal) throws ApplicationException{
        UserDTO userDTO = getUserDTO();
        return ResponseEntity.ok(shoppingCartService.getAllCart(userDTO.getUserId()));
    }

    @RequestMapping(method = RequestMethod.PUT, path = "/delete")
    public ResponseEntity<?> deleteCartItem(@Param("shoppingCatId") long shoppingCatId, @Param("productId") long productId, Principal principal){
        UserDTO userDTO = getUserDTO();
        ShoppingCart shoppingCart = shoppingCartService.deleteCartItem(shoppingCatId, productId);
        if (shoppingCart==null){
            return ResponseEntity.badRequest().body("delete fail");
        }
        return ResponseEntity.ok(shoppingCart) ;
    }

    @RequestMapping(method = RequestMethod.PUT, path = "/update")
    public ResponseEntity<?> updateCart(@Param("shoppingCatId") long shoppingCatId, @Param("productId") long productId, @Param("quantity") int quantity){
        ShoppingCart shoppingCart = shoppingCartService.updateQuantity(shoppingCatId, productId, quantity);
        if (shoppingCart==null){
            return ResponseEntity.badRequest().body("update fail");
        }
        return ResponseEntity.ok(shoppingCart);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/{id}")
    public Optional<ShoppingCart> findProduct(@PathVariable Long id) throws ApplicationException {
        return shoppingCartRepository.findById(id);
    }
}
