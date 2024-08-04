package com.jdragon.apex.listener;

import com.jdragon.apex.entity.AgBanHistory;
import com.jdragon.apex.entity.vo.TodayBanStatic;
import com.jdragon.apex.handle.ApexStatusHandler;
import com.jdragon.apex.mapper.AgBanHistoryMapper;
import com.jdragon.apex.service.AgBanHistoryService;
import com.jdragon.cqhttp.CqListener;
import com.jdragon.cqhttp.constants.MessageType;
import com.jdragon.cqhttp.message.ChatMessage;
import com.jdragon.cqhttp.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class CareBanMessageListener {

    private final AgBanHistoryService agBanHistoryService;

    private final MessageService messageService;

    private final AgBanHistoryMapper banHistoryMapper;

    public CareBanMessageListener(AgBanHistoryService agBanHistoryService, MessageService messageService, AgBanHistoryMapper banHistoryMapper, ApexStatusHandler apexStatusHandler) {
        this.agBanHistoryService = agBanHistoryService;
        this.messageService = messageService;
        this.banHistoryMapper = banHistoryMapper;
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

    @CqListener(type = MessageType.CHAT_MESSAGE, subType = "group")
    public void banStatic(final ChatMessage message) {
        if (message.getRawMessage().equals("今天封禁统计")) {
            List<TodayBanStatic> todayBanStatics = banHistoryMapper.todayBanStatic();
            String collect = todayBanStatics.stream().map(TodayBanStatic::toString).collect(Collectors.joining("\n"));
            if (StringUtils.isBlank(collect)) {
                messageService.sendGroupMsg(message.getMessageId(), message.getGroupId(), "今天暂无人被封禁");
            } else {
                messageService.sendGroupMsg(message.getMessageId(), message.getGroupId(), collect);
            }
        }
    }
}
