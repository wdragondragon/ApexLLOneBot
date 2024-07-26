package com.jdragon.apex.service;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jdragon.apex.entity.AgKeys;
import com.jdragon.apex.entity.AgMachineNew;
import com.jdragon.apex.entity.AgMachinesKeys;
import com.jdragon.apex.mapper.AgKeysMapper;
import com.jdragon.apex.utils.ExpirationTimeCalculator;
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

    public static final Map<String, Integer> KEY_TYPE_MAP = Map.of(
            "天", 1,
            "周", 2,
            "月", 3,
            "年", 4,
            "永久", 5);


    public List<String> createKeysExt(Integer createNumber, String validateTypeStr, String keyTypeStr) {
        List<String> keyList = new LinkedList<>();
        for (int i = 0; i < createNumber; i++) {
            String uuid = UUID.randomUUID().toString();
            AgKeys agKeys = AgKeys.builder()
                    .valKey(uuid)
                    .validateType(validateTypeStr)
                    .used(0)
                    .keyType(KEY_TYPE_MAP.getOrDefault(keyTypeStr, 1))
                    .externalCard(1)
                    .build();
            agKeys.insert();
            keyList.add(uuid);
        }
        return keyList;
    }

    public String createKey(String qq, String keyType, String validateType, String createGroup) {
        String uuid = UUID.randomUUID().toString();
        AgKeys agKeys = AgKeys.builder()
                .valKey(uuid)
                .qq(qq)
                .validateType(validateType)
                .used(0)
                .createGroup(createGroup)
                .keyType(KEY_TYPE_MAP.getOrDefault(keyType, 1))
                .build();
        agKeys.insert();
        return uuid;
    }

    public void updateLastValTime(String valKey) {
        LambdaUpdateWrapper<AgKeys> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(AgKeys::getValKey, valKey);
        lambdaUpdateWrapper.set(AgKeys::getLastValTime, LocalDateTime.now());
        update(lambdaUpdateWrapper);
    }
}
