package com.jdragon.cqhttp.entity.msg;

import com.jdragon.cqhttp.entity.Message;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ImageMessage extends Message {

    public ImageMessage(String file) {
        super(MessageTypeEnums.IMAGE.getType());
        setFile(file);
    }

    public String getFile() {
        return (String) getData().get("file");
    }

    public void setFile(String file) {
        getData().put("file", file);
    }
}
