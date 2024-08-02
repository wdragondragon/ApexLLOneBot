package com.jdragon.apex.controller;


import com.jdragon.apex.client.DiscordClient;
import com.jdragon.apex.entity.vo.DiscordMessage;
import com.jdragon.apex.service.DiscordService;
import com.jdragon.apex.service.OpenAiService;
import com.jdragon.cqhttp.entity.CqResult;
import com.jdragon.cqhttp.entity.GroupMember;
import com.jdragon.cqhttp.service.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    public TestController(MessageService messageService, OpenAiService openAiService, DiscordClient discordClient, DiscordService discordService) {
        this.messageService = messageService;
        this.openAiService = openAiService;
        this.discordService = discordService;
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
        messageService.sendGroupMsg(null, groupId, msg);
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

    @Operation(summary = "testAt")
    @GetMapping("/testAt")
    public String testAt() {
        messageService.sendGroupMsg(null, 206666041L, Arrays.asList(1061917196L, 1061917110L), "at测试");
        return "发送成功";
    }
}
