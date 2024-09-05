package com.jdragon.cqhttp.entity;

import com.jdragon.cqhttp.entity.msg.MessageTypeEnums;
import lombok.Data;
import lombok.SneakyThrows;

import java.util.LinkedList;
import java.util.List;

@Data
public class MessageChain {

    private List<Message> messages = new LinkedList<>();

    public void addMessage(Message message) {
        messages.add(message);
    }

    public Message getMessage(Integer index) {
        if (index >= messages.size()) {
            return null;
        }
        return messages.get(index);
    }

    @SneakyThrows
    public MessageChain(Message[] messages) {
        for (Message message : messages) {
            MessageTypeEnums anEnum = MessageTypeEnums.getEnum(message.getType());
            if (anEnum != null) {
                Class<? extends Message> clazz = anEnum.getClazz();
                Message messageNew = clazz.getConstructor().newInstance();
                messageNew.init(message);
                message = messageNew;
            }
            addMessage(message);
        }
    }

}
