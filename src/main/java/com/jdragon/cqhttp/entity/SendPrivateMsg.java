package com.jdragon.cqhttp.entity;

import lombok.Data;

@Data
public class SendPrivateMsg {
    private Long user_id;

    private Message[] message;

}
