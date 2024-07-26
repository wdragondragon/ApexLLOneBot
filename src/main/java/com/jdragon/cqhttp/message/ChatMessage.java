package com.jdragon.cqhttp.message;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.jdragon.cqhttp.config.ObjectMapperHolder;
import com.jdragon.cqhttp.entity.Message;
import com.jdragon.cqhttp.entity.Sender;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.IOException;
import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class ChatMessage extends BaseMessage {
    private long selfId;
    private long userId;
    private long time;
    private long messageId;
    private long realId;
    private long messageSeq;
    private String messageType;
    private Sender sender;
    private String rawMessage;
    private int font;
    private String subType;
    private Message[] message;
    private String messageFormat;
    private String postType;
    private long groupId;

    public static ChatMessage fromJson(String json) throws IOException {
        ChatMessage chatMessage = ObjectMapperHolder.MAPPER.readValue(json, ChatMessage.class);
        chatMessage.validateAttributes(ObjectMapperHolder.MAPPER.convertValue(chatMessage, new TypeReference<Map<String, Object>>() {
        }));
        return chatMessage;
    }

    @Override
    public String getType() {
        return messageType;
    }
}
