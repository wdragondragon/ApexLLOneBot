package com.jdragon.apex.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AgMachinesKeys {
    private Integer id;

    private String valKey;

    private String machineCode;

    private String qq;

    private LocalDateTime expirationTime;

    private String validateType;

    private Integer used;

    private Integer keyType;

    private LocalDateTime lastValTime;

    private Integer externalCard;

    private String createGroup;
}
