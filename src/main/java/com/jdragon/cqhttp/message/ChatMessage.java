package com.jdragon.cqhttp.message;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.jdragon.cqhttp.entity.SendMsg;
import com.jdragon.cqhttp.entity.msg.ReplyMessage;
import com.jdragon.cqhttp.entity.msg.TextMessage;
import com.jdragon.cqhttp.holder.SpringContextHolder;
import com.jdragon.cqhttp.config.ObjectMapperHolder;
import com.jdragon.cqhttp.entity.Message;
import com.jdragon.cqhttp.entity.MessageChain;
import com.jdragon.cqhttp.entity.Sender;
import com.jdragon.cqhttp.service.MessageService;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

    private MessageChain messageChain;

    public static ChatMessage fromJson(String json) throws IOException {
        ChatMessage chatMessage = ObjectMapperHolder.SNAKE_CASE_MAPPER.readValue(json, ChatMessage.class);
        chatMessage.validateAttributes(ObjectMapperHolder.SNAKE_CASE_MAPPER.convertValue(chatMessage, new TypeReference<>() {
        }));
        chatMessage.setMessageChain(new MessageChain(chatMessage.getMessage()));
        return chatMessage;
    }

    public void replyPrivate(String msg) {
        replyPrivate(new TextMessage(msg));
    }

    public void replyPrivate(Message... messages) {
        MessageService messageService = SpringContextHolder.getBean(MessageService.class);
        SendMsg sendMsg = new SendMsg();
        sendMsg.setUser_id(userId);
        sendMsg.setMessage(messages);
        messageService.sendMsg(sendMsg);
    }

    public void reply(String msg) {
        reply(new TextMessage(msg));
    }

    public void reply(Message... messages) {
        MessageService messageService = SpringContextHolder.getBean(MessageService.class);
        SendMsg sendMsg = new SendMsg();
        sendMsg.setUser_id(userId);
        sendMsg.setGroup_id(groupId);
        sendMsg.setType(getType());

        ReplyMessage replyMessage = new ReplyMessage(messageId);
        List<Message> messagesList = new ArrayList<>();
        messagesList.add(replyMessage);
        messagesList.addAll(List.of(messages));

        sendMsg.setMessage(messagesList.toArray(new Message[0]));
        messageService.sendMsg(sendMsg);
    }

    @Override
    public String getType() {
        return messageType;
    }
}
