package com.lagou.edu.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Component
public class BlackListFilter implements GlobalFilter, Ordered {

    // 模拟黑名单
    private static List<String> blackList = new ArrayList<>();

    static {
        blackList.add("0:0:0:0:0:0:0:1");
    }

    /**
     * 过滤器核心方法
     * @param exchange 封装了request和reponse对象的上下文
     * @param chain 网关过滤器，包含全局过滤器和单路由过滤器
     * @return
     * */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 获取客户端ip，在list中不给通过
        // 1. 取出request和response
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        // 获取ip
        String hostString = request.getRemoteAddress().getHostString();
        if (blackList.contains(hostString)){
            String data = "ACCESS DENY" + hostString;
            System.out.println(data);
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            DataBuffer wrap = response.bufferFactory().wrap(data.getBytes(StandardCharsets.UTF_8));
            return response.writeWith(Mono.just(wrap));

        }

        System.out.println("ACCESS PERMIT" + hostString);
        return chain.filter(exchange);
    }


    /**
     * 返回值表示当前该过滤器的优先级；数值越小，优先级越高
     * */
    @Override
    public int getOrder() {
        return 0;
    }
}
