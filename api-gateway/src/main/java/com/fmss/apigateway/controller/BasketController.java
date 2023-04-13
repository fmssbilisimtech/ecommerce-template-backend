package com.fmss.apigateway.controller;

import com.fmss.apigateway.model.dto.BasketResponseDto;
import com.fmss.apigateway.service.BasketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/basket")
@RequiredArgsConstructor
public class BasketController {

    private final BasketService basketService;

    @GetMapping("/basket-user/{userId}")
    public Mono<ResponseEntity<BasketResponseDto>> getCurrentUserBasket() {
        return basketService.getCurrentUserBasket();
    }

    @GetMapping("/basket-basket/{basketId}")
    public ResponseEntity<BasketResponseDto> getBasketByBasketId(@PathVariable String basketId) {
        return ResponseEntity.ok(basketService.getBasketByBasketId(basketId));
    }

    @PutMapping("/disable/{basketId}")
    public void disableBasket(@PathVariable String basketId) {
        basketService.disableBasket(basketId);
    }

    @DeleteMapping("/{basketId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBasket(@PathVariable String basketId) {
        basketService.deleteBasket(basketId);
    }



}
