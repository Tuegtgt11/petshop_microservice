package com.tass.product.entities;

import com.tass.common.model.base.BaseEntity;
import com.tass.product.entities.myenum.BrandStatus;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "brands")
public class Brand extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Column(columnDefinition = "text")
    private String image;
    @Enumerated(EnumType.ORDINAL)
    private BrandStatus status;
}
