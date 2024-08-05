package com.jdragon.apex.config;

import feign.Client;

import feign.RequestInterceptor;
import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetSocketAddress;
import java.net.Proxy;

@Configuration
public class ProxyFeignConfig {
    @Bean
    public Client feignClient() {
        return new feign.okhttp.OkHttpClient(okHttpClient());
    }

    public OkHttpClient okHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        // 配置代理
        builder.proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 7890)));
        return builder.build();
    }

    @Bean
    public RequestInterceptor proxyRequestInterceptor() {
        return requestTemplate -> requestTemplate.header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/127.0.0.0 Safari/537.36");
    }
}
