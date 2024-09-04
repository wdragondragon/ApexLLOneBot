package com.jdragon.cqhttp.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.jdragon.cqhttp.client.CqHttpClient;
import com.jdragon.cqhttp.config.ObjectMapperHolder;
import com.jdragon.cqhttp.entity.*;
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

    public void sendGroupPic(Long msgId, Long groupId, String message, byte[] imageBytes) {
        String jsonTemplate = """
                {
                    "group_id": %d,
                    "message": [
                        {
                            "type": "image",
                            "data": {
                                "file": "base64://%s"
                            }
                        }
                    ]
                }
                """;

        SendGroupMsg sendGroupMsg = new SendGroupMsg();
        sendGroupMsg.setGroup_id(groupId);
        List<Message> messages = new ArrayList<>();
        if (msgId != null) {
            Message replyMsg = new Message();
            replyMsg.setData(Map.of("id", msgId));
            replyMsg.setType("reply");
            messages.add(replyMsg);
        }
        if (StringUtils.isNotBlank(message)) {
            Message msg = new Message();
            msg.setData(Map.of("text", message));
            msg.setType("text");
            messages.add(msg);
        }
        // 将图片字节数组转换为Base64字符串
        String base64Image = Base64.getEncoder().encodeToString(imageBytes);
        Message picMessage = new Message();
        picMessage.setData(Map.of("file", "base64://" + base64Image));
        picMessage.setType("image");
        messages.add(picMessage);
        sendGroupMsg.setMessage(messages.toArray(new Message[0]));
        String result = messageClient.sendGroupMsg(sendGroupMsg);
        log.info("发送群聊信息结果：{}", result);
    }

    @SneakyThrows
    public void sendGroupMsg(Long msgId, Long groupId, List<Long> atList, String text) {
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
        List<Message> messages = new ArrayList<>();
        if (msgId != null) {
            Message replyMsg = new Message();
            replyMsg.setData(Map.of("id", msgId));
            replyMsg.setType("reply");
            messages.add(replyMsg);
        }
        for (Long at : atList) {
            Message atMsg = new Message();
            atMsg.setData(Map.of("qq", at));
            atMsg.setType("at");
            messages.add(atMsg);
        }

        Message message = new Message();
        message.setData(Map.of("text", text));
        message.setType("text");
        messages.add(message);
        sendGroupMsg.setMessage(messages.toArray(new Message[0]));
        String result = messageClient.sendGroupMsg(sendGroupMsg);
        log.info("发送群聊信息结果：{}", result);
    }

    @SneakyThrows
    public void sendGroupMsg(Long msgId, Long groupId, String text) {
        sendGroupMsg(msgId, groupId, new ArrayList<>(), text);
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
