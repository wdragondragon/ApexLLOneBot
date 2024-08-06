package com.jdragon.apex.listener;


import com.jdragon.apex.entity.ApexStatusUserInfo;
import com.jdragon.apex.handle.ApexUserInfoHandler;
import com.jdragon.apex.service.AgCareBanService;
import com.jdragon.cqhttp.CqListener;
import com.jdragon.cqhttp.constants.MessageType;
import com.jdragon.cqhttp.entity.Message;
import com.jdragon.cqhttp.message.ChatMessage;
import com.jdragon.cqhttp.service.MessageService;
import com.jdragon.test.OCRExample;
import lombok.extern.slf4j.Slf4j;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Component
public class CareMessageListener {

    private final ApexUserInfoHandler apexUserInfoHandler;

    private final MessageService messageService;

    private final AgCareBanService agCareBanService;

    public CareMessageListener(ApexUserInfoHandler apexUserInfoHandler, MessageService messageService, AgCareBanService agCareBanService) {
        this.apexUserInfoHandler = apexUserInfoHandler;
        this.messageService = messageService;
        this.agCareBanService = agCareBanService;
    }

    @CqListener(type = MessageType.CHAT_MESSAGE, subType = "group")
    public void getApexUserInfo(final ChatMessage message) {
        String rawMessage = message.getRawMessage();
        if (rawMessage.startsWith("apex查询")) {
            String param = rawMessage.replaceAll("apex查询", "").trim();
            ApexStatusUserInfo userInfo = apexUserInfoHandler.getUserInfo(param);
            if (userInfo != null) {
                messageService.sendGroupMsg(message.getMessageId(), message.getGroupId(), userInfo.toString());
            } else {
                messageService.sendGroupMsg(message.getMessageId(), message.getGroupId(), "未查询到该玩家");
            }
        }
    }


    @CqListener(type = MessageType.CHAT_MESSAGE, subType = "group")
    public void care(final ChatMessage message) {
        long msgId = message.getMessageId();
        long groupId = message.getGroupId();
        long userId = message.getUserId();
        if (message.getMessage().length != 1) {
            return;
        }
        String rawMessage = message.getRawMessage();
        String regex = "关注(\\D+?)(\\d+)";
        Matcher matcher = Pattern.compile(regex).matcher(rawMessage);
        if (matcher.find()) {
            String careType = matcher.group(1);
            if (!Arrays.asList("封禁", "活跃").contains(careType)) {
                messageService.sendGroupMsg(message.getMessageId(), message.getGroupId(), "只允许关注[封禁|活跃]");
                return;
            }
            String value = matcher.group(2);
            ApexStatusUserInfo userInfo = apexUserInfoHandler.getUserInfo(value);
            if (userInfo == null) {
                messageService.sendGroupMsg(msgId, groupId, "用户未找到，关注失败");
                return;
            }
            if (agCareBanService.careUid(groupId, userId, userInfo.getUid(), careType)) {
                messageService.sendGroupMsg(msgId, groupId, "关注成功");
            } else {
                messageService.sendGroupMsg(msgId, groupId, "已关注，请勿重复关注");
            }
        }
    }


    @CqListener(type = MessageType.CHAT_MESSAGE, subType = "group")
    public void careOcr(final ChatMessage message) throws TesseractException, IOException {
        Message[] messageArr = message.getMessage();
        if (messageArr.length == 2 && messageArr[0].getType().equals("text") && messageArr[1].getType().equals("image")) {
            String text = messageArr[0].getData().get("text").toString();
            if (text.trim().startsWith("关注")) {
                String careType = text.replaceAll("关注", "");
                if (!Arrays.asList("封禁", "活跃").contains(careType)) {
                    messageService.sendGroupMsg(message.getMessageId(), message.getGroupId(), "只允许关注[封禁|活跃]");
                    return;
                }
                String url = messageArr[1].getData().get("url").toString();
                String orcToString = OCRExample.orcToString(url).trim();
                Matcher matcher = Pattern.compile("ID:\\s*(\\d+)").matcher(orcToString);
                if (matcher.find()) {
                    String id = matcher.group(1).trim();
                    ApexStatusUserInfo userInfo = apexUserInfoHandler.getUserInfo(id);
                    if (userInfo == null) {
                        messageService.sendGroupMsg(message.getMessageId(), message.getGroupId(), "用户未找到，关注失败");
                        return;
                    }
                    if (agCareBanService.careUid(message.getGroupId(), message.getUserId(), userInfo.getUid(), careType)) {
                        messageService.sendGroupMsg(message.getMessageId(), message.getGroupId(), String.format("识别到uid图片，关注%s成功", id));
                    } else {
                        messageService.sendGroupMsg(message.getMessageId(), message.getGroupId(), String.format("识别到uid图片，已关注%s，请勿重复关注", id));
                    }
                } else {
                    messageService.sendGroupMsg(message.getMessageId(), message.getGroupId(), "请发送正确的uid截图");
                }
            }
        }
    }
}
