package com.ustc.ruoan.framework.web.controller;

import com.ustc.ruoan.framework.web.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ruoan
 */
@RestController
@RequestMapping("/core")
@Slf4j
public class HelloController {

    @Autowired
    private RedisService redisService;

    @GetMapping("/hello")
    public String hello() throws Exception {
        log.info("i am coming");
        return redisService.get("hello");
    }
}