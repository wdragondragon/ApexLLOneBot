package com.jdragon.apex.entity.vo.apexapi;

import lombok.Data;

@Data
public class Global {
    private String name;
    private String tag;
    private String uid;
    private String avatar;
    private String platform;
    private int level;
    private int toNextLevelPercent;
    private int internalUpdateCount;
    private int levelPrestige;
    private Bans bans;
    private Rank rank;

    @Data
    public static class Bans {
        private boolean isActive;
        private int remainingSeconds;
        private String last_banReason;
    }

    @Data
    public static class Rank {
        private int rankScore;
        private String rankName;
        private int rankDiv;
        private int ladderPosPlatform;
        private String rankImg;
        private String rankedSeason;
        private double ALStopPercent;
        private int ALStopInt;
        private double ALStopPercentGlobal;
        private int ALStopIntGlobal;
        private boolean ALSFlag;
    }

}