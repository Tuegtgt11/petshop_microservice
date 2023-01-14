package com.tass.shoppingcartservice.services;

import com.tass.common.model.ApplicationException;
import com.tass.common.model.BaseResponseV2;
import com.tass.common.model.ERROR;
import com.tass.common.model.dto.product.ProductDTO;
import com.tass.common.model.dto.shopping.CartItemDTO;
import com.tass.common.model.dto.shopping.CartItemIdDTO;
import com.tass.common.model.dto.shopping.ShoppingCartDTO;
import com.tass.common.model.userauthen.UserDTO;
import com.tass.common.myenums.ProductStatus;
import com.tass.common.myenums.ShoppingCartStatus;
import com.tass.common.utils.JsonHelper;
import com.tass.shoppingcartservice.connector.ProductConnector;
import com.tass.shoppingcartservice.entities.CartItem;
import com.tass.shoppingcartservice.entities.CartItemId;
import com.tass.shoppingcartservice.entities.ShoppingCart;
import com.tass.shoppingcartservice.repositories.CartItemRepository;
import com.tass.shoppingcartservice.repositories.ShoppingCartRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
@RequiredArgsConstructor
@Log4j2
public class ShoppingCartService extends BaseService{

    @Autowired
    ShoppingCartRepository shoppingCartRepository;

    @Autowired
    CartItemRepository cartItemRepository;

    @Autowired
    ProductConnector productConnector;

    @Autowired
    ResdisPusherMessageService resdisPusherMessageService;

    @Autowired
    @Qualifier("shopping")
    ChannelTopic channelTopic;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Value("${spring.rabbitmq.exchange}")
    private String EXCHANGE_NAME;

    @Value("${spring.rabbitmq.routing-key.shopingcart}")
    private String ROUTING_KEY_NAME;


    public BaseResponseV2 addToShoppingCart(Long userId, Long productId, int quantity) throws ApplicationException
    {

        UserDTO userDTO = getUserDTO();


        BaseResponseV2<ProductDTO> productInfoResponse = productConnector.getProductById(productId);

        if (!productInfoResponse.isSuccess()){
            throw new ApplicationException(ERROR.INVALID_PARAM);
        }
        ProductDTO productDTO = productInfoResponse.getData();

        if (productDTO == null){
            throw new ApplicationException(ERROR.INVALID_PARAM);
        }

        if (productDTO.getStatus() != ProductStatus.ACTIVE){

            throw new ApplicationException(ERROR.PRODUCT_NOT_ACTIVE);
        }

        Optional<ShoppingCart> optionalShoppingCart = shoppingCartRepository.findByUserId(userDTO.getUserId());
        if (optionalShoppingCart.isPresent()){
            Set<CartItem> cartItems = optionalShoppingCart.get().getItems();
            boolean exits = false;
            for (CartItem item:cartItems
            ) {
                if (item.getProduct()==(productId)){
                    exits = true;

                    if (item.getQuantity() + quantity >productDTO.getQuantity()){
                        item.setQuantity(productDTO.getQuantity());
                    }
                    if (item.getQuantity() + quantity < productDTO.getQuantity()){
                        item.setQuantity(item.getQuantity() + quantity);
                    }

                    if (item.getQuantity() > productDTO.getQuantity()){
                        item.setQuantity(productDTO.getQuantity());
                    }

                }
            }
            if (!exits){
                CartItem item = new CartItem();
                item.setProduct(productDTO.getId());
                item.setQuantity(productDTO.getQuantity());
                item.setShoppingCart(optionalShoppingCart.get());
                CartItemId cartItemId = new CartItemId(optionalShoppingCart.get().getId(), productId);
                item.setId(cartItemId);
                cartItems.add(item);
            }
            optionalShoppingCart.get().setItems(cartItems);
            optionalShoppingCart.get().setStatus(ShoppingCartStatus.SUCCESS);
//            return new BaseResponseV2<>(shoppingCartRepository.save(optionalShoppingCart.get()));
            shoppingCartRepository.save(optionalShoppingCart.get());

            Set<CartItemDTO> cartItemDTO = new HashSet<>();
            CartItemDTO itemDTO = new CartItemDTO();
            itemDTO.setProduct(productDTO.getId());
            itemDTO.setQuantity(productDTO.getQuantity());
            itemDTO.setShoppingCartId(optionalShoppingCart.get().getId());
            CartItemIdDTO cartItemIdDTO = new CartItemIdDTO(optionalShoppingCart.get().getId(), productDTO.getId());
            itemDTO.setId(cartItemIdDTO);
            cartItemDTO.add(itemDTO);

            ShoppingCartDTO shoppingCartDTO = new ShoppingCartDTO();
            shoppingCartDTO.setId(optionalShoppingCart.get().getId());
            shoppingCartDTO.setUserId(optionalShoppingCart.get().getUserId());
            shoppingCartDTO.setStatus(ShoppingCartStatus.SUCCESS);
            shoppingCartDTO.setItems(cartItemDTO);

//            String message = JsonHelper.toString(shoppingCartDTO);
//            resdisPusherMessageService.sendMessage(message , channelTopic);
//            log.info("message",message);
            rabbitTemplate.convertAndSend(EXCHANGE_NAME,ROUTING_KEY_NAME, shoppingCartDTO);
            return new BaseResponseV2<>();

        } else {
            ShoppingCart shoppingCart = new ShoppingCart();
            Set<CartItem> cartItems = new HashSet<>();
            CartItem cartItem = new CartItem();
            cartItem.setProduct(productDTO.getId());
            if (quantity > productDTO.getQuantity()){
                quantity = productDTO.getQuantity();
            }
            cartItem.setQuantity(quantity);
            cartItems.add(cartItem);
            CartItemId cartItemId = new CartItemId(shoppingCart.getId(), productId);
            cartItem.setId(cartItemId);
            cartItem.setShoppingCart(shoppingCart);
            shoppingCart.setItems(cartItems);
            shoppingCart.setUserId(userDTO.getUserId());
            shoppingCart.setStatus(ShoppingCartStatus.SUCCESS);
//            return new BaseResponseV2<>(shoppingCartRepository.save(shoppingCart));

            shoppingCartRepository.save(shoppingCart);

            Set<CartItemDTO> cartItemDTO = new HashSet<>();
            CartItemDTO itemDTO = new CartItemDTO();
            itemDTO.setProduct(productDTO.getId());
            itemDTO.setQuantity(productDTO.getQuantity());
            itemDTO.setShoppingCartId(shoppingCart.getId());
            CartItemIdDTO cartItemIdDTO = new CartItemIdDTO(shoppingCart.getId(), productId);
            itemDTO.setId(cartItemIdDTO);
            cartItemDTO.add(itemDTO);

            ShoppingCartDTO shoppingCartDTO = new ShoppingCartDTO();
            shoppingCartDTO.setId(shoppingCart.getId());
            shoppingCartDTO.setUserId(shoppingCart.getUserId());
            shoppingCartDTO.setStatus(ShoppingCartStatus.SUCCESS);
            shoppingCartDTO.setItems(cartItemDTO);

//            String message = JsonHelper.toString(shoppingCartDTO);
//            resdisPusherMessageService.sendMessage(message , channelTopic);
//            log.info("message",message);
            rabbitTemplate.convertAndSend(EXCHANGE_NAME, ROUTING_KEY_NAME, shoppingCartDTO);
            return new BaseResponseV2<>();
        }
    }

