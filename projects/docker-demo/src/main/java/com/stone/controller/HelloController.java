package com.stone.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hello")
public class HelloController {

    private static int visitCount;

    @GetMapping("/count")
    public String visitCount() {
        return "今天被访问了" + ++visitCount + "次";
    }
}
