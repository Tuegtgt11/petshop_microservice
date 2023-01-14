package com.tass.shoppingcartservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class ShoppingcartServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShoppingcartServiceApplication.class, args);
    }

}
