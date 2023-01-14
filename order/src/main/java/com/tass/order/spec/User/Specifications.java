package com.tass.order.spec.User;


import com.tass.common.myenums.OrderStatus;
import com.tass.order.entities.Order;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public class Specifications {
    public static Specification<Order> OrderSpec(OrderStatus status, long userId , int page, int limit){
        return ((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (status != null)
                predicates.add(criteriaBuilder.equal(root.get("status"),status));
            if (String.valueOf(userId) != null &&  !(String.valueOf(userId) .isEmpty()))
                predicates.add(criteriaBuilder.equal(root.get("user"),userId));
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });
    }

}
