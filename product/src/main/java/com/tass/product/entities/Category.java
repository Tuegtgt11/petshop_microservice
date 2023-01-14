package com.tass.product.entities;

import com.tass.common.model.base.BaseEntity;
import com.tass.common.myenums.CategoryStatus;
import lombok.*;

import javax.persistence.*;


@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "categories")
public class Category extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Column(columnDefinition = "text")
    private String icon;
    @Enumerated(EnumType.ORDINAL)
    private CategoryStatus status;
}
