package com.jdragon.cqhttp.entity;

import lombok.Data;

@Data
public class SendMsg {

    private Long user_id;

    private Long group_id;

    private String type;

    private Message[] message;

}
