package com.jdragon.apex.listener;

import com.jdragon.apex.entity.vo.PlayerInfo;
import com.jdragon.apex.handle.ApexLegendsLeaderboards;
import com.jdragon.cqhttp.CqListener;
import com.jdragon.cqhttp.constants.MessageType;
import com.jdragon.cqhttp.message.ChatMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class ApexStatusMessageListener {

    private final ApexLegendsLeaderboards apexLegendsLeaderboards;

    public ApexStatusMessageListener(ApexLegendsLeaderboards apexLegendsLeaderboards) {
        this.apexLegendsLeaderboards = apexLegendsLeaderboards;
    }

    @CqListener(type = MessageType.CHAT_MESSAGE, subType = "group")
    public void leaderBoards(final ChatMessage message) {
        String rawMessage = message.getRawMessage();
        String[] split = rawMessage.split("\\s+");
        if (split.length == 2 && split[0].equals("查询在线高手")) {
            List<PlayerInfo> playerInfos = apexLegendsLeaderboards.getLegendsLeaderboards();
            String reply = playerInfos.stream()
                    .filter(playerInfo -> Integer.parseInt(playerInfo.level()) < Integer.parseInt(split[1]))
                    .filter(playerInfo -> !playerInfo.onlineStatus().equals("offline"))
                    .map(PlayerInfo::toString)
                    .collect(Collectors.joining("\n"));
            message.reply(reply);
        }
    }
}
