package com.djn.order.service;

import com.djn.order.domain.Order;

/**
 * Name: OrderService
 * Description: 订单Service
 * Copyright: Copyright (c) 2023 MVWCHINA All rights Reserved
 * Company: 江苏医视教育科技发展有限公司
 *
 * @author 丁佳男
 * @version 1.0
 * @since 2023-11-22 16:08
 */
public interface OrderService {

    Order queryOrderById(Long orderId);
}
