package com.jdragon.apex.config;


import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApexApiFeignConfig {

    @Value("${apex.auth}")
    private String auth;

    @Bean
    public RequestInterceptor apexApiRequestInterceptor() {
        return template -> template.query("auth", auth);
    }
}
