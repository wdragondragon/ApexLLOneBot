package com.jdragon.cqhttp.constants;

import lombok.Getter;

@Getter
public enum MessageType {
    CHAT_MESSAGE("message"),
    META_EVENT("meta_event"),
    NOTICE_EVENT("notice");

    private final String code;

    MessageType(String code) {
        this.code = code;
    }

    public static MessageType getByCode(String code) {
        for (MessageType mt : MessageType.values()) {
            if (mt.code.equals(code)) {
                return mt;
            }
        }
        return null;
    }
}
