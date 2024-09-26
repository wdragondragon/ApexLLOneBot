package com.jdragon.apex.controller;


import com.jdragon.apex.entity.vo.PlayerInfo;
import com.jdragon.apex.handle.ApexLegendsLeaderboards;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "apex_status")
@RestController
@RequestMapping("/apex-status")
public class ApexStatusController {

    private final ApexLegendsLeaderboards apexLegendsLeaderboards;

    public ApexStatusController(ApexLegendsLeaderboards apexLegendsLeaderboards) {
        this.apexLegendsLeaderboards = apexLegendsLeaderboards;
    }

    @Operation(summary = "获取低等级大师以上在线的玩家")
    @GetMapping("/lowLevelMaster")
    public List<PlayerInfo> lowLevelMaster() {
        List<PlayerInfo> playerInfos = apexLegendsLeaderboards.getLegendsLeaderboards();
        playerInfos = playerInfos.stream()
                .filter(playerInfo -> Integer.parseInt(playerInfo.level()) < 500)
                .filter(playerInfo -> !playerInfo.onlineStatus().equals("offline"))
                .toList();
        return playerInfos;
    }

    @Operation(summary = "获取低等级大师以上在线的玩家")
    @GetMapping("/lowLevelMasterStr")
    public String lowLevelMasterStr() {
        List<PlayerInfo> playerInfos = apexLegendsLeaderboards.getLegendsLeaderboards();

        return playerInfos.stream()
                .filter(playerInfo -> Integer.parseInt(playerInfo.level()) < 500)
                .filter(playerInfo -> !playerInfo.onlineStatus().equals("offline"))
                .map(PlayerInfo::toString)
                .collect(Collectors.joining("</br>"));
    }

}
