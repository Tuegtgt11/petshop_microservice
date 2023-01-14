package com.tass.common.model.dto.shopping;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.tass.common.myenums.ShoppingCartStatus;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;
@Data
public class ShoppingCartDTO implements Serializable {
    private long id;
    private Long userId;

    private Set<CartItemDTO> items;

    private ShoppingCartStatus status;

    public ShoppingCartDTO() {
    }
}
