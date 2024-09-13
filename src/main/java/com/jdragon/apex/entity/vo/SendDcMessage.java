package com.jdragon.apex.entity.vo;

import lombok.Data;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

@Data
public class SendDcMessage {

    private String channel_id;

    private String content;

    private String nonce = String.valueOf(new BigInteger(62, new SecureRandom()).toString());

    private List<String> sticker_ids = new ArrayList<>();

    private int type = 0;


    @Data
    public static class Attachments {

        private String filename;

        private String id;

        private String uploaded_filename;
    }
}
