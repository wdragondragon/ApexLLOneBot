package com.jdragon.apex.controller;

import com.jdragon.apex.entity.AgBanHistory;
import com.jdragon.apex.mapper.AgBanHistoryMapper;
import com.jdragon.apex.service.AgBanHistoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

@Tag(name = "ban")
@RequestMapping("/ban")
@Controller
public class BanController {

    private final AgBanHistoryMapper banHistoryMapper;

    public BanController(AgBanHistoryMapper banHistoryMapper) {
        this.banHistoryMapper = banHistoryMapper;
    }

    @Operation(summary = "ban页面")
    @GetMapping("/today")
    public String getBanInfo(Model model) {
        List<AgBanHistory> agBanHistories = banHistoryMapper.todayBan();
        // 将数据添加到模型中
        model.addAttribute("items", agBanHistories);
        return "banInfo";
    }
}
