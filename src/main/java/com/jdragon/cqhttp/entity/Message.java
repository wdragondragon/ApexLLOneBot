package com.jdragon.cqhttp.entity;

import lombok.Data;

import java.util.Map;

@Data
public class Message {
    private Map<String, Object> data;
    private String type;
}
