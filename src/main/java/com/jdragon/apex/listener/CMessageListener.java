package com.jdragon.apex.listener;

import com.jdragon.apex.entity.AgMachinesKeys;
import com.jdragon.apex.mapper.AgMachineNewMapper;
import com.jdragon.apex.service.AgKeysService;
import com.jdragon.apex.service.AgMachineKeysService;
import com.jdragon.apex.utils.ExpirationTimeCalculator;
import com.jdragon.cqhttp.CqListener;
import com.jdragon.cqhttp.constants.MessageType;
import com.jdragon.cqhttp.entity.Message;
import com.jdragon.cqhttp.message.ChatMessage;
import com.jdragon.cqhttp.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RegExUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.regex.Matcher;

@Slf4j
@Component
public class CMessageListener {

    private final MessageService messageService;

    private final AgMachineKeysService agMachineKeysService;

    private final AgMachineNewMapper agMachineNewMapper;
    private final AgKeysService agKeysService;

    public CMessageListener(MessageService messageService, AgMachineKeysService agMachineKeysService, AgMachineNewMapper agMachineNewMapper, AgKeysService agKeysService) {
        this.messageService = messageService;
        this.agMachineKeysService = agMachineKeysService;
        this.agMachineNewMapper = agMachineNewMapper;
        this.agKeysService = agKeysService;
    }

    @CqListener(type = MessageType.CHAT_MESSAGE, subType = "group")
    public void checkAgAuth(final ChatMessage message) {
        long groupId = message.getGroupId();
        long msgId = message.getMessageId();
        Message[] messageArray = message.getMessage();
        if (messageArray[0].getType().equals("at") && messageArray.length == 2) {
            String qq = String.valueOf(messageArray[0].getData().get("qq"));
            String text = String.valueOf(messageArray[1].getData().get("text"));
            if ("查询ag授权".equals(text)) {
                messageService.sendGroupMsg(msgId, groupId, getAuthStr("qq", qq));
            }
        }
    }

    @CqListener(type = MessageType.CHAT_MESSAGE, subType = "group")
    public void createExperienceKey(final ChatMessage message) {
        long userId = message.getUserId();
        long groupId = message.getGroupId();
        long selfId = message.getSelfId();
        Matcher matcher = RegExUtils.dotAllMatcher("获取(.*)体验卡", message.getRawMessage().trim());
        if (matcher.find()) {
            String validateType = matcher.group(1);
            String key = agMachineKeysService.createExperienceCardByQQ(String.valueOf(userId), String.valueOf(groupId), validateType);
            String retMsg = String.format("获取%s体验卡成功，卡密为：%s", validateType, key);
            messageService.sendPrivateMsg(userId, retMsg);
            String selfMsg = String.format("群号[%s]，用户qq[%s]申请了一张%s体验卡，卡密为：%s", groupId, userId, validateType, key);
            messageService.sendPrivateMsg(selfId, selfMsg);
            messageService.sendGroupMsg(message.getMessageId(), message.getGroupId(), "卡密已私聊，请查收后妥善保管。");
        }
    }

    @CqListener(type = MessageType.CHAT_MESSAGE, subType = "group")
    public void createKey(final ChatMessage message) {
        if (!message.getSender().isAdmin()) {
            return;
        }
        long userId = message.getUserId();
        Message[] messageArray = message.getMessage();
        if (messageArray[0].getType().equals("at") && messageArray.length == 2) {
            Matcher matcher = RegExUtils.dotAllMatcher("授权(天|月|周|年|永久)卡(.*)", messageArray[1].getData().get("text").toString().trim());
            if (matcher.find()) {
                String qq = String.valueOf(messageArray[0].getData().get("qq"));
                String keyType = matcher.group(1);
                String validateType = matcher.group(2);
                String key = agKeysService.createKey(qq, keyType, validateType);
                messageService.sendPrivateMsg(userId, String.format("授权给[%s]的[%s%s卡]，卡密为:[%s]",
                        qq, validateType, keyType, key));
                messageService.sendPrivateMsg(Long.parseLong(qq), String.format("[%s]授权给你的[%s%s卡]，卡密为:[%s]",
                        userId, validateType, keyType, key));
                messageService.sendGroupMsg(message.getMessageId(), message.getGroupId(), "卡密已私聊，请查收后妥善保管。");
            }
        }
    }

    @CqListener(type = MessageType.CHAT_MESSAGE, subType = "group")
    public void help(final ChatMessage message) {
        String msg = message.getRawMessage().trim();
        if (msg.equals("/帮助") || msg.equals("/help") || msg.equals("/h")) {
            String result = """
                    可用命令：
                    1、@某人 查询ag授权
                    2、获取[ai/apex_recoils/apex_recoils_server/auto_upgrade_script]体验卡
                    3、@某人 授权[天|月|周|年|永久]卡[ai/apex_recoils/apex_recoils_server/auto_upgrade_script]
                    4、生成[数字]张[天|月|周|年|永久]卡[ai/apex_recoils/apex_recoils_server/auto_upgrade_script]
                    """;
            messageService.sendGroupMsg(message.getMessageId(), message.getGroupId(), result);
        }
    }

    @CqListener(type = MessageType.CHAT_MESSAGE, subType = "group")
    public void createKeysExt(final ChatMessage message) {
        if (!message.getSender().isAdmin()) {
            return;
        }

        Matcher matcher = RegExUtils.dotAllMatcher("生成(\\d+)张(天|月|周|年|永久)卡(.*)", message.getRawMessage().trim());
        if (matcher.find()) {
            Integer createNumber = Integer.valueOf(matcher.group(1));
            String keyType = matcher.group(2);
            String validateType = matcher.group(3);
            List<String> keyList = agKeysService.createKeysExt(createNumber, validateType, keyType);
            String keyStr = Strings.join(keyList, '\n');
            messageService.sendGroupMsg(message.getMessageId(), message.getGroupId(),
                    String.format("生成%d张%s%s卡成功，已私聊，请查收后妥善保管。", createNumber, validateType, keyType));
            messageService.sendPrivateMsg(message.getUserId(),
                    String.format("生成%d张%s%s卡成功，卡密：\n%s", createNumber, validateType, keyType, keyStr));
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
