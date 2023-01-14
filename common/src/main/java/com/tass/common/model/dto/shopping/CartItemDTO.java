package com.tass.common.model.dto.shopping;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;

import javax.persistence.EmbeddedId;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import java.io.Serializable;


@Data
public class CartItemDTO implements Serializable{
    private CartItemIdDTO id;
    private int quantity;
    private long shoppingCartId;

    private long product;
}
