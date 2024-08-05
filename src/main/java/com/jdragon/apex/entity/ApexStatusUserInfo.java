package com.jdragon.apex.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("apex_user_info")
public class ApexStatusUserInfo extends Model<ApexStatusUserInfo> {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private String platform;

    private String name;

    private String uid;

    private String pid;

    private Integer level;

    private Integer rp;

    private Boolean online;

    @Override
    public String toString() {
        return String.format("平台：%s\nID：%s\n等级：%s\n排位分：%s\n当前%s在线", getPlatform(), getName(), getLevel(), getRp(), getOnline() ? "" : "不");
    }
}
