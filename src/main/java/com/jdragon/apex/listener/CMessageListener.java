package com.jdragon.apex.listener;

import com.jdragon.cqhttp.CqListener;
import com.jdragon.cqhttp.constants.MessageType;
import com.jdragon.cqhttp.message.ChatMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CMessageListener {

    @CqListener(type = MessageType.CHAT_MESSAGE)
    public void onChatMessage(final ChatMessage message) {
        log.info("自定义监听：{}", message);
    }

}
