package com.fmss.orderservice.dto;

import com.fmss.commondata.dtos.response.BasketResponseDto;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record PlaceOrderRequestDTO(
        @NotNull
        BasketResponseDto basketResponseDto) {
}
