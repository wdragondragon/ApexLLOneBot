package com.jdragon.cqhttp.entity;

import lombok.Data;

import java.util.ArrayList;
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

}
