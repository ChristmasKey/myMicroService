package com.djn.order.controller;

import com.djn.order.domain.Order;
import com.djn.order.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * Name: OrderController
 * Description: 订单Controller
 * Copyright: Copyright (c) 2023 MVWCHINA All rights Reserved
 * Company: 江苏医视教育科技发展有限公司
 *
 * @author 丁佳男
 * @version 1.0
 * @since 2023-11-22 16:07
 */

@Slf4j
@RestController
@RequestMapping("/order")
public class OrderController {

    @Resource
    private OrderService orderService;

    @GetMapping("/{orderId}")
    public Order queryOrderByOrderId(@PathVariable("orderId") Long orderId, @RequestHeader(value = "Truth", required = false) String truth) {
        System.out.println("truth: " + truth);
        return orderService.queryOrderById(orderId);
    }
}
