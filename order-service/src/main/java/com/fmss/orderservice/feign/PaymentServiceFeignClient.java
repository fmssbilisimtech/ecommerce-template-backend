package com.fmss.orderservice.feign;

import com.fmss.commondata.dtos.request.CreatePaymentRequestDto;
import com.fmss.commondata.dtos.response.PaymentResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;


@FeignClient(name = "payment-service", url = "http://89.19.23.50:9004")
public interface PaymentServiceFeignClient {

    @PostMapping(value = "/api/v1/payment")
    PaymentResponseDto createPayment(CreatePaymentRequestDto createPaymentRequestDto);

}
