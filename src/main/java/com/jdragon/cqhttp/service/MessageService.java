package com.jdragon.cqhttp.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.jdragon.cqhttp.client.CqHttpClient;
import com.jdragon.cqhttp.config.ObjectMapperHolder;
import com.jdragon.cqhttp.entity.CqResult;
import com.jdragon.cqhttp.entity.GroupMember;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

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
        String json = String.format(jsonTemplate, userId, text);
        Map<String, Object> request = ObjectMapperHolder.MAPPER.readValue(json, new TypeReference<>() {
        });
        messageClient.sendPrivateMsg(request);
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
        String json = String.format(jsonTemplate, groupId, text);
        Map<String, Object> request = ObjectMapperHolder.MAPPER.readValue(json, new TypeReference<>() {
        });
        messageClient.sendPrivateMsg(request);
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
