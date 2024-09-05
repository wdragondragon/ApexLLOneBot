package com.jdragon.cqhttp.entity.msg;

import com.jdragon.cqhttp.entity.Message;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class TextMessage extends Message {

    public TextMessage(String text) {
        super(MessageTypeEnums.TEXT.getType());
        setText(text);
    }

    public String getText() {
        return (String) getData().get("text");
    }

    public void setText(String text) {
        getData().put("text", text);
    }

}
