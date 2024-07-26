
package com.jdragon.cqhttp.entity;

import lombok.Data;

@Data
public class SendGroupMsg {
    private Long group_id;

    private Message[] message;

}
