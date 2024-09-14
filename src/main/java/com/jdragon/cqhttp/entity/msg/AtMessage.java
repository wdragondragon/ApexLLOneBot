package com.jdragon.cqhttp.entity.msg;

import com.jdragon.cqhttp.entity.Message;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class AtMessage extends Message {

    public AtMessage(long qq) {
        super(MessageTypeEnums.AT.getType());
        setQq(qq);
    }

    public long getQq() {
        return Long.parseLong((String) getData().get("qq"));
    }

    public void setQq(long qq) {
        getData().put("qq", qq);
    }
}
