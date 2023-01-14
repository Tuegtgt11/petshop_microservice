package com.tass.userservice.spec;

import com.tass.userservice.entities.Role;
import com.tass.userservice.entities.User;
import com.tass.userservice.entities.myenum.UserStatus;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public class UserSpec {
    public static Specification<User> userSpec(String username, String fullName, String phone, String email, Integer gender, String address, Role role, UserStatus status, Integer page, Integer pageSize) {
        return ((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (username != null && !(username.isEmpty())) {
                predicates.add(criteriaBuilder.like(root.get("username"), "%" + username + "%"));
            }
            if (fullName != null && !(fullName.isEmpty())) {
                predicates.add(criteriaBuilder.like(root.get("fullName"), "%" + fullName + "%"));
            }
            if (phone != null && !(phone.isEmpty())) {
                predicates.add(criteriaBuilder.like(root.get("phone"), "%" + phone + "%"));
            }
            if (email != null && !(email.isEmpty())) {
                predicates.add(criteriaBuilder.like(root.get("email"), "%" + email + "%"));
            }
            if (gender != null ) {
                predicates.add(criteriaBuilder.equal(root.get("gender"), gender ));
            }
            if (address != null && !(address.isEmpty())) {
                predicates.add(criteriaBuilder.like(root.get("address"), "%" + address + "%"));
            }
            if (role != null) {
                predicates.add(criteriaBuilder.equal(root.get("role"),role ));
            }
            if (status != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"),status ));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });
    }
}
