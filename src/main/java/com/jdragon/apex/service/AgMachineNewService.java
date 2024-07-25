package com.jdragon.apex.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jdragon.apex.entity.AgMachineNew;
import com.jdragon.apex.entity.AgMachinesKeys;
import com.jdragon.apex.mapper.AgMachineNewMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AgMachineNewService extends ServiceImpl<AgMachineNewMapper, AgMachineNew> {

    @Autowired
    private AgKeysService agKeysService;

    public void resetKeyValue(String machineCode) {
        LambdaUpdateWrapper<AgMachineNew> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(AgMachineNew::getMachineCode, machineCode);
        updateWrapper.set(AgMachineNew::getValKey, null);
        this.update(updateWrapper);
    }

    public AgMachineNew getByValKey(String valKey) {
        return this.getOne(new LambdaQueryWrapper<AgMachineNew>()
                .eq(AgMachineNew::getValKey, valKey));
    }


    public AgMachinesKeys validate(String machineCode, String validateType) {
        List<AgMachinesKeys> authList =
                baseMapper.getAuthList("machine", machineCode);
        if (authList.size() == 1) {
            AgMachinesKeys agMachinesKeys = authList.getFirst();
            if (agMachinesKeys.getLastValTime() == null ||
                    LocalDateTime.now().isAfter(agMachinesKeys.getLastValTime().plusSeconds(45))) {
                agMachinesKeys.setLastValTime(LocalDateTime.now());
                agKeysService.updateLastValTime(agMachinesKeys.getValKey());
                return agMachinesKeys;
            }
        }
        return null;
    }

    public AgMachineNew findNoBindKeyMachine(String machineCode) {
        LambdaQueryWrapper<AgMachineNew> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AgMachineNew::getMachineCode, machineCode);
        queryWrapper.eq(AgMachineNew::getValKey, null);
        return getOne(queryWrapper);
    }

}
