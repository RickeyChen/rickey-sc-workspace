package com.lagou.edu.service;

import org.springframework.stereotype.Component;

/**
 * 降级回退逻辑；
 * 实现ResumeFeignClient接口；
 * 实现接口中的所有方法；
 * */
@Component
public class ResumeFallback implements ResumeFeignClient {

    @Override
    public Integer findDefaultResumeState(Long userId) {
        return 1997;
    }

}
