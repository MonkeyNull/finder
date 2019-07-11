package com.monkey.finder.find.interceptor;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * @author: 李怀君
 * @date: 2018/7/11 10:40
 * @desc:
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Resource
    private FirstInterceptor firstInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 多个拦截器组成一个拦截器链
        // addPathPatterns 用于添加拦截规则
        // excludePathPatterns 用户排除拦截
        //拦截以/为前缀的所有请求
        registry.addInterceptor(firstInterceptor).addPathPatterns("/**");
    }

}
