package com.jdragon.apex.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("ag_keys")
public class AgKeys extends Model<AgKeys> {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private String valKey;

    private String qq;

    private LocalDateTime expirationTime;

    private String validateType;

    private Integer used;

    private Integer keyType;

    private LocalDateTime lastValTime;

    private Integer externalCard;

    private String createGroup;

    @Override
    public Serializable pkVal() {
        return id;
    }
}
