package com.lagou.edu.service;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

// 表明当前类是Feign客户端，value为注册中心上的服务名称
@FeignClient(value = "lagou-service-resume", fallback = ResumeFallback.class, path = "/resume")
public interface ResumeFeignClient {

    /**
     * Feign做的事：拼接url
     * */
    @RequestMapping(value = "/openstate/{userId}", method = RequestMethod.GET)
    public Integer findDefaultResumeState(@PathVariable("userId") Long userId);

}
