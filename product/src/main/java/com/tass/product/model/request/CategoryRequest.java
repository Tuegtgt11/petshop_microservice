package com.tass.product.model.request;

import com.tass.common.myenums.CategoryStatus;
import lombok.*;

import javax.persistence.Column;

@Data
public class CategoryRequest {
    private String name;
    @Column(columnDefinition = "text")
    private String icon;
    private CategoryStatus status;
}
