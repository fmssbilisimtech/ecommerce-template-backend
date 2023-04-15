package com.fmss.productservice;


import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;


@EnableFeignClients
@EnableCaching
@EnableEncryptableProperties
@SpringBootApplication
@RequiredArgsConstructor
public class ProductServiceApplication {



    public static void main(String[] args) {
        SpringApplication.run(ProductServiceApplication.class, args);
    }
}

