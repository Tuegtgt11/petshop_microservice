package com.tass.order.repositories;

import com.tass.order.entities.OrderDetail;
import com.tass.order.entities.OrderDetailId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, OrderDetailId> , JpaSpecificationExecutor<OrderDetail> {
    List<OrderDetail> findAllByOrderId(Long id);


}
