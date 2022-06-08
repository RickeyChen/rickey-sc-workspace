package com.lagou.edu.controller;

import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequestMapping("/autoDeliver")
public class AutoDeliverController {

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private DiscoveryClient discoveryClient;

//    @GetMapping("/checkState/{userId}")
//    public Integer findResumeOpenState(@PathVariable Long userId) {
//        // 调用远程服务 -> 简历Resume微服务接口 RestTemplate -> JdbcTemplate
//        Integer forObject = restTemplate.getForObject("http://localhost:8080/resume/openstate/" + userId, Integer.class);
//        return forObject;
//    }

    /**
     * 服务注册到Eureka之后的改造
     *
     * */
//    @GetMapping("/checkState/{userId}")
//    public Integer findResumeOpenState(@PathVariable Long userId) {
//        // 从Eureka Server中获取我们关注的那个服务的实例信息和接口信息
//        // 1. 从Eureka Server获取lagou-service-resume实例信息（使用discoveryClient）;
//        List<ServiceInstance> instances = discoveryClient.getInstances("lagou-service-resume");
//        // 2. 如果有多个实例，选择一个使用;LoadBalance
//        ServiceInstance serviceInstance = instances.get(0);
//        // 3. 元数据信息中获取host, port, etc...
//        String host = serviceInstance.getHost();
//        int port = serviceInstance.getPort();
//        StringBuilder url = new StringBuilder("http://")
//                .append(host)
//                .append(":")
//                .append(port)
//                .append("/resume/openstate/")
//                .append(userId);
//        System.out.println("获取的url===>>>    " + url);
//        // 调用远程服务 -> 简历Resume微服务接口 RestTemplate -> JdbcTemplate
//        Integer forObject = restTemplate.getForObject(url.toString(), Integer.class);
//        return forObject;
//    }

    /**
     * 测试Ribbon负载均衡
     */
    @GetMapping("/checkState/{userId}")
    public Integer findResumeOpenState(@PathVariable Long userId) {
        // 使用ribbon自己获取服务
        String url = "http://lagou-service-resume/resume/openstate/" + userId;
        Integer forObject = restTemplate.getForObject(url, Integer.class);
        return forObject;
    }

    /**
     * 提供者resume模拟超时，测试Hystrix控制
     */
    @HystrixCommand(
            // 线程池标识要保持唯一，否则公用
            threadPoolKey = "findResumeOpenStateTimeout",
            // 线程池细节属性
            threadPoolProperties = {
                    @HystrixProperty(name = "coreSize", value = "1"), //线程数
                    @HystrixProperty(name = "maxQueueSize", value = "20") // 等待队列长度
            },
            // 使用Hystrix，超过目标秒就熔断
            commandProperties = {
                    @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "2000")
            }
    )
    @GetMapping("/checkStateTimeout/{userId}")
    public Integer findResumeOpenStateTimeout(@PathVariable Long userId) {
        // 使用ribbon自己获取服务
        String url = "http://lagou-service-resume/resume/openstate/" + userId;
        Integer forObject = restTemplate.getForObject(url, Integer.class);
        return forObject;
    }

    /**
     * 1) 服务提供者超市，熔断，返回错误信息
     * 2） 服务提供者直接抛出异常
     * --以上都是不希望直接返回给用户--
     */
    @HystrixCommand(
            // 线程池标识要保持唯一，否则公用
            threadPoolKey = "findResumeOpenStateFallback",
            // 线程池细节属性
            threadPoolProperties = {
                    @HystrixProperty(name = "coreSize", value = "2"), //线程数
                    @HystrixProperty(name = "maxQueueSize", value = "20") // 等待队列长度
            },
            commandProperties = {
                    @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "2000"),
                    // 高级配置
                    @HystrixProperty(name = "metrics.rollingStats.timeInMilliseconds", value = "8000"), // 统计时间窗口长度
                    @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "2"), // 统计时间内的最小请求数
                    @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "50"), // 统计时间窗口内的错误数量百分比阈值
                    @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "3000") // 自我修复时间长度窗口
            },
            fallbackMethod = "myFallBack"
    )
    @GetMapping("/checkStateFallback/{userId}")
    public Integer findResumeOpenStateFallback(@PathVariable Long userId) {
        // 使用ribbon自己获取服务
        String url = "http://lagou-service-resume/resume/openstate/" + userId;
        Integer forObject = restTemplate.getForObject(url, Integer.class);
        return forObject;
    }

    // 定义返回默认值； 该方法的形参和返回值与原始方法保持一致
    public Integer myFallBack(Long userId) {
        return -1;
    }


}
