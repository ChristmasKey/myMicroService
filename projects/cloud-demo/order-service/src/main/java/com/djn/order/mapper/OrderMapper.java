package com.djn.order.mapper;

import com.djn.order.domain.Order;
import org.apache.ibatis.annotations.Param;

/**
 * Name: OrderMapper
 * Description: 订单Mapper
 * Copyright: Copyright (c) 2023 MVWCHINA All rights Reserved
 * Company: 江苏医视教育科技发展有限公司
 *
 * @author 丁佳男
 * @version 1.0
 * @since 2023-11-22 16:07
 */
public interface OrderMapper {

    Order findById(@Param("orderId") Long orderId);

    int insertOperInfo(@Param("operTime") String operTime, @Param("operType") String operType);
}
