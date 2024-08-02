package com.jdragon.apex.listener;

import com.jdragon.apex.entity.AgBanHistory;
import com.jdragon.apex.service.AgBanHistoryService;
import com.jdragon.cqhttp.CqListener;
import com.jdragon.cqhttp.constants.MessageType;
import com.jdragon.cqhttp.message.ChatMessage;
import com.jdragon.cqhttp.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class BanMessageListener {

    private final AgBanHistoryService agBanHistoryService;

    private final MessageService messageService;

    public BanMessageListener(AgBanHistoryService agBanHistoryService, MessageService messageService) {
        this.agBanHistoryService = agBanHistoryService;
        this.messageService = messageService;
    }

    @CqListener(type = MessageType.CHAT_MESSAGE, subType = "group")
    public void onChatMessage(final ChatMessage message) {
        long msgId = message.getMessageId();
        long groupId = message.getGroupId();
        String rawMessage = message.getRawMessage();
        String[] split = rawMessage.split(" ");
        if (split.length == 2 && split[0].equals("查封")) {
            String uid = split[1].trim();
            AgBanHistory agBanHistory = agBanHistoryService.queryBanHistoryByUid(uid);
            String msg = "未查到该uid的封禁记录";
            if (agBanHistory != null) {
                msg = agBanHistory.toString();
            }
            messageService.sendGroupMsg(msgId, groupId, msg);
        }
    }

}
