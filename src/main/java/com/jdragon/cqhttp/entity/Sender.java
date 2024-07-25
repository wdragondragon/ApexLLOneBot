package com.jdragon.cqhttp.entity;

import lombok.Data;

@Data
public class Sender {
    private long userId;
    private String nickname;
    private String card;
    private String role;
}
