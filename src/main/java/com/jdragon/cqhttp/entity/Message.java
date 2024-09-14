package com.jdragon.cqhttp.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Message {
    private Map<String, Object> data = new HashMap<>();
    private String type;

    public Message(String type) {
        this.type = type;
    }

    public void init(Message message) {
        this.data = message.getData();
        this.type = message.getType();
    }


}
