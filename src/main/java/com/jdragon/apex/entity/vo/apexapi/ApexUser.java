package com.jdragon.apex.entity.vo.apexapi;

import lombok.Data;

@Data
public class ApexUser {
    private Global global;
    private Realtime realtime;
    private Total total;
}