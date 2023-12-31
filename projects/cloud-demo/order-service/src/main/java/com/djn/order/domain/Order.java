package com.djn.order.domain;

import lombok.Data;
import com.djn.feign.domain.User;

/**
 * Name: Order
 * Description: 订单
 * Copyright: Copyright (c) 2023 MVWCHINA All rights Reserved
 * Company: 江苏医视教育科技发展有限公司
 *
 * @author 丁佳男
 * @version 1.0
 * @since 2023-11-22 16:07
 */
@Data
public class Order {
    private Long id;
    private Long userId;
    private String name;
    private Double price;
    private Integer num;

    private User user;
}
