package com.jdragon.apex.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jdragon.apex.entity.AgMachineNew;
import com.jdragon.apex.mapper.AgMachineNewMapper;
import org.springframework.stereotype.Service;

@Service
public class AgMachineNewService extends ServiceImpl<AgMachineNewMapper, AgMachineNew> {

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


    public AgMachineNew findNoBindKeyMachine(String machineCode) {
        LambdaQueryWrapper<AgMachineNew> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AgMachineNew::getMachineCode, machineCode);
        queryWrapper.eq(AgMachineNew::getValKey, null);
        return getOne(queryWrapper);
    }

}
