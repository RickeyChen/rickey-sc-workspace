package com.lagou.edu.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/config")
@RefreshScope
public class ConfigController {

    @Value("${rickey.msg}")
    private String msg;

    @GetMapping("/viewconfig")
    public String viewconfig() {
        return ">>>>>>>>>" + msg;
    }

}
