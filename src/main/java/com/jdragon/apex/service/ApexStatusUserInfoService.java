package com.jdragon.apex.service;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jdragon.apex.entity.ApexStatusUserInfo;
import com.jdragon.apex.mapper.ApexStatusUserInfoMapper;
import org.springframework.stereotype.Service;

@Service
public class ApexStatusUserInfoService extends ServiceImpl<ApexStatusUserInfoMapper, ApexStatusUserInfo> {

    public ApexStatusUserInfo getByUid(String uid) {
        LambdaQueryWrapper<ApexStatusUserInfo> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(ApexStatusUserInfo::getUid, uid);
        return getOne(lambdaQueryWrapper);
    }

}
