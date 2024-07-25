package com.jdragon.cqhttp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jdragon.cqhttp.config.ObjectMapperHolder;
import com.jdragon.cqhttp.handler.MessageHandler;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@EnableFeignClients
@ComponentScan
@Configuration
public class CqHttpListenerAutoConfig {
    @Bean
    public CqHttpMessageListenerScanPostProcesser cqHttpMessageListenerScanPostProcesser() {
        return new CqHttpMessageListenerScanPostProcesser();
    }

    @Bean
    public MessageHandler messageHandler() {
        return new MessageHandler();
    }
}
