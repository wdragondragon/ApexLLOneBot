package com.jdragon.apex.entity.vo.apexapi;

import lombok.Data;

import java.util.Map;

@Data
public class ApexUser {
    private Global global;
    private Realtime realtime;
    private Map<String, Stat> total;

    @Data
    public static class Stat {
        private String name;
        private String value;

    }
}