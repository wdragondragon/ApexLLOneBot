package com.jdragon.cqhttp.entity.msg;

import com.jdragon.cqhttp.entity.Message;
import lombok.NoArgsConstructor;

import java.util.Base64;

@NoArgsConstructor
public class ImageMessage extends Message {

    public ImageMessage(byte[] imageBytes) {
        super(MessageTypeEnums.IMAGE.getType());
        String base64Image = Base64.getEncoder().encodeToString(imageBytes);
        setFile("base64://" + base64Image);
    }

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
