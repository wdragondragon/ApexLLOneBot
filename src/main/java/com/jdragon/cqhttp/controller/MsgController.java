package com.jdragon.cqhttp.controller;


import com.jdragon.cqhttp.handler.MessageHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Slf4j
@RequestMapping
@RestController
public class MsgController {

    private final MessageHandler messageHandler;

    public MsgController(MessageHandler messageHandler) {
        this.messageHandler = messageHandler;
    }

    @PostMapping
    public String route(@RequestBody String msg) throws IOException {
        messageHandler.handle(msg);
        return "success";
    }
}
