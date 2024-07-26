package com.jdragon.apex.listener;

import com.jdragon.apex.entity.AgMachinesKeys;
import com.jdragon.apex.mapper.AgMachineNewMapper;
import com.jdragon.apex.service.AgMachineKeysService;
import com.jdragon.apex.utils.ExpirationTimeCalculator;
import com.jdragon.cqhttp.CqListener;
import com.jdragon.cqhttp.constants.MessageType;
import com.jdragon.cqhttp.entity.Message;
import com.jdragon.cqhttp.message.ChatMessage;
import com.jdragon.cqhttp.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RegExUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.regex.Matcher;

@Slf4j
@Component
public class CMessageListener {

    private final MessageService messageService;

    private final AgMachineKeysService agMachineKeysService;

    private final AgMachineNewMapper agMachineNewMapper;

    public CMessageListener(MessageService messageService, AgMachineKeysService agMachineKeysService, AgMachineNewMapper agMachineNewMapper) {
        this.messageService = messageService;
        this.agMachineKeysService = agMachineKeysService;
        this.agMachineNewMapper = agMachineNewMapper;
    }

    @CqListener(type = MessageType.CHAT_MESSAGE, subType = "group")
    public void checkAgAuth(final ChatMessage message) {
        long groupId = message.getGroupId();
        Message[] messageArray = message.getMessage();
        if (messageArray[0].getType().equals("at") && messageArray.length == 2) {
            String qq = String.valueOf(messageArray[0].getData().get("qq"));
            String text = String.valueOf(messageArray[1].getData().get("text"));
            if ("查询ag授权".equals(text)) {
                messageService.sendGroupMsg(groupId, getAuthStr("qq", qq));
            }
        }
    }

    @CqListener(type = MessageType.CHAT_MESSAGE, subType = "group")
    public void createExperienceKey(final ChatMessage message) {
        long userId = message.getUserId();
        long groupId = message.getGroupId();
        long selfId = message.getSelfId();
        Matcher matcher = RegExUtils.dotAllMatcher("获取(.*)体验卡", message.getRawMessage());
        if (matcher.find()) {
            String validateType = matcher.group(1);
            String key = agMachineKeysService.createExperienceCardByQQ(String.valueOf(userId), String.valueOf(groupId), validateType);
            String retMsg = String.format("获取%s体验卡成功，卡密为：%s", validateType, key);
            messageService.sendPrivateMsg(userId, retMsg);
            String selfMsg = String.format("群号[%s]，用户qq[%s]申请了一张%s体验卡，卡密为：%s", groupId, userId, validateType, key);
            messageService.sendPrivateMsg(selfId, selfMsg);
        }
    }

    public String getAuthStr(String type, String condition) {
        List<AgMachinesKeys> authList = agMachineNewMapper.getAuthList(type, condition, null);
        if (authList.isEmpty()) {
            return "没有与你相关的授权信息";
        }

        StringBuilder authStr = new StringBuilder();
        for (AgMachinesKeys auth : authList) {
            String authStrItem = """
                    [授权类型：%s，授权时效：%s，机器码：%s]
                    """;
            authStr.append(String.format(authStrItem,
                    auth.getValidateType(),
                    auth.getExpirationTime() == null ? "永久授权" : ExpirationTimeCalculator.formatDateTime(auth.getExpirationTime()),
                    auth.getMachineCode()
            ));
        }
        return authStr.toString();
    }

}
