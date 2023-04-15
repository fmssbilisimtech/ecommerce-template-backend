package com.fmss.basketservice.mapper;


import com.fmss.basketservice.exception.BasketNotFoundException;
import com.fmss.basketservice.feign.ProductClient;
import com.fmss.basketservice.model.dto.BasketItemRequestDto;
import com.fmss.basketservice.model.dto.BasketItemRequestWithUserIdDto;
import com.fmss.basketservice.model.dto.ProductResponseDto;
import com.fmss.basketservice.model.entity.BasketItem;
import com.fmss.basketservice.model.enums.BasketStatus;
import com.fmss.basketservice.repository.BasketItemRepository;
import com.fmss.basketservice.repository.BasketRepository;
import com.fmss.commondata.dtos.response.BasketItemResponseDto;
import com.fmss.commondata.dtos.response.BasketResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

//@Mapper(componentModel = "spring")
//public interface BasketItemMapper {
//
//    BasketItemResponseDto toResponseDto(BasketItem basketItem);
//
//    List<BasketItemResponseDto> toResponseDtoList(List<BasketItem> basketItemList);
//
//    BasketItem toEntity(BasketItemRequestDto basketItemRequestDto);
//}

@Component
@RequiredArgsConstructor
public class BasketItemMapper {
    private final BasketRepository basketRepository;
    private final ProductClient productClient;

    private final BasketItemRepository basketItemRepository;

    public BasketItemResponseDto toResponseDto(BasketItem basketItem){
        ProductResponseDto productById = productClient.getProductById(basketItem.getProductId());

        return BasketItemResponseDto.builder()
                .basketItemId(basketItem.getBasketItemId())
                .price(productById.price())
                .imgUrl(productById.image())
                .name(productById.name())
                .productId(basketItem.getProductId())
                .quantity(basketItem.getQuantity())
                .build();
    }

    public BasketItem toEntity(BasketItemRequestDto basketItemRequestDto, BasketResponseDto basketByUserId){
        return BasketItem.builder()
                .productId(basketItemRequestDto.productId())
                .quantity(basketItemRequestDto.quantity())
                .basket(basketRepository.findById(basketByUserId.basketId()).orElseThrow(BasketNotFoundException::new))
                .build();
    }

    public BasketItem toEntityWithUserId(BasketItemRequestWithUserIdDto basketItemRequestWithUserIdDto){
        return BasketItem.builder()
                .productId(basketItemRequestWithUserIdDto.productId())
                .quantity(basketItemRequestWithUserIdDto.quantity())
                .basket(basketRepository.findByUserIdAndBasketStatus(basketItemRequestWithUserIdDto.userId(), BasketStatus.ACTIVE).orElseThrow(BasketNotFoundException::new))
                .build();
    }

    public List<BasketItemResponseDto> toResponseDtoList(List<BasketItem> basketItems){
        return basketItems.stream().map(this::toResponseDto).toList();
    }
}
