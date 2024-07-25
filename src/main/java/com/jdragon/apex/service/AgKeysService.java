package com.jdragon.apex.service;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jdragon.apex.entity.AgKeys;
import com.jdragon.apex.entity.AgMachineNew;
import com.jdragon.apex.entity.AgMachinesKeys;
import com.jdragon.apex.mapper.AgKeysMapper;
import com.jdragon.utils.ExpirationTimeCalculator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
public class AgKeysService extends ServiceImpl<AgKeysMapper, AgKeys> {

    @Autowired
    private AgMachineNewService agMachineNewService;

    public static final Map<String, Integer> KEY_TYPE_MAP = Map.of(
            "天", 1,
            "周", 2,
            "月", 3,
            "年", 4,
            "永久", 5);


    public String createExperienceCardByQQ(String qq, String validateType) {
        LambdaQueryWrapper<AgKeys> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(AgKeys::getQq, qq);
        lambdaQueryWrapper.eq(AgKeys::getValidateType, validateType);
        lambdaQueryWrapper.eq(AgKeys::getKeyType, 0);

        AgKeys experienceKey = this.getOne(lambdaQueryWrapper);

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

    public List<String> createKeysExt(Integer createNumber, String validateTypeStr, String keyTypeStr) {
        List<String> keyList = new LinkedList<>();
        for (int i = 0; i < createNumber; i++) {
            String uuid = UUID.randomUUID().toString();
            AgKeys agKeys = AgKeys.builder()
                    .valKey(uuid)
                    .validateType(validateTypeStr)
                    .used(0)
                    .keyType(KEY_TYPE_MAP.getOrDefault(keyTypeStr, 1))
                    .build();
            agKeys.insert();
            keyList.add(uuid);
        }
        return keyList;
    }

    public String createKey(String qq, String keyType, String validateType) {
        String uuid = UUID.randomUUID().toString();
        AgKeys agKeys = AgKeys.builder()
                .valKey(uuid)
                .qq(qq)
                .validateType(validateType)
                .used(0)
                .keyType(KEY_TYPE_MAP.getOrDefault(keyType, 1))
                .build();
        agKeys.insert();
        return uuid;
    }

    public String bind(String machineCode, String valKey) {
        LambdaQueryWrapper<AgKeys> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(AgKeys::getValKey, valKey);
        AgKeys agKeys = getOne(lambdaQueryWrapper);
        if (agKeys == null) {
            return "key不可用";
        }

        if (agKeys.getUsed() == 1) {
            return "key已被使用过";
        }

        AgMachinesKeys validate = agMachineNewService.validate(machineCode, valKey);
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
        this.updateById(agKeys);
        return "绑定成功，有效期到" + ExpirationTimeCalculator.formatDateTime(agKeys.getExpirationTime());
    }

    public void updateLastValTime(String valKey) {
        LambdaUpdateWrapper<AgKeys> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(AgKeys::getValKey, valKey);
        lambdaUpdateWrapper.set(AgKeys::getLastValTime, LocalDateTime.now());
        update(lambdaUpdateWrapper);
    }
}
