package com.jdragon.apex.entity.vo.apexapi;

import lombok.Data;

@Data
public class Total {
    private Stat scout_of_action_targets_hit;
    private Stat jackson_bow_out_damage_done;
    private Stat smoke_show_damage_done;
    private Stat specialEvent_kills;
    private Stat specialEvent_damage;
    private Stat specialEvent_wins;
    private Stat damage;
    private Stat kd;

    @Data
    public static class Stat {
        private String name;
        private int value;

    }
}
