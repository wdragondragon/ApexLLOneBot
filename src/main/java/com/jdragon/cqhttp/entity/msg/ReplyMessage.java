package com.jdragon.cqhttp.entity.msg;

import com.jdragon.cqhttp.entity.Message;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ReplyMessage extends Message {

    public ReplyMessage(long id) {
        super(MessageTypeEnums.REPLY.getType());
        setId(id);
    }

    public long getId() {
        return (long) getData().get("id");
    }

    public void setId(long id) {
        getData().put("id", id);
    }

}
