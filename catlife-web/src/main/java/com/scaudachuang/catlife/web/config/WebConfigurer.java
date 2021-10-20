package com.scaudachuang.catlife.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author hiluyx
 * @since 2021/7/11 21:15
 **/
@Configuration
public class WebConfigurer implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SelfOnlineInterceptor())
                .addPathPatterns("/self/**").excludePathPatterns("/self/login", "/self/catLife/aCatRecord");
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
