package com.jdragon.apex.service;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jdragon.apex.entity.ApexStatusUserInfo;
import com.jdragon.apex.mapper.ApexStatusUserInfoMapper;
import org.springframework.stereotype.Service;

@Service
public class ApexStatusUserInfoService extends ServiceImpl<ApexStatusUserInfoMapper, ApexStatusUserInfo> {

    public ApexStatusUserInfo getByUid(String uid) {
        ApexStatusUserInfo apexStatusUserInfo = new ApexStatusUserInfo();
        apexStatusUserInfo.setUid(uid);

        LambdaQueryWrapper<ApexStatusUserInfo> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.setEntity(apexStatusUserInfo);
        return getOne(lambdaQueryWrapper);
    }

}
