package com.fmss.apigateway.model.entity;

import com.fmss.commondata.model.entity.AbstractEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BasketItem extends AbstractEntity {
    private String productId;

    private Integer quantity;

    @ManyToOne
    @JoinColumn(name = "basket_id")
    private Basket basket;
}
