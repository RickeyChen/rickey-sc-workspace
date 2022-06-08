package com.lagou.edu;

import com.netflix.hystrix.contrib.metrics.eventstream.HystrixMetricsStreamServlet;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableDiscoveryClient
@EnableHystrix
@EnableCircuitBreaker
//@SpringCloudApplication // 综合性注解=SpringBootApplication+EnableDiscoveryClient
public class AutoDeliverApplication8090 {

    public static void main(String[] args) {
        SpringApplication.run(AutoDeliverApplication8090.class, args);
    }

    // 使用RestTemplate模板对象进行远程调用
    @Bean
    @LoadBalanced //使用负载均衡
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

    /**
     * 给被监控的微服务中国注册一个servlet， 后期访问此servlet来获取该服务Hystrix监控数据
     * 前提： 被检控的微服务需要引入spring boot的actuator肝功能
     */
    @Bean
    public ServletRegistrationBean getServlet() {
        HystrixMetricsStreamServlet streamServlet = new HystrixMetricsStreamServlet();
        ServletRegistrationBean registrationBean = new ServletRegistrationBean(streamServlet);
        registrationBean.setLoadOnStartup(1);
        registrationBean.addUrlMappings("/actuator/hystrix.stream");
        registrationBean.setName("HystrixMetricsStreamServlet");
        return registrationBean;
    }


}
