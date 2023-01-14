package com.tass.product.repositories;

import com.tass.product.entities.Category;
import com.tass.product.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product , Long>, JpaSpecificationExecutor<Product> {
    Optional<Product> findProductByBarcode(String barcode);

    List<Product> findAllByCategory(Category category);
}
