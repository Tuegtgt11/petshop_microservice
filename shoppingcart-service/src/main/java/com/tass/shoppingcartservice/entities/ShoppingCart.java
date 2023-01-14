package com.tass.shoppingcartservice.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.tass.common.myenums.ShoppingCartStatus;
import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "shopping_cart")
public class ShoppingCart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private Long userId;
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "shoppingCart")
    @JsonManagedReference
    private Set<CartItem> items;

    private ShoppingCartStatus status;
}
