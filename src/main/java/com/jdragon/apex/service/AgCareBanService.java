package com.jdragon.apex.service;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jdragon.apex.entity.AgBanHistory;
import com.jdragon.apex.entity.AgCareBan;
import com.jdragon.apex.mapper.AgCareBanMapper;
import com.jdragon.cqhttp.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AgCareBanService extends ServiceImpl<AgCareBanMapper, AgCareBan> {


    private final MessageService messageService;

    public AgCareBanService(MessageService messageService) {
        this.messageService = messageService;
    }

    public AgCareBan getOne(AgCareBan agCareBan) {
        LambdaQueryWrapper<AgCareBan> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.setEntity(agCareBan);
        return getOne(lambdaQueryWrapper);
    }

    public void sendCareMessage(AgBanHistory agBanHistory) {
        String uid = agBanHistory.getUid();
        List<AgCareBan> agCareBanList = baseMapper.queryCareBan("uid", uid);
        Map<String, List<AgCareBan>> agCareBanGroup = agCareBanList.stream().collect(Collectors.groupingBy(AgCareBan::getGroupId));
        String msg = String.format("你关注的uid为[%s]已被封禁", uid);
        for (Map.Entry<String, List<AgCareBan>> entry : agCareBanGroup.entrySet()) {
            String groupId = entry.getKey();
            List<AgCareBan> careBanList = entry.getValue();
            List<Long> atList = careBanList.stream().map(AgCareBan::getUserId).map(Long::valueOf).toList();
            messageService.sendGroupMsg(null, Long.valueOf(groupId), atList, msg);
        }
    }
}
