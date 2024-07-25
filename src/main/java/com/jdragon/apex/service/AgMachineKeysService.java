package com.jdragon.apex.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jdragon.apex.entity.AgKeys;
import com.jdragon.apex.entity.AgMachineNew;
import com.jdragon.apex.entity.AgMachinesKeys;
import com.jdragon.apex.mapper.AgMachineNewMapper;
import com.jdragon.apex.utils.ExpirationTimeCalculator;
import com.jdragon.cqhttp.entity.CqResult;
import com.jdragon.cqhttp.entity.GroupMember;
import com.jdragon.cqhttp.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AgMachineKeysService {

    private final AgKeysService agKeysService;

    private final AgMachineNewMapper agMachineNewMapper;

    private final AgMachineNewService agMachineNewService;

    private final MessageService messageService;

    public AgMachineKeysService(AgKeysService agKeysService, AgMachineNewMapper agMachineNewMapper, AgMachineNewService agMachineNewService, MessageService messageService) {
        this.agKeysService = agKeysService;
        this.agMachineNewMapper = agMachineNewMapper;
        this.agMachineNewService = agMachineNewService;
        this.messageService = messageService;
    }

    public String createExperienceCardByQQ(String qq, String validateType) {
        LambdaQueryWrapper<AgKeys> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(AgKeys::getQq, qq);
        lambdaQueryWrapper.eq(AgKeys::getValidateType, validateType);
        lambdaQueryWrapper.eq(AgKeys::getKeyType, 0);

        AgKeys experienceKey = agKeysService.getOne(lambdaQueryWrapper);

        if (experienceKey != null) {
            if (experienceKey.getUsed() == 1) {
                experienceKey.setUsed(0);
                AgMachineNew agMachineNew = agMachineNewService.getByValKey(experienceKey.getValKey());
                if (agMachineNew != null) {
                    log.info("从{}中解绑{}", agMachineNew.getMachineCode(), experienceKey.getValKey());
                    agMachineNew.setValKey(null);
                    agMachineNewService.resetKeyValue(agMachineNew.getMachineCode());
                }
                experienceKey.updateById();
            }
            return experienceKey.getValKey();
        } else {
            AgKeys agKeys = AgKeys.builder()
                    .valKey(UUID.randomUUID().toString())
                    .qq(qq)
                    .expirationTime(null)
                    .validateType(validateType)
                    .used(0)
                    .keyType(0)
                    .build();
            agKeys.insert();
            return agKeys.getValKey();
        }
    }

    public String bind(String machineCode, String valKey) {
        LambdaQueryWrapper<AgKeys> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(AgKeys::getValKey, valKey);
        AgKeys agKeys = agKeysService.getOne(lambdaQueryWrapper);
        if (agKeys == null) {
            return "key不可用";
        }

        if (agKeys.getUsed() == 1) {
            return "key已被使用过";
        }

        AgMachinesKeys validate = validate(machineCode, valKey);
        if (validate != null) {
            if (validate.getKeyType() != 0) {
                return "当前绑定的" + agKeys.getValidateType() + "类型的key并为过期，暂不可再次绑定";
            }
        }

        AgMachineNew agMachineNew = agMachineNewService.findNoBindKeyMachine(machineCode);
        if (agMachineNew == null) {
            agMachineNewService.save(AgMachineNew.builder()
                    .valKey(valKey)
                    .machineCode(machineCode)
                    .build());
        } else {
            agMachineNew.setValKey(valKey);
            agMachineNewService.updateById(agMachineNew);
        }
        agKeys.setUsed(1);
        agKeys.setExpirationTime(ExpirationTimeCalculator.calculateNewExpirationTime(agKeys.getKeyType(), LocalDateTime.now()));
        agKeysService.updateById(agKeys);
        return "绑定成功，有效期到" + ExpirationTimeCalculator.formatDateTime(agKeys.getExpirationTime());
    }

    public AgMachinesKeys validate(String machineCode, String validateType) {
        List<AgMachinesKeys> authList =
                agMachineNewMapper.getAuthList("machine", machineCode, validateType);
        if (authList.size() == 1) {
            AgMachinesKeys agMachinesKeys = authList.getFirst();
            CqResult<List<GroupMember>> groupMemberList = messageService.getGroupMemberList(206666041L);
            List<String> userIdList = groupMemberList.getData().stream()
                    .map(GroupMember::getUser_id)
                    .map(String::valueOf).toList();
            if (agMachinesKeys.getExternalCard() == 1 || userIdList.contains(agMachinesKeys.getQq())) {
                if (agMachinesKeys.getLastValTime() == null ||
                        LocalDateTime.now().isAfter(agMachinesKeys.getLastValTime().plusSeconds(45))) {
                    agMachinesKeys.setLastValTime(LocalDateTime.now());
                    agKeysService.updateLastValTime(agMachinesKeys.getValKey());
                    return agMachinesKeys;
                }
            }
        }
        return null;
    }
}
