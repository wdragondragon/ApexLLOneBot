package com.jdragon.apex.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("dc_report")
public class DcReport extends Model<DcReport> {

    private String id;

    private String url;

}
