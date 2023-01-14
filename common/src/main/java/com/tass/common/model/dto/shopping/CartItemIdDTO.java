package com.tass.common.model.dto.shopping;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItemIdDTO implements Serializable {

    private Long shoppingCartId;

    private Long productId;

}