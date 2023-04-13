package com.fmss.basketservice.integration;


import com.fmss.basketservice.model.entity.Basket;
import com.fmss.basketservice.model.entity.BasketItem;
import com.fmss.basketservice.model.enums.BasketStatus;
import com.fmss.basketservice.repository.BasketItemRepository;
import com.fmss.basketservice.repository.BasketRepository;
import com.fmss.commondata.model.enums.PaymentStatus;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class BasketControllerTest extends BaseIntegrationTest{

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    BasketRepository basketRepository;

    @Autowired
    BasketItemRepository basketItemRepository;

    Basket basketAlreadyCreated;
    BasketItem basketItemAlreadyCreated;

    @BeforeEach
    void setUp() {
        Basket basket = Basket.builder()
                .basketId(UUID.randomUUID())
                .userId(UUID.fromString("1234"))
                .basketStatus(BasketStatus.ACTIVE)
                .totalPrice(BigDecimal.valueOf(1000))
                .build();

        BasketItem basketItem = BasketItem.builder()
                .basket(basket)
                .basketItemId(UUID.randomUUID())
                .productId(UUID.randomUUID())
                .productId(UUID.randomUUID())
                .quantity(5)
                .build();
        List<BasketItem> basketItemList = new ArrayList<>();
        basket.setBasketItems(basketItemList);

        basketAlreadyCreated = basketRepository.save(basket);
        basketItemAlreadyCreated = basketItemRepository.save(basketItem);
    }

    @AfterEach
    void tearDown() {
        basketItemRepository.deleteAll();
        basketRepository.deleteAll();
    }

    @Test
    void getBasketByUserId() throws Exception {
        this.mockMvc.perform(get("/basket/basket-user/1234")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.basketId").value(basketAlreadyCreated.getBasketId()));
    }

    @Test
    void getBasketByBasketId() throws Exception {
        this.mockMvc.perform(get("/basket/basket-basket/" + basketAlreadyCreated.getBasketId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.basketId").value(basketAlreadyCreated.getBasketId()));
    }
}
