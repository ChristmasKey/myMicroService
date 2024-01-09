package com.djn.user.controller;

import com.djn.user.config.PatternProperties;
import com.djn.user.domain.User;
import com.djn.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Name: UserController
 * Description: 用户Controller
 * Copyright: Copyright (c) 2023 MVWCHINA All rights Reserved
 * Company: 江苏医视教育科技发展有限公司
 *
 * @author 丁佳男
 * @version 1.0
 * @since 2023-11-22 15:27
 */

@Slf4j
@RestController
@RequestMapping("/user")
//@RefreshScope
public class UserController {

    @Resource
    private UserService userService;

    /**
     * 通过 @Value 注解获取 application.yml 中的配置
     */
    //@Value("${pattern.dateformat}")
    //private String dateformat;

    @Resource
    private PatternProperties properties;

    @GetMapping("/prop")
    public PatternProperties getProperties() {
        return properties;
    }

    /**
     * 此接口用于验证服务是否从 Nacos 中获取到了配置
     *
     * @return java.lang.String
     * @author SpringStone
     * @date 2023-12-24 16:55
     */
    @GetMapping("/now")
    public String now() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern(properties.getDateformat()));
    }

    @GetMapping("/{id}")
    public User queryById(@PathVariable("id") Long id, @RequestHeader(value = "Truth", required = false) String truth) {
        System.out.println("truth: " + truth);
        return userService.queryById(id);
    }
}
