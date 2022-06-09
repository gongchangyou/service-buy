package com.mouse.buy.controller;

import com.mouse.order.intf.OrderService;
import com.mouse.store.intf.StoreService;
import io.seata.core.context.RootContext;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
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
    ) {

        //这俩操作成功了
        val orderId = orderService.create();
        val storeResult = storeService.decr(3);

        //制造异常
        List a = null;
        a.get(3);

        //这里没执行
        val orderResult = orderService.buy(orderId);

        return new HashMap() {{
            put("orderId", orderId);
            put("storeResult", storeResult);
            put("orderResult", orderResult);
        }};

    }

    @GlobalTransactional(timeoutMills = 300000, name = "dubbo-demo-tx")
    @GetMapping(value = "/transaction/success")
    public Map paySuccess(
            @RequestParam(name = "count", required = false) Integer count
    ) {
        log.info("[reduceStock] 当前 XID: {}", RootContext.getXID());

        val orderId = orderService.create();
        val storeResult = storeService.decr(3);
        val orderResult = orderService.buy(orderId);

        return new HashMap() {{
            put("orderId", orderId);
            put("storeResult", storeResult);
            put("orderResult", orderResult);
        }};

    }
}
