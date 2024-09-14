package com.jdragon.apex.client;

import com.jdragon.apex.config.ProxyFeignConfig;
import com.jdragon.apex.entity.vo.DiscordMessage;
import com.jdragon.apex.entity.vo.UploadAttachments;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "discordClient", url = "https://discord.com/api/v9", configuration = ProxyFeignConfig.class)
public interface DiscordClient {

    @GetMapping("/channels/{channelId}/messages")
    List<DiscordMessage> getDiscordMessage(@PathVariable("channelId") String channelId,
                                           @RequestParam("limit") Integer limit,
                                           @RequestHeader("authorization") String authorization);

    @PostMapping("/channels/{channelId}/messages")
    DiscordMessage sendDiscordMessage(@PathVariable("channelId") String channelId,
                                      @RequestBody DiscordMessage message,
                                      @RequestHeader("authorization") String authorization);


    @PostMapping("/channels/{channelId}/attachments")
    DiscordMessage sendDiscordAttachments(@PathVariable("channelId") String channelId,
                                          @RequestBody UploadAttachments message,
                                          @RequestHeader("authorization") String authorization);

}
