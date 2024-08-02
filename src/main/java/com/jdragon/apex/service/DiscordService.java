package com.jdragon.apex.service;


import com.jdragon.apex.client.DiscordClient;
import com.jdragon.apex.entity.vo.DiscordMessage;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class DiscordService {
    private final DiscordClient discordClient;

    @Setter
    @Value("${dc.auth}")
    private String authorization;

    public DiscordService(DiscordClient discordClient) {
        this.discordClient = discordClient;
    }

    public List<DiscordMessage> getDiscordMessages(String channelId, Integer limit) {
        return discordClient.getDiscordMessage(channelId, limit, authorization);
    }
}
