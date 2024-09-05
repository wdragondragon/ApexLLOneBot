package com.jdragon.cqhttp.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.jdragon.cqhttp.client.CqHttpClient;
import com.jdragon.cqhttp.config.ObjectMapperHolder;
import com.jdragon.cqhttp.entity.*;
import com.jdragon.cqhttp.entity.msg.AtMessage;
import com.jdragon.cqhttp.entity.msg.ImageMessage;
import com.jdragon.cqhttp.entity.msg.ReplyMessage;
import com.jdragon.cqhttp.entity.msg.TextMessage;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class MessageService {
    private final CqHttpClient messageClient;

    public MessageService(CqHttpClient messageClient) {
        this.messageClient = messageClient;
    }

    public void sendPrivateMsg(long userId, Message... messages) {
        SendPrivateMsg sendPrivateMsg = new SendPrivateMsg();
        sendPrivateMsg.setUser_id(userId);
        sendPrivateMsg.setMessage(messages);
        String result = messageClient.sendPrivateMsg(sendPrivateMsg);
        log.info("发送私聊信息结果：{}", result);
    }

    public void sendGroupMsg(long groupId, Message... messages) {
        SendGroupMsg sendGroupMsg = new SendGroupMsg();
        sendGroupMsg.setGroup_id(groupId);
        sendGroupMsg.setMessage(messages);
        String result = messageClient.sendGroupMsg(sendGroupMsg);
        log.info("发送群聊信息结果：{}", result);
    }


    @SneakyThrows
    public void sendPrivateMsg(long userId, String text) {
        sendPrivateMsg(userId, new TextMessage(text));
    }

    public void sendGroupPic(Long msgId, Long groupId, String message, byte[] imageBytes) {
        List<Message> messages = new ArrayList<>();
        if (msgId != null) {
            Message replyMsg = new ReplyMessage(msgId);
            messages.add(replyMsg);
        }
        if (StringUtils.isNotBlank(message)) {
            Message msg = new TextMessage(message);
            messages.add(msg);
        }
        // 将图片字节数组转换为Base64字符串
        String base64Image = Base64.getEncoder().encodeToString(imageBytes);
        Message picMessage = new ImageMessage("base64://" + base64Image);
        messages.add(picMessage);
        sendGroupMsg(groupId, messages.toArray(new Message[0]));
    }

    @SneakyThrows
    public void sendGroupMsg(Long msgId, Long groupId, List<Long> atList, String text) {
        List<Message> messages = new ArrayList<>();
        if (msgId != null) {
            Message replyMsg = new ReplyMessage(msgId);
            messages.add(replyMsg);
        }
        for (Long at : atList) {
            Message atMsg = new AtMessage(at);
            messages.add(atMsg);
        }
        Message message = new TextMessage(text);
        messages.add(message);
        sendGroupMsg(groupId, messages.toArray(new Message[0]));
    }

    @SneakyThrows
    public void sendGroupMsg(Long msgId, Long groupId, String text) {
        if (msgId == null) {
            sendGroupMsg(groupId, new TextMessage(text));
        } else {
            sendGroupMsg(groupId, new ReplyMessage(msgId), new TextMessage(text));
        }
    }

    @SneakyThrows
    public CqResult<List<GroupMember>> getGroupMemberList(Long groupId) {
        String jsonTemplate = """
                {
                    "group_id": %d
                }
                """;
        String json = String.format(jsonTemplate, groupId);
        Map<String, Object> request = ObjectMapperHolder.MAPPER.readValue(json, new TypeReference<>() {
        });
        return messageClient.getGroupMemberList(request);
    }
}
