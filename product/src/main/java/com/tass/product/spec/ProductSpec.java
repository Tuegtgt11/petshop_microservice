package com.tass.product.spec;

import com.tass.common.myenums.ProductStatus;
import com.tass.product.entities.Brand;
import com.tass.product.entities.Category;
import com.tass.product.entities.Product;
import com.tass.product.entities.Size;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ProductSpec {
    public static Specification<Product> productSpec(String name, String barcode, Size size, BigDecimal price, BigDecimal from, BigDecimal to, ProductStatus status, Brand brand, Category category, Integer page, Integer pageSize) {
        return ((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (name != null && !(name.isEmpty())) {
                predicates.add(criteriaBuilder.like(root.get("name"), "%" + name + "%"));
            }
            if (barcode != null && !(barcode.isEmpty())) {
                predicates.add(criteriaBuilder.equal(root.get("barcode"),barcode ));
            }
            if (size != null) {
                predicates.add(criteriaBuilder.equal(root.get("size"),size));
            }
            if (price != null) {
                predicates.add(criteriaBuilder.equal(root.get("price"), price ));
            }
            if (from != null && to != null)
            {
                predicates.add(criteriaBuilder.between(root.get("price"), from, to));
            }
            if (status != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"),status));
            }
            if (brand != null) {
                predicates.add(criteriaBuilder.equal(root.get("brand"),brand));
            }
            if (category != null) {
                predicates.add(criteriaBuilder.equal(root.get("category"),category));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });
    }

}
