package com.jdragon.apex.entity.vo;

import lombok.Getter;

//@Getter
public record PlayerInfo(String name, String onlineStatus, String uid, String level, String score) {
    public PlayerInfo(String name, String onlineStatus, String uid, String level, String score) {
        this.name = name;
        this.onlineStatus = onlineStatus.replaceAll("avatar_", "");
        this.uid = uid;
        this.level = level;
        this.score = score;
    }

    @Override
    public String toString() {
        return String.format("[%13s][%4s][%7s][%s]%s", uid, level, score, onlineStatus, name);
    }
}
