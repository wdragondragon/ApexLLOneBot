package com.jdragon.cqhttp.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.jdragon.cqhttp.client.CqHttpClient;
import com.jdragon.cqhttp.config.ObjectMapperHolder;
import com.jdragon.cqhttp.entity.*;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class MessageService {
    private final CqHttpClient messageClient;

    public MessageService(CqHttpClient messageClient) {
        this.messageClient = messageClient;
    }

    @SneakyThrows
    public void sendPrivateMsg(long userId, String text) {
        String jsonTemplate = """
                {
                    "user_id": %d,
                    "message": [
                        {
                            "type": "text",
                            "data": {
                                "text": "%s"
                            }
                        }
                    ]
                }
                """;
        SendPrivateMsg sendPrivateMsg = new SendPrivateMsg();
        sendPrivateMsg.setUser_id(userId);
        Message message = new Message();
        message.setData(Map.of("text", text));
        message.setType("text");
        sendPrivateMsg.setMessage(List.of(message).toArray(new Message[0]));
        String result = messageClient.sendPrivateMsg(sendPrivateMsg);
        log.info("发送私聊信息结果：{}", result);
    }

    @SneakyThrows
    public void sendGroupMsg(long groupId, String text) {
        String jsonTemplate = """
                {
                    "group_id": %d,
                    "message": [
                        {
                            "type": "text",
                            "data": {
                                "text": "%s"
                            }
                        }
                    ]
                }
                """;
        SendGroupMsg sendGroupMsg = new SendGroupMsg();
        sendGroupMsg.setGroup_id(groupId);
        Message message = new Message();
        message.setData(Map.of("text", text));
        message.setType("text");
        sendGroupMsg.setMessage(List.of(message).toArray(new Message[0]));
        String result = messageClient.sendGroupMsg(sendGroupMsg);
        log.info("发送群聊信息结果：{}", result);
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
