package com.stone.controller;

import cn.hutool.core.date.DateUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hello")
public class HelloController {

    private static int visitCount;

    @GetMapping("/count")
    public String visitCount() {
        System.out.println("本次访问时间为: " + DateUtil.format(DateUtil.date(), "yyyy-MM-dd HH:mm:ss"));
        return "今天被访问了" + ++visitCount + "次";
    }
}
