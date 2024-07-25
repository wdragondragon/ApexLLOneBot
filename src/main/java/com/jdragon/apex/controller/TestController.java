package com.jdragon.apex.controller;


import com.jdragon.cqhttp.entity.CqResult;
import com.jdragon.cqhttp.entity.GroupMember;
import com.jdragon.cqhttp.service.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "测试")
@RequestMapping("/test")
@RestController
public class TestController {

    private final MessageService messageService;

    public TestController(MessageService messageService) {
        this.messageService = messageService;
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
}
