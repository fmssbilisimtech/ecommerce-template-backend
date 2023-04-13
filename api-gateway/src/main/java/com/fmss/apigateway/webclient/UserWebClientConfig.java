package com.fmss.apigateway.webclient;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class UserWebClientConfig {
    @Value("${service.user.url}")
    String userServiceBaseUrl;

    @Value("${basket.user.url}")
    String basketServiceBaseUrl;

    public WebClient userServiceWebClient() {
        return WebClient
                .builder()
                .baseUrl(userServiceBaseUrl)
//                .filter(ExchangeFilterFunction.ofResponseProcessor(clientResponse -> {
//                    if (clientResponse.statusCode().isError()) {
//                        return switch (clientResponse.statusCode().value()){
//                            case 400 -> clientResponse.bodyToMono(ErrorBody.class).flatMap(errorBody -> Mono.error(new BadRequestException(errorBody.getErrorDescription())));
//                            case 403 -> clientResponse.bodyToMono(ErrorBody.class).flatMap(errorBody -> Mono.error(new ForbiddenException(errorBody.getErrorDescription())));
//                            case 404 -> clientResponse.bodyToMono(ErrorBody.class).flatMap(errorBody -> Mono.error(new NotFoundException(errorBody.getErrorDescription())));
//                            case 409 -> clientResponse.bodyToMono(ErrorBody.class).flatMap(errorBody -> Mono.error(new ConflictException(errorBody.getErrorDescription())));
//                            case 412 -> clientResponse.bodyToMono(ErrorBody.class).flatMap(errorBody -> Mono.error(new PreconditionException(errorBody.getErrorDescription())));
//                            case 429 -> clientResponse.bodyToMono(ErrorBody.class).flatMap(errorBody -> Mono.error(new TooManyRequestException(errorBody.getErrorDescription())));
//                            case 433 -> clientResponse.bodyToMono(ErrorBody.class).flatMap(errorBody -> Mono.error(new DCBusinessException(errorBody.getErrorDescription(),errorBody.getErrorCode())));
//                            case 500 -> clientResponse.bodyToMono(ErrorBody.class).flatMap(errorBody -> Mono.error(new InternalServerException(errorBody.getErrorDescription())));
//                            case 502 -> clientResponse.bodyToMono(ErrorBody.class).flatMap(errorBody -> Mono.error(new DCRequestedServiceDownException(errorBody.getErrorDescription(), 434)));
//                            default -> Mono.just(clientResponse);
//                        };
//                    }
//                    else {
//                        return Mono.just(clientResponse);
//                    }
//                }))
                .build();
    }

    public WebClient basketServiceWebClient() {
        return WebClient
                .builder()
                .baseUrl(basketServiceBaseUrl)
//                .filter(ExchangeFilterFunction.ofResponseProcessor(clientResponse -> {
//                    if (clientResponse.statusCode().isError()) {
//                        return switch (clientResponse.statusCode().value()){
//                            case 400 -> clientResponse.bodyToMono(ErrorBody.class).flatMap(errorBody -> Mono.error(new BadRequestException(errorBody.getErrorDescription())));
//                            case 403 -> clientResponse.bodyToMono(ErrorBody.class).flatMap(errorBody -> Mono.error(new ForbiddenException(errorBody.getErrorDescription())));
//                            case 404 -> clientResponse.bodyToMono(ErrorBody.class).flatMap(errorBody -> Mono.error(new NotFoundException(errorBody.getErrorDescription())));
//                            case 409 -> clientResponse.bodyToMono(ErrorBody.class).flatMap(errorBody -> Mono.error(new ConflictException(errorBody.getErrorDescription())));
//                            case 412 -> clientResponse.bodyToMono(ErrorBody.class).flatMap(errorBody -> Mono.error(new PreconditionException(errorBody.getErrorDescription())));
//                            case 429 -> clientResponse.bodyToMono(ErrorBody.class).flatMap(errorBody -> Mono.error(new TooManyRequestException(errorBody.getErrorDescription())));
//                            case 433 -> clientResponse.bodyToMono(ErrorBody.class).flatMap(errorBody -> Mono.error(new DCBusinessException(errorBody.getErrorDescription(),errorBody.getErrorCode())));
//                            case 500 -> clientResponse.bodyToMono(ErrorBody.class).flatMap(errorBody -> Mono.error(new InternalServerException(errorBody.getErrorDescription())));
//                            case 502 -> clientResponse.bodyToMono(ErrorBody.class).flatMap(errorBody -> Mono.error(new DCRequestedServiceDownException(errorBody.getErrorDescription(), 434)));
//                            default -> Mono.just(clientResponse);
//                        };
//                    }
//                    else {
//                        return Mono.just(clientResponse);
//                    }
//                }))
                .build();
    }
}
