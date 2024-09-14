package com.jdragon.apex.handle;

import com.jdragon.apex.client.ApexApiClient;
import com.jdragon.apex.entity.ApexStatusUserInfo;
import com.jdragon.apex.entity.vo.apexapi.ApexUser;
import com.jdragon.apex.entity.vo.apexapi.Global;
import com.jdragon.apex.entity.vo.apexapi.Realtime;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ApexUserInfoHandler {

    private final ApexApiClient apexApiClient;

    private final ApexStatusHandler apexStatusHandler;

    public ApexUserInfoHandler(ApexApiClient apexApiClient, ApexStatusHandler apexStatusHandler) {
        this.apexApiClient = apexApiClient;
        this.apexStatusHandler = apexStatusHandler;
    }

    public ApexStatusUserInfo getUserInfo(String param) {
        try {
            ApexStatusUserInfo apexStatusUserInfo = getUserInfo("PC", "uid", param);
            if (apexStatusUserInfo == null) {
                return getUserInfo("PC", "name", param);
            }
            return apexStatusUserInfo;
        } catch (Exception e) {
            return null;
        }

    }

    public ApexStatusUserInfo getUserInfo(String platform, String type, String param) {
        ApexUser apexUser;
        ApexStatusUserInfo apexStatusUserInfo = new ApexStatusUserInfo();

        if ("uid".equals(type)) {
            String uid = apexStatusHandler.getPidFromUid(platform, param);
            if (StringUtils.isBlank(uid)) {
                return null;
            }
            apexUser = apexApiClient.getByUid(platform, uid);
        } else {
            apexUser = apexApiClient.getByName(platform, param);
        }

        Global global = apexUser.getGlobal();
        Global.Rank rank = global.getRank();
        Realtime realtime = apexUser.getRealtime();
        apexStatusUserInfo.setName(global.getName());
        apexStatusUserInfo.setPid(global.getUid());
        apexStatusUserInfo.setUid(global.getUid());
        apexStatusUserInfo.setLevel(global.getLevel() + global.getLevelPrestige() * 500);
        apexStatusUserInfo.setPlatform(platform);
        apexStatusUserInfo.setRp(rank.getRankScore());
        apexStatusUserInfo.setOnline(realtime.getIsOnline() == 1);
        return apexStatusUserInfo;
    }

}
