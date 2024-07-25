package com.jdragon.cqhttp.entity;


import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupMember {

    private Long group_id;
    private Long user_id;
    private String nickname;
    private String card;
    private String sex;
    private Integer age;
    private String area;
    private Integer level;
    private Integer qq_level;
    private Long join_time;
    private Long last_sent_time;
    private Long title_expire_time;
    private Boolean unfriendly;
    private Boolean card_changeable;
    private Boolean is_robot;
    private Long shut_up_timestamp;
    private String role;
    private String title;
}
