package com.jdragon.apex.listener;

import com.jdragon.apex.entity.AgBanHistory;
import com.jdragon.apex.entity.AgCareBan;
import com.jdragon.apex.entity.vo.TodayBanStatic;
import com.jdragon.apex.mapper.AgBanHistoryMapper;
import com.jdragon.apex.mapper.AgCareBanMapper;
import com.jdragon.apex.service.AgBanHistoryService;
import com.jdragon.apex.service.AgCareBanService;
import com.jdragon.cqhttp.CqListener;
import com.jdragon.cqhttp.constants.MessageType;
import com.jdragon.cqhttp.message.ChatMessage;
import com.jdragon.cqhttp.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
@Component
public class BanMessageListener {

    private final AgBanHistoryService agBanHistoryService;

    private final MessageService messageService;

    private final AgCareBanService agCareBanService;

    private final AgBanHistoryMapper banHistoryMapper;

    public BanMessageListener(AgBanHistoryService agBanHistoryService, MessageService messageService, AgCareBanService agCareBanService, AgCareBanMapper agCareBanMapper, AgBanHistoryMapper banHistoryMapper) {
        this.agBanHistoryService = agBanHistoryService;
        this.messageService = messageService;
        this.agCareBanService = agCareBanService;
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
    public void careBan(final ChatMessage message) {
        long msgId = message.getMessageId();
        long groupId = message.getGroupId();
        long userId = message.getUserId();
        String rawMessage = message.getRawMessage();
        String regex = "关注封禁(\\d+)";
        Matcher matcher = Pattern.compile(regex).matcher(rawMessage);
        if (matcher.find()) {
            String value = matcher.group(1);
            AgCareBan agCareBan = new AgCareBan();
            agCareBan.setCareType("uid");
            agCareBan.setCareValue(value);
            agCareBan.setGroupId(String.valueOf(groupId));
            agCareBan.setUserId(String.valueOf(userId));

            AgCareBan one = agCareBanService.getOne(agCareBan);
            if (one == null) {
                agCareBanService.save(agCareBan);
                messageService.sendGroupMsg(msgId, groupId, "关注成功");
            } else {
                messageService.sendGroupMsg(msgId, groupId, "已关注，请勿重复关注");
            }
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
