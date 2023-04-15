package com.fmss.basketservice.controller;


import com.fmss.basketservice.configuration.ThreadContext;
import com.fmss.basketservice.service.BasketService;
import com.fmss.commondata.configuration.UserContext;
import com.fmss.commondata.dtos.response.BasketResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static com.fmss.basketservice.constants.BasketConstants.*;

@RestController
@RequestMapping(API_PREFIX + API_VERSION_V1 + API_BASKETS)
@RequiredArgsConstructor
public class BasketController {

    private final BasketService basketService;

    @Operation(summary = "Basket get by userId")
    @ApiResponses(value =
    @ApiResponse(
            responseCode = "200",
            description = "Get basket by userId",
            content = @Content(
                    schema = @Schema(implementation = BasketResponseDto.class),
                    mediaType = "application/json")))
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/user")
    public BasketResponseDto getUserBasket() {
        return basketService.getBasketByUserId(UUID.fromString(ThreadContext.getCurrentUser().getUserId()));
    }

    @Operation(summary = "Basket get by userId")
    @ApiResponses(value =
    @ApiResponse(
            responseCode = "200",
            description = "Get basket by userId",
            content = @Content(
                    schema = @Schema(implementation = BasketResponseDto.class),
                    mediaType = "application/json")))
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public BasketResponseDto getBasket() {
        return basketService.getBasketByBasketId();
    }

    @Operation(summary = "Disable basket")
    @ApiResponses(value =
    @ApiResponse(
            responseCode = "200",
            description = "Disable basket",
            content = @Content(
                    mediaType = "application/json")))
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/disable/{basketId}")
    public void disableBasket(@PathVariable UUID basketId) {
        basketService.disableBasket(basketId);
    }

    @Operation(summary = "Delete basket")
    @ApiResponses(value =
    @ApiResponse(
            responseCode = "204",
            description = "Delete basket",
            content = @Content(
                    mediaType = "application/json")))
    @DeleteMapping("/{basketId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBasket(@PathVariable UUID basketId) {
        basketService.deleteBasket(basketId);
    }


}
