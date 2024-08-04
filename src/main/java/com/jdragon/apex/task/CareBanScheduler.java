package com.jdragon.apex.task;

import com.jdragon.apex.service.AgBanHistoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CareBanScheduler {

    private final AgBanHistoryService agBanHistoryService;

    public CareBanScheduler(AgBanHistoryService agBanHistoryService) {
        this.agBanHistoryService = agBanHistoryService;
    }

    @Scheduled(fixedRate = 300000) // 5分钟（300000毫秒）执行一次
    public void performTask() {
        log.info("定时任务执行: {}", System.currentTimeMillis());
        agBanHistoryService.refreshBanHistory();
    }
}
