package com.djn.order.clients;

import com.djn.order.config.DefaultFeignConfiguration;
import com.djn.order.domain.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Name: UserClient
 * Description: UserService的请求客户端
 * Copyright: Copyright (c) 2024 MVWCHINA All rights Reserved
 * Company: 江苏医视教育科技发展有限公司
 *
 * @author 丁佳男
 * @version 1.0
 * @since 2024-01-08 12:29
 */
// @FeignClient(value = "userservice", configuration = DefaultFeignConfiguration.class)
@FeignClient(value = "userservice")
public interface UserClient {

    @GetMapping("/user/{id}")
    User findById(@PathVariable("id") Long id);
}
