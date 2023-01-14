package com.tass.product.model.request;

import com.tass.common.myenums.BrandStatus;
import lombok.*;

import javax.persistence.Column;

@Data
public class BrandRequest {
    private String name;
    @Column(columnDefinition = "text")
    private String image;
    private BrandStatus status;
}
