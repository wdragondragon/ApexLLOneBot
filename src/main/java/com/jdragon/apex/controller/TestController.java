package com.jdragon.apex.controller;


import com.jdragon.apex.client.ApexApiClient;
import com.jdragon.apex.entity.ApexStatusUserInfo;
import com.jdragon.apex.entity.vo.DiscordMessage;
import com.jdragon.apex.entity.vo.RankData;
import com.jdragon.apex.entity.vo.apexapi.ApexUser;
import com.jdragon.apex.handle.ApexRankHistory;
import com.jdragon.apex.handle.ApexUserInfoHandler;
import com.jdragon.apex.service.DiscordService;
import com.jdragon.apex.service.OpenAiService;
import com.jdragon.cqhttp.entity.CqResult;
import com.jdragon.cqhttp.entity.GroupMember;
import com.jdragon.cqhttp.service.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;


@Slf4j
@Tag(name = "测试")
@RequestMapping("/test")
@RestController
public class TestController {

    private final DiscordService discordService;

    private final MessageService messageService;

    private final OpenAiService openAiService;

    private final ApexUserInfoHandler apexUserInfoHandler;

    private final ApexApiClient apexApiClient;

    private final ApexRankHistory apexRankHistory;

    public TestController(MessageService messageService, OpenAiService openAiService, DiscordService discordService, ApexUserInfoHandler apexUserInfoHandler, ApexApiClient apexApiClient, ApexRankHistory apexRankHistory) {
        this.messageService = messageService;
        this.openAiService = openAiService;
        this.discordService = discordService;
        this.apexUserInfoHandler = apexUserInfoHandler;
        this.apexApiClient = apexApiClient;
        this.apexRankHistory = apexRankHistory;
    }

    @Operation(summary = "测试发送信息")
    @GetMapping("/sendPrivateMsg")
    public String sendPrivateMsg(@RequestParam Long userId, @RequestParam String msg) {
        messageService.sendPrivateMsg(userId, msg);
        return "成功";
    }


    @Operation(summary = "测试发送信息")
    @GetMapping("/sendGroupMsg")
    public String sendGroupMsg(@RequestParam Long groupId, @RequestParam String msg) {
        messageService.sendGroupMsg(groupId, msg);
        return "成功";
    }

    @Operation(summary = "获取群成员")
    @GetMapping("/getGroupMemberList")
    public CqResult<List<GroupMember>> getGroupMemberList(@RequestParam Long groupId) {
        return messageService.getGroupMemberList(groupId);
    }

    @Operation(summary = "ai测试")
    @GetMapping("/aiMsg")
    public String getGroupMemberList(@RequestParam String content) {
        return openAiService.aiMsg("206666041", content);
    }

    @Operation(summary = "setDcAuth")
    @GetMapping("/setDcAuth")
    public String discordMsg(@RequestParam String authorization) {
        discordService.setAuthorization(authorization);
        return authorization;
    }

    @Operation(summary = "discordMsg")
    @GetMapping("/discordMsg")
    public List<DiscordMessage> discordMsg(@RequestParam String channelId, @RequestParam Integer limit) {
        return discordService.getDiscordMessages(channelId, limit);
    }

    @Operation(summary = "testUploadImage")
    @GetMapping("/testUploadImage")
    public DiscordMessage discordMsg() {
        return discordService.testUploadImage();
    }

    @Operation(summary = "testAt")
    @GetMapping("/testAt")
    public String testAt() {
        messageService.sendGroupMsg(206666041L, Arrays.asList(1061917196L, 1061917110L), "at测试");
        return "发送成功";
    }


    @Operation(summary = "testProfile")
    @GetMapping("/testProfile")
    public ApexStatusUserInfo testProfile(@RequestParam String param) {
        return apexUserInfoHandler.getUserInfo(param);
    }

    @Operation(summary = "testApexApi")
    @GetMapping("/testApexApi")
    public ApexUser testApexApi(@RequestParam String platform,
                                @RequestParam String player) {
        return apexApiClient.getByName(platform, player);
    }


    @CrossOrigin(value = "*", allowedHeaders = "*")
    @Operation(summary = "apexRankHistory")
    @GetMapping("/apexRankHistory")
    public List<RankData> apexRankHistory(@RequestParam String season,
                                          @RequestParam String uid) {
        return apexRankHistory.getRankHistory(season, uid);
    }
}
