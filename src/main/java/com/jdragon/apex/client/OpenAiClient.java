package com.jdragon.apex.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "openaiClient", url = "${api.url:https://apikeyplus.com/v1}")
public interface OpenAiClient {
    @PostMapping(value = "/chat/completions", consumes = "application/json")
    String sendMessage(@RequestHeader("Authorization") String authorization, @RequestBody String body);
}