    public BaseResponseV2 getAllCart(Long userId) throws ApplicationException{
        UserDTO userDTO = getUserDTO();
        Optional<ShoppingCart> shoppingCart = shoppingCartRepository.findByUserId(userDTO.getUserId());
        if (!shoppingCart.isPresent()){
            return null;
        }
        Long shoppingCartId = shoppingCart.get().getId();
        List<CartItem> cartItemList = cartItemRepository.findAllByShoppingCartId(shoppingCartId);
        return new BaseResponseV2<>(cartItemList);
    }
    public ShoppingCart deleteCartItem(Long shoppingCartId, Long productId) throws ApplicationException
    {
        BaseResponseV2<ProductDTO> productInfoResponse = productConnector.getProductById(productId);

        if (!productInfoResponse.isSuccess()){
            throw new ApplicationException(ERROR.INVALID_PARAM);
        }
        ProductDTO productDTO = productInfoResponse.getData();

        Optional<ShoppingCart> optionalShoppingCart = shoppingCartRepository.findById(shoppingCartId);
        if (!optionalShoppingCart.isPresent()){
            return null;
        }
        ShoppingCart shoppingCart = optionalShoppingCart.get();
        Set<CartItem> cartItems = shoppingCart.getItems();
        boolean exits = false;
        for (CartItem item:cartItems
        ) {
            if (item.getProduct()==(productDTO.getId())){
                exits = true;
                cartItems.remove(item);
                CartItemId cartItemId = new CartItemId(shoppingCartId, productId);
                cartItemRepository.deleteById(cartItemId);
            }
        }
        if (!exits){
            return null;
        }
        shoppingCart.setItems(cartItems);
        shoppingCartRepository.save(shoppingCart);
        return shoppingCart;
    }

    public ShoppingCart updateQuantity(Long shoppingCartId, Long productId, int quantity) throws ApplicationException
    {
        BaseResponseV2<ProductDTO> productInfoResponse = productConnector.getProductById(productId);

        if (!productInfoResponse.isSuccess()){
            throw new ApplicationException(ERROR.INVALID_PARAM);
        }
        ProductDTO productDTO = productInfoResponse.getData();
        if (!productId.equals(productDTO.getId())) {
            throw new ApplicationException(ERROR.SYSTEM_ERROR);
        }

        Optional<ShoppingCart> optionalShoppingCart = shoppingCartRepository.findById(shoppingCartId);
        if (!optionalShoppingCart.isPresent()){
            return null;
        }
        ShoppingCart shoppingCart = optionalShoppingCart.get();
        Set<CartItem> cartItems = shoppingCart.getItems();
        for (CartItem item:cartItems
        ) {
            if (item.getProduct()==(productId)){
                item.setQuantity(quantity);
                shoppingCart.setItems(cartItems);
                cartItemRepository.save(item);
            }
        }
        return shoppingCartRepository.save(shoppingCart);
    }

    public void deleteAllCart(Long shoppingCartId) throws ApplicationException
    {
        Optional<ShoppingCart> optionalShoppingCart = shoppingCartRepository.findById(shoppingCartId);
        if (!optionalShoppingCart.isPresent()){
            return;
        }
        ShoppingCart shoppingCart = optionalShoppingCart.get();
        Set<CartItem> cartItems = shoppingCart.getItems();
        for (CartItem item:cartItems
        ) {
            cartItemRepository.deleteAllByShoppingCartId(item.getShoppingCart().getId());
        }
    }

}