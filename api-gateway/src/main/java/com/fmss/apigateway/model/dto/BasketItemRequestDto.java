package com.fmss.apigateway.model.dto;

public record BasketItemRequestDto(
        String productId,
        Integer quantity,
        String basketId)
{}
