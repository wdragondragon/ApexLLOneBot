package com.jdragon.cqhttp.entity.msg;

import com.jdragon.cqhttp.entity.Message;
import lombok.NoArgsConstructor;

import java.util.Objects;

@NoArgsConstructor
public class TextMessage extends Message {

    public TextMessage(String text) {
        super(MessageTypeEnums.TEXT.getType());
        setText(text);
    }

    public String getText() {
        return (String) getData().getOrDefault("text", "");
    }

    public void setText(String text) {
        getData().put("text", text);
    }

    public boolean eqStr(String str) {
        return Objects.equals(getText().trim(), str);
    }
}
