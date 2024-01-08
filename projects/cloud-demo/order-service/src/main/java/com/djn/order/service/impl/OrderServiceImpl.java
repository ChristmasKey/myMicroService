package com.djn.order.service.impl;

// import com.djn.order.clients.UserClient;
import com.djn.feign.clients.UserClient;
import com.djn.order.domain.Order;
// import com.djn.order.domain.User;
import com.djn.feign.domain.User;
import com.djn.order.mapper.OrderMapper;
import com.djn.order.service.OrderService;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

/**
 * Name: OrderServiceImpl
 * Description: 订单Service实现
 * Copyright: Copyright (c) 2023 MVWCHINA All rights Reserved
 * Company: 江苏医视教育科技发展有限公司
 *
 * @author 丁佳男
 * @version 1.0
 * @since 2023-11-22 16:08
 */

@Service
public class OrderServiceImpl implements OrderService {

    @Resource
    private OrderMapper orderMapper;

    @Resource
    private UserClient userClient;

    @Override
    public Order queryOrderById(Long orderId) {
        //1.查询订单
        Order order = orderMapper.findById(orderId);
        //2.用 Feign 发起远程调用
        User user = userClient.findById(order.getUserId());
        //3.封装User到Order
        order.setUser(user);
        //4.返回
        return order;
    }

    /*@Resource
    private RestTemplate restTemplate;

    @Override
    public Order queryOrderById(Long orderId) {
        //1.查询订单
        Order order = orderMapper.findById(orderId);
        //2.利用RestTemplate发起http请求
        //2.1拼接url路径
        //String url = "http://localhost:8864/user/" + order.getUserId();
        String url = "http://userService/user/" + order.getUserId();
        //2.2发送http请求，实现远程调用
        User user = restTemplate.getForObject(url, User.class);
        //3.封装User到Order
        order.setUser(user);
        //4.返回
        return order;
    }*/
}
