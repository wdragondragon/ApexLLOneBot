package com.jdragon.apex.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.*;

import java.io.Serializable;


@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("ag_machines_new")
public class AgMachineNew extends Model<AgMachineNew> {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private String valKey;

    private String machineCode;

    @Override
    public Serializable pkVal() {
        return id;
    }
}
