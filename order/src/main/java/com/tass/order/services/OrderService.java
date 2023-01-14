package com.tass.order.services;

import com.tass.common.model.ApplicationException;
import com.tass.common.model.BaseResponse;
import com.tass.common.model.BaseResponseV2;
import com.tass.common.model.dto.order.OrderDTO;
import com.tass.common.model.dto.product.ProductDTO;
import com.tass.common.model.dto.shopping.CartItemDTO;
import com.tass.common.model.dto.shopping.ShoppingCartDTO;
import com.tass.common.model.userauthen.UserDTO;
import com.tass.common.myenums.OrderStatus;
import com.tass.common.myenums.ProductStatus;
import com.tass.common.myenums.ShoppingCartStatus;
import com.tass.common.utils.JsonHelper;
import com.tass.order.connector.ProductConnector;
import com.tass.order.connector.ShoppingCartConnector;
import com.tass.order.entities.Order;
import com.tass.order.entities.OrderDetail;
import com.tass.order.entities.OrderDetailId;
import com.tass.order.repositories.OrderDetailRepository;
import com.tass.order.repositories.OrderRepository;
import com.tass.order.request.CreatedOrderRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Log4j2
public class OrderService extends BaseService{
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    OrderDetailRepository orderDetailRepository;

    @Autowired
    ProductConnector productConnector;

    @Autowired
    ShoppingCartConnector shoppingCartConnector;

    @Autowired
    ResdisPusherMessageService resdisPusherMessageService;
    @Autowired
    RabbitTemplate rabbitTemplate;
    @Value("${spring.rabbitmq.exchange}")
    private String EXCHANGE_NAME;

    @Value("${spring.rabbitmq.routing-key.order}")
    private String ROUTING_KEY_NAME;
    @Autowired
    @Qualifier("shoppingcart")
    ChannelTopic channelTopic;
    public Optional<Order> findById(Long id) {
        return orderRepository.findById(id);
    }

    public Order save(Order order) {
        return orderRepository.save(order);
    }

    public void delete(Long id) {
        orderRepository.deleteById(id);
    }
    public Order saveCart(Order order) {
        return orderRepository.save(order);
    }

    public Map<String, Object> findAll(Pageable pageable) {
        Map<String, Object> responses = new HashMap<>();
        Page<Order> pageTotal = orderRepository.findAllBy(pageable);
        List<Order> list = pageTotal.getContent();
        responses.put("content", list);
        responses.put("currentPage", pageTotal.getNumber() + 1);
        responses.put("totalItems", pageTotal.getTotalElements());
        responses.put("totalPage", pageTotal.getTotalPages());
        return responses;
    }

//    public void handleEventOrder(ShoppingCartDTO shoppingCartDTO){
//        // handle event order
//
//        if (shoppingCartDTO.getStatus() == ShoppingCartStatus.SUCCESS){
//            this.placeOrder(shoppingCartDTO.getId());
//            return;
//        }
//    }

    @Transactional
    public BaseResponseV2<Order> placeOrder(Long id) {
        Optional<ShoppingCartDTO> shoppingCartOptional = shoppingCartConnector.getShoppingCartById(id);
        if (shoppingCartOptional.isPresent()) {
            ShoppingCartDTO shoppingCart = shoppingCartOptional.get();

            Set<OrderDetail> orderDetailSet = new HashSet<>();
            OrderDetail dto = null;
            for (CartItemDTO cartItem:
                    shoppingCart.getItems()) {
                BaseResponseV2<ProductDTO> optionalProductDTO = productConnector.getProductById(cartItem.getProduct());
                dto = new OrderDetail();
                if (orderRepository.findMaxId() != null){
                    dto.setId(new OrderDetailId(orderRepository.findMaxId() + 1, cartItem.getProduct()));
                }
                dto.setId(new OrderDetailId(1L, cartItem.getProduct()));
                if (orderRepository.findMaxId() != null){
                    dto.setOrder(Order.builder().id(orderRepository.findMaxId() + 1).build());
                }
                dto.setOrder(Order.builder().id(1L).build());
                dto.setProduct(cartItem.getProduct());
                dto.setQuantity(cartItem.getQuantity());
                dto.setUnitPrice(optionalProductDTO.getData().getPrice());
                dto.setQuantity(cartItem.getQuantity());
                dto.setUpdatedAt(LocalDateTime.now());
                orderDetailSet.add(dto);

            }
//            for (int i = 0; i <orderDetailSet.size(); i++) {
//
//            }
            UserDTO userDTO = getUserDTO();
            for (CartItemDTO cartItem:
                    shoppingCart.getItems()) {
                BaseResponseV2<ProductDTO> optionalProductDTO = productConnector.getProductById(cartItem.getProduct());
                Order order = Order.builder()
                        .status(OrderStatus.DONE)
                        .userId(shoppingCart.getUserId())
                        .orderDetails(orderDetailSet)
                        .isShoppingCart(true)
                        .totalPrice(optionalProductDTO.getData().getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())))
                        .build();
                order.setUpdatedAt(LocalDateTime.now());
                orderRepository.save(order);
                orderDetailRepository.saveAll(orderDetailSet);

                OrderDTO orderDTO = new OrderDTO();
                orderDTO.setShoppingCartId(id);
                orderDTO.setProductId(cartItem.getProduct());
                orderDTO.setQty(cartItem.getQuantity());
                rabbitTemplate.convertAndSend(EXCHANGE_NAME,ROUTING_KEY_NAME,orderDTO);
                return new BaseResponseV2<>(order);
            }
        }
        return null;
    }
    public Page<Order> searchAllForAdmin(Specification<Order> specification, int page, int limit){
        return orderRepository.findAll(specification, PageRequest.of(page, limit));
    }

    public int totalOrder(){
        return orderRepository.findAll().size();
    }
//    public int totalOrderByStatus(int status){
//        OrderStatus status1 = OrderStatus.CONFIRMED;
//        if (status == 0){
//            status1 = OrderStatus.PENDING;
//        }
//        if (status == 2){
//            status1 = OrderStatus.CANCELLED;
//        }
//        if (status == 3){
//            status1 = OrderStatus.DONE;
//        }
//        if (status == 4){
//            status1 = OrderStatus.PROCESSING;
//        }
//        return orderRepository.findAllByStatus(status1).size();
//    }


//    @RabbitListener(queues = {"${spring.rabbitmq.queue.shopingcart}"})
//    private void listenMessage(ShoppingCartDTO i){
//        log.info("asdasddas " + i);
//    }

}
