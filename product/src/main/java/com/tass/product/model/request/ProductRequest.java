package com.tass.product.model.request;

import com.tass.product.entities.Brand;
import com.tass.product.entities.Category;
import com.tass.product.entities.Size;
import com.tass.product.entities.myenum.ProductStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequest {
    private String name;
    @Column(columnDefinition = "text")
    private String description;
    private String barcode;
    private String images;
    @Column(columnDefinition = "text")
    private String detail;
    private int sold;
    private Size size;
    private int quantity;
    private BigDecimal price;
    private Brand brand;
    private Category category;
    private ProductStatus status;
}
