package com.mouse.buy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.mouse")
//@EnableDubbo
public class BuyApplication {

    public static void main(String[] args) {
        SpringApplication.run(BuyApplication.class, args);
    }

}
