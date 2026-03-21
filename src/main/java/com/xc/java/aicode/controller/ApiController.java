package com.xc.java.aicode.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController            // 1. 声明这是个接口类，返回数据而非页面
@RequestMapping("/api")    // 2. 定义该类下所有接口的统一前缀
public class ApiController {

    @RequestMapping("/hello") // 3. 访问路径为 /api/hello
    public String sayHello() {
        return "Hello World"; // 直接返回字符串给前端
    }

}
