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

    private Double alStopPercent;

    private Integer alStopInt;

    private Double alStopPercentGlobal;

    private Integer alStopIntGlobal;

    private Boolean online;

    @Override
    public String toString() {
        return String.format("平台：%s\nUID：%s\n名字：%s\n等级：%s\n排位分：%s\nPC排名：[%s]|[%s%%]\n全平台排名：[%s]|[%s%%]\n当前%s在线", getPlatform(), getUid(), getName(), getLevel(), getRp()
                , getAlStopInt(), getAlStopPercent()
                , getAlStopIntGlobal(), getAlStopPercentGlobal()
                , getOnline() ? "" : "不");
    }
}
