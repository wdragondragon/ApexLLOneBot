package com.jdragon.cqhttp.handler;

import com.jdragon.cqhttp.constants.MessageType;
import com.jdragon.cqhttp.message.MetaEvent;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class MetaEventHandler extends BaseHandler<MetaEvent> {
    public MetaEventHandler() {
        super(MessageType.META_EVENT);
    }

    @Override
    public MetaEvent createMsgObject(String msg) throws IOException {
        return MetaEvent.fromJson(msg);
    }

    @Override
    public void handleMsg(MetaEvent obj) {
        log.info("接受到元消息：{}", obj);
    }
}
