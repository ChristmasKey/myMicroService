package com.djn.order.config;

import feign.Logger;
import org.springframework.context.annotation.Bean;

/**
 * Name: DefaultFeignConfiguration
 * Description: Feign配置类
 * Copyright: Copyright (c) 2024 MVWCHINA All rights Reserved
 * Company: 江苏医视教育科技发展有限公司
 *
 * @author 丁佳男
 * @version 1.0
 * @since 2024-01-08 15:42
 */
public class DefaultFeignConfiguration {
    @Bean
    public Logger.Level feignLogLevel() {
        return Logger.Level.BASIC;
    }
}
