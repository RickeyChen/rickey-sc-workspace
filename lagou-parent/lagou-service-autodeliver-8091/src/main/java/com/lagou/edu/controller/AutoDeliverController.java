package com.lagou.edu.controller;

import com.lagou.edu.service.ResumeFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/autoDeliver")
public class AutoDeliverController {

    @Qualifier("com.lagou.edu.service.ResumeFeignClient")
    @Autowired
    private ResumeFeignClient resumeFeignClient;

    /**
     * 改用Feign方式调用
     * 调用的是本地方法，实际是远程调用
     * */
    @GetMapping("/checkState/{userId}")
    public Integer findResumeOpenState(@PathVariable Long userId) {
        Integer defaultResumeState = resumeFeignClient.findDefaultResumeState(userId);
        return defaultResumeState;
    }

}
