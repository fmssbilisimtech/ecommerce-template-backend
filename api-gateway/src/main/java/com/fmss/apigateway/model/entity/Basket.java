package com.fmss.apigateway.model.entity;

import com.fmss.commondata.model.entity.AbstractEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.List;

@Entity
@SuperBuilder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Basket extends AbstractEntity {

    @OneToMany(mappedBy = "basket", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BasketItem> basketItems;

    private BigDecimal totalPrice;

    //@Enumerated(EnumType.STRING)
    //private BasketStatus basketStatus;

    private String userId;
}
