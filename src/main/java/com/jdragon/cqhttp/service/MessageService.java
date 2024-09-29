package com.jdragon.cqhttp.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.jdragon.cqhttp.client.CqHttpClient;
import com.jdragon.cqhttp.config.ObjectMapperHolder;
import com.jdragon.cqhttp.entity.*;
import com.jdragon.cqhttp.entity.msg.AtMessage;
import com.jdragon.cqhttp.entity.msg.TextMessage;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class MessageService {
    private final CqHttpClient messageClient;

    public MessageService(CqHttpClient messageClient) {
        this.messageClient = messageClient;
    }

    public void sendMsg(SendMsg sendMsg) {
        if (sendMsg.getGroup_id() != null) {
            String result = messageClient.sendGroupMsg(sendMsg);
            log.info("发送群聊信息结果：{}", result);
        } else {
            String result = messageClient.sendPrivateMsg(sendMsg);
            log.info("发送私聊信息结果：{}", result);
        }
    }

    public void sendPrivateMsg(long userId, Message... messages) {
        SendMsg sendPrivateMsg = new SendMsg();
        sendPrivateMsg.setUser_id(userId);
        sendPrivateMsg.setMessage(messages);
        sendMsg(sendPrivateMsg);
    }

    public void sendGroupMsg(long groupId, Message... messages) {
        SendMsg sendGroupMsg = new SendMsg();
        sendGroupMsg.setGroup_id(groupId);
        sendGroupMsg.setMessage(messages);
        sendMsg(sendGroupMsg);
    }


    @SneakyThrows
    public void sendPrivateMsg(long userId, String text) {
        sendPrivateMsg(userId, new TextMessage(text));
    }

    @SneakyThrows
    public void sendGroupMsg(Long groupId, List<Long> atList, String text) {
        List<Message> messages = new ArrayList<>();
        for (Long at : atList) {
            Message atMsg = new AtMessage(at);
            messages.add(atMsg);
        }
        Message message = new TextMessage(text);
        messages.add(message);
        sendGroupMsg(groupId, messages.toArray(new Message[0]));
    }

    @SneakyThrows
    public void sendGroupMsg(Long groupId, String text) {
        sendGroupMsg(groupId, new TextMessage(text));
    }

    @SneakyThrows
    public CqResult<List<GroupMember>> getGroupMemberList(Long groupId) {
        String jsonTemplate = """
                {
                    "group_id": %d
                }
                """;
        String json = String.format(jsonTemplate, groupId);
        try {
            Map<String, Object> request = ObjectMapperHolder.SNAKE_CASE_MAPPER.readValue(json, new TypeReference<>() {
            });
            return messageClient.getGroupMemberList(request);
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }
}
