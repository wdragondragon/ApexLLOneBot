package com.jdragon.apex.client;

import com.jdragon.apex.config.ProxyFeignConfig;
import com.jdragon.apex.entity.vo.DiscordMessage;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "discordClient", url = "https://discord.com/api/v9", configuration = ProxyFeignConfig.class)
public interface DiscordClient {

    @GetMapping("/channels/{channelId}/messages")
    List<DiscordMessage> getDiscordMessage(@PathVariable("channelId") String channelId,
                                           @RequestParam("limit") Integer limit,
                                           @RequestHeader("authorization") String authorization);
}
