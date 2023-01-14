package com.tass.order.repositories;

import com.tass.common.myenums.OrderStatus;
import com.tass.order.entities.Order;
import com.tass.order.entities.OrderDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> , JpaSpecificationExecutor<Order> {
    Page<Order> findAllBy(Pageable pageable);
//    Order findByOrderDetails(OrderEvent orderEvent);

    @Query("SELECT max(o.id) FROM Order o")
    Long findMaxId();

    List<Order> findAllByStatus(OrderStatus status);
}
