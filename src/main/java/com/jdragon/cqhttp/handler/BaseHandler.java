package com.jdragon.cqhttp.handler;

import com.jdragon.cqhttp.constants.MessageType;
import com.jdragon.cqhttp.message.BaseMessage;
import lombok.Getter;

import java.io.IOException;


@Getter
public abstract class BaseHandler<T extends BaseMessage> {

    public MessageType type;

    public BaseHandler(MessageType type) {
        this.type = type;
    }

    abstract T createMsgObject(String msg) throws IOException;

    abstract void handleMsg(T obj) throws IOException;

    public void handleMsg(String msg) throws IOException {
        T msgObject = createMsgObject(msg);
        handleMsg(msgObject);
    }
}
