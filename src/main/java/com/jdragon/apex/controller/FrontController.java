package com.jdragon.apex.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "front")
@RequestMapping("/front")
@Controller
public class FrontController {

    @GetMapping("login")
    public String login() {
        return "login";
    }

    @GetMapping("input")
    public String input() {
        return "input";
    }

    @GetMapping("history")
    public String history() {
        return "history";
    }

}
