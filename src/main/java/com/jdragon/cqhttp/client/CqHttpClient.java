package com.jdragon.cqhttp.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(name = "messageClient", url = "http://localhost:3000")
public interface CqHttpClient {

    @PostMapping("/send_private_msg")
    void sendPrivateMsg(@RequestBody Map<String, Object> request);

    @PostMapping("/send_group_msg")
    void sendGroupMsg(@RequestBody Map<String, Object> request);

}
