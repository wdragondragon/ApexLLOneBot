package com.jdragon.apex.service;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jdragon.apex.entity.AgBanHistory;
import com.jdragon.apex.entity.AgCareBan;
import com.jdragon.apex.entity.ApexStatusUserInfo;
import com.jdragon.apex.handle.ApexStatusHandler;
import com.jdragon.apex.mapper.AgCareBanMapper;
import com.jdragon.cqhttp.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AgCareBanService extends ServiceImpl<AgCareBanMapper, AgCareBan> {


    private final MessageService messageService;

    private final ApexStatusHandler apexStatusHandler;

    private final ApexStatusUserInfoService apexStatusUserInfoService;

    public AgCareBanService(MessageService messageService, ApexStatusHandler apexStatusHandler, ApexStatusUserInfoService apexStatusUserInfoService) {
        this.messageService = messageService;
        this.apexStatusHandler = apexStatusHandler;
        this.apexStatusUserInfoService = apexStatusUserInfoService;
    }

    public AgCareBan getOne(AgCareBan agCareBan) {
        LambdaQueryWrapper<AgCareBan> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.setEntity(agCareBan);
        return getOne(lambdaQueryWrapper);
    }

    public void sendCareBanMessage(AgBanHistory agBanHistory) {
        String uid = agBanHistory.getUid();
        List<AgCareBan> agCareBanList = baseMapper.queryCareBan(uid);
        Map<String, List<AgCareBan>> agCareBanGroup = agCareBanList.stream().collect(Collectors.groupingBy(AgCareBan::getGroupId));
        String msg = String.format("你关注的uid为[%s]已被封禁", uid);
        for (Map.Entry<String, List<AgCareBan>> entry : agCareBanGroup.entrySet()) {
            String groupId = entry.getKey();
            List<AgCareBan> careBanList = entry.getValue();
            List<Long> atList = careBanList.stream().map(AgCareBan::getUserId).map(Long::valueOf).toList();
            messageService.sendGroupMsg(null, Long.valueOf(groupId), atList, msg);
        }
    }

    public void sendCareNormalMessage() {
        AgCareBan agCareBan = new AgCareBan();
        agCareBan.setCareType("活跃");
        List<AgCareBan> agCareBanList = list(new LambdaQueryWrapper<AgCareBan>().setEntity(agCareBan));
        for (AgCareBan careBan : agCareBanList) {
            String uid = careBan.getCareValue();
            ApexStatusUserInfo userInfo = apexStatusHandler.getUserInfo(uid);
            ApexStatusUserInfo oldUserInfo = apexStatusUserInfoService.getByUid(uid);
            if (oldUserInfo != null) {
                String msg = getDiff(userInfo, oldUserInfo);
                userInfo.setId(oldUserInfo.getId());
                apexStatusUserInfoService.updateById(userInfo);
                if (StringUtils.isNotBlank(msg)) {
                    messageService.sendGroupMsg(null, Long.valueOf(careBan.getGroupId()), List.of(Long.valueOf(careBan.getUserId())), msg);
                }
            } else {
                apexStatusUserInfoService.save(userInfo);
            }
        }
    }

    @NotNull
    private static String getDiff(ApexStatusUserInfo userInfo, ApexStatusUserInfo oldUserInfo) {
        String msg = "";
        if (!userInfo.isOnline() == oldUserInfo.isOnline()) {
            msg += String.format("你关注的[%s]现在%s", userInfo.getName(), userInfo.isOnline() ? "上线了" : "下线了") + "\n";
        }
        if (!Objects.equals(userInfo.getLevel(), oldUserInfo.getLevel())) {
            msg += String.format("你关注的[%s]发生等级变化，从[%s]到[%s]", userInfo.getName(), oldUserInfo.getLevel(), userInfo.getLevel()) + "\n";
        }
        if (!Objects.equals(userInfo.getRp(), oldUserInfo.getRp())) {
            msg += String.format("你关注的[%s]发生排位分变化，从[%s]到[%s]", userInfo.getName(), oldUserInfo.getRp(), userInfo.getRp()) + "\n";
        }
        return msg;
    }

    public boolean careUid(Long groupId, Long userId, String uid, String careType) {
        AgCareBan agCareBan = new AgCareBan();
        agCareBan.setCareType(careType);
        agCareBan.setCareValue(uid);
        agCareBan.setGroupId(String.valueOf(groupId));
        agCareBan.setUserId(String.valueOf(userId));

        AgCareBan one = this.getOne(agCareBan);
        if (one == null) {
            this.save(agCareBan);
            return true;
        }
        return false;
    }
}
