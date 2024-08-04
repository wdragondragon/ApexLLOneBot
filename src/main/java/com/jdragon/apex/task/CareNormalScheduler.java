package com.jdragon.apex.task;

import com.jdragon.apex.service.AgCareBanService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CareNormalScheduler {


    private final AgCareBanService agCareBanService;

    public CareNormalScheduler(AgCareBanService agCareBanService) {
        this.agCareBanService = agCareBanService;
    }

    @Scheduled(fixedRate = 300000) // 5分钟（300000毫秒）执行一次
    public void performTask() {
        agCareBanService.sendCareNormalMessage();
    }
}
