package com.fmss.orderservice.service;


import com.fmss.orderservice.model.OrderOutbox;
import com.fmss.orderservice.repository.OrderOutboxRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderOutBoxService {
    private final OrderOutboxRepository orderOutboxRepository;

    public OrderOutbox saveOrderOutbox(OrderOutbox orderOutbox) {
     return   orderOutboxRepository.save(orderOutbox);

    }

    public void deleteOrderOutbox(UUID orderId) {
        orderOutboxRepository.deleteByOrderId(orderId);
    }
}

