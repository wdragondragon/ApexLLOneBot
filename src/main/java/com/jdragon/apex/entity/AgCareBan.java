package com.jdragon.apex.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("ag_care_ban")
public class AgCareBan extends Model<AgCareBan> {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private String careType;

    private String careValue;

    private String groupId;

    private String userId;
}
