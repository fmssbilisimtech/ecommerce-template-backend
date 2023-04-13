package com.fmss.apigateway.service;

import com.fmss.apigateway.model.dto.BasketResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class BasketService {

    private final WebClient basketWebClient;

    public Mono<ResponseEntity<BasketResponseDto>> getCurrentUserBasket(){
        String userId = null;
        return basketWebClient
                .get()
                .uri("/basket-user/" + userId)
                .retrieve()
                .bodyToMono(BasketResponseDto.class)
                .map(basketResponseDto -> ResponseEntity.ok(basketResponseDto));
    }
}
