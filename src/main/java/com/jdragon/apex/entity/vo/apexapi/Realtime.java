package com.jdragon.apex.entity.vo.apexapi;

import lombok.Data;

@Data
public class Realtime {
    private String lobbyState;
    private int isOnline;
    private int isInGame;
    private int canJoin;
    private int partyFull;
    private String selectedLegend;
    private String currentState;
    private long currentStateSinceTimestamp;
    private String currentStateAsText;

    // Getters and Setters
}