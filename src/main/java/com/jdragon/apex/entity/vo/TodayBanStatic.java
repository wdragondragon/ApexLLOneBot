package com.jdragon.apex.entity.vo;

import lombok.Data;

@Data
public class TodayBanStatic {

    private String rankRole;

    private String platform;

    private String count;

    private String minRankRange;

    @Override
    public String toString() {
        return String.format("%s端%s封禁人数%s,最高排名%s", platform, rankRole, count, minRankRange);
    }
}
