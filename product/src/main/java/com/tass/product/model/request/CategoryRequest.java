package com.tass.product.model.request;

import com.tass.product.entities.myenum.CategoryStatus;
import lombok.*;

import javax.persistence.Column;

@Data
public class CategoryRequest {
    private String name;
    @Column(columnDefinition = "text")
    private String icon;
    private CategoryStatus status;
}
