package com.jdragon.cqhttp.entity.msg;

import com.jdragon.cqhttp.entity.Message;
import lombok.Getter;

@Getter
public enum MessageTypeEnums {

    AT("at", AtMessage.class),
    IMAGE("image", ImageMessage.class),
    REPLY("reply", ReplyMessage.class),
    TEXT("text", TextMessage.class),
    ;

    private final String type;

    private final Class<? extends Message> clazz;


    MessageTypeEnums(String type, Class<? extends Message> clazz) {
        this.type = type;
        this.clazz = clazz;
    }

    public static MessageTypeEnums getEnum(String type) {
        for (MessageTypeEnums e : MessageTypeEnums.values()) {
            if (e.type.equals(type)) {
                return e;
            }
        }
        return null;
    }
}
