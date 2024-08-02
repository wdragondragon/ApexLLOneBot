package com.jdragon.apex.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("ag_ban_history")
public class AgBanHistory extends Model<AgBanHistory> {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private String uid;

    private String descMsg;

    private String msg;

    private String link;

    private LocalDateTime banDate;

    @Override
    public String toString() {
        String banDateStr = banDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        return "Ban in " + banDateStr + "\n" + uid + "\n" + descMsg + "\n\n" + msg;
    }
}
