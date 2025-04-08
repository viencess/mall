package com.hmall.api.config;

import cn.hutool.core.util.StrUtil;
import com.hmall.api.client.fallback.ItemClientFallback;
import com.hmall.common.utils.UserContext;
import feign.Logger;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Bean;

public class DefaultFeignConfig {
    @Bean
    public Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

    @Bean
    public RequestInterceptor userInfoRequestInterceptor() {
        return new RequestInterceptor() {
            @Override
            public void apply(RequestTemplate requestTemplate) {
                String userInfo = UserContext.getUser().toString();
                if (StrUtil.isNotBlank(userInfo)) {
                    requestTemplate.header("user-info", userInfo);
                }
            }
        };
    }

    @Bean
    public ItemClientFallback itemClientFallback() {
        return new ItemClientFallback();
    }
}
