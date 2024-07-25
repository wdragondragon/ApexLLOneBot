package com.jdragon.cqhttp.handler;

import com.jdragon.cqhttp.constants.MessageType;
import com.jdragon.cqhttp.message.ChatMessage;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class ChatMessageHandler extends BaseHandler<ChatMessage> {
    public ChatMessageHandler() {
        super(MessageType.CHAT_MESSAGE);
    }

    @Override
    public ChatMessage createMsgObject(String msg) throws IOException {
        return ChatMessage.fromJson(msg);
    }

    @Override
    public void handleMsg(ChatMessage obj) {
        log.info("接受到聊天消息：{}", obj);
    }
}
