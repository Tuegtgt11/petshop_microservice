package com.tass.product.model.request.dto;

import com.tass.common.myenums.ProductStatus;
import com.tass.product.entities.Brand;
import com.tass.product.entities.Category;
import com.tass.product.entities.Product;
import com.tass.product.entities.Size;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductInfo{
    private String name;
    private String description;

    private String barcode;
    private String images;
    private String detail;
    private int sold;
    private Size size;
    private int quantity;
    private BigDecimal price;
    private ProductStatus status;
    private Brand brand;
    private Category category;

    public ProductInfo(Product entity) {
        this.name = entity.getName();
        this.description = entity.getDescription();
        this.barcode = entity.getBarcode();
        this.images = entity.getImages();
        this.detail = entity.getDetail();
        this.sold = entity.getSold();
        this.size = entity.getSize();
        this.quantity = entity.getQuantity();
        this.price = entity.getPrice();
        this.status = entity.getStatus();
        this.brand = entity.getBrand();
        this.category = entity.getCategory();
    }

    public ProductInfo() {
    }
}
