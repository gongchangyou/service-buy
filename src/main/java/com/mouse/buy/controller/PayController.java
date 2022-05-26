package com.mouse.buy.controller;

import com.mouse.order.intf.OrderService;
import com.mouse.store.intf.StoreService;
import lombok.val;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/pay")
public class PayController {

    @DubboReference(version = "1.0.0",group = "order-demo", protocol = "dubbo")
    private OrderService orderService;

    @DubboReference(version = "1.0.0",group = "store-demo", protocol = "dubbo")
    private StoreService storeService;

    @Value("${test}")
    int test;

    @Value("${spring.profiles.active}")
    String active;

    @GetMapping(value = "/toPay")
    public Map pay(
            @RequestParam(name = "count", required = false) Integer count
            ){

        val orderId = orderService.create();
        val storeResult = storeService.decr(3);
        val orderResult = orderService.buy(orderId);

        return new HashMap() {{
            put("orderId", orderId);
            put("storeResult", storeResult);
            put("orderResult", orderResult);
        }};

    }

    @GetMapping(value = "/transaction/failed")
    public Map payFailed(
            @RequestParam(name = "count", required = false) Integer count
    ) throws Exception {

        val orderId = orderService.create();
        val storeResult = storeService.decr(3);

        throw new Exception("order status mod failed");

        val orderResult = orderService.buy(orderId);

        return new HashMap() {{
            put("orderId", orderId);
            put("storeResult", storeResult);
            put("orderResult", orderResult);
        }};

    }
}
