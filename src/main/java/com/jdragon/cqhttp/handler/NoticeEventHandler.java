package com.jdragon.cqhttp.handler;

import com.jdragon.cqhttp.constants.MessageType;
import com.jdragon.cqhttp.message.NoticeEvent;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class NoticeEventHandler extends BaseHandler<NoticeEvent> {
    public NoticeEventHandler() {
        super(MessageType.NOTICE_EVENT);
    }

    @Override
    public NoticeEvent createMsgObject(String msg) throws IOException {
        return NoticeEvent.fromJson(msg);
    }

    @Override
    public void handleMsg(NoticeEvent obj) {
        log.info("接受到通知消息：{}", obj);
    }
}
