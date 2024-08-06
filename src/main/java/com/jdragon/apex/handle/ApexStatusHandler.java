package com.jdragon.apex.handle;

import com.jdragon.apex.client.ApexStatusClient;
import com.jdragon.apex.entity.ApexStatusUserInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
public class ApexStatusHandler {

    private final ApexStatusClient apexStatusClient;

    private static final String COOKIE = """
            _ga=GA1.1.1750590508.1711836871; cc_cookie={"level":["necessary","recaptcha"],"revision":0,"data":null,"rfc_cookie":false}; apexlegendsstatus_ssid=al2sv02fc95u1g2rkog04pv7r0; _ga_HGQ7L9V5FY=GS1.1.1722760461.14.1.1722763178.0.0.0
            """.trim();

    private static final Pattern PATTERN_PROFILE = Pattern.compile("https://apexlegendsstatus\\.com/profile/uid/([a-zA-Z0-9]+)/([0-9]+)");

    private static final Pattern PATTERN_NAME = Pattern.compile("<input[^>]*type=\"hidden\"[^>]*value=\"([^\"]*)\"[^>]*id=\"pname\"[^>]*>");

    private static final Pattern PATTERN_PID = Pattern.compile("<input[^>]*type=\"hidden\"[^>]*value=\"([^\"]*)\"[^>]*id=\"puid\"[^>]*>");

    private static final Pattern PATTERN_RP = Pattern.compile("<p[^>]*>([0-9,]+) RP</p>");

    private static final Pattern PATTERN_LEVEL = Pattern.compile("level=(\\d+)");

    public ApexStatusHandler(ApexStatusClient apexStatusClient) {
        this.apexStatusClient = apexStatusClient;
    }

    public ApexStatusUserInfo getUserInfo(String param) {
        ApexStatusUserInfo apexStatusUserInfo = getUserInfo("PC", "uid", param);
        if (apexStatusUserInfo == null) {
            return getUserInfo("PC", "name", param);
        }
        return apexStatusUserInfo;
    }

    public ApexStatusUserInfo getUserInfo(String platform, String type, String param) {
        if ("uid".equals(type)) {
            return getUserInfoByUid(platform, param);
        } else {
            return getUserInfoByName(platform, param);
        }
    }

    public ApexStatusUserInfo getUserInfoByUid(String platform, String uid) {
        ApexStatusUserInfo userInfo = new ApexStatusUserInfo();
        userInfo.setPlatform(platform);
        userInfo.setUid(uid);

        String pid = getPidFromUid(platform, uid);
        if (StringUtils.isNotBlank(pid)) {
            userInfo.setPid(pid);
            try {
                String csrfPreProd = apexStatusClient.coreInterface("CSRF_PRE_PROD", platform, pid, COOKIE);
                coreInterface(userInfo, csrfPreProd);
            } catch (Exception e) {
                log.error("获取段位和等级异常", e);
            }
            return userInfo;
        }
        return null;
    }

    public ApexStatusUserInfo getUserInfoByName(String platform, String name) {
        ApexStatusUserInfo userInfo = new ApexStatusUserInfo();
        userInfo.setPlatform(platform);
        userInfo.setName(name);
        try {
            String csrfPreProd = apexStatusClient.coreInterfaceByName("CSRF_PRE_PROD", platform, name, COOKIE);
            coreInterface(userInfo, csrfPreProd);
            return userInfo;
        } catch (Exception e) {
            log.error("获取段位和等级异常", e);
        }
        return null;
    }

    public void coreInterface(ApexStatusUserInfo apexStatusUserInfo, String csrfPreProd) {
        Matcher matcherRP = PATTERN_RP.matcher(csrfPreProd);
        Matcher matcherLevel = PATTERN_LEVEL.matcher(csrfPreProd);
        Matcher matcherPUID = PATTERN_PID.matcher(csrfPreProd);
        Matcher matcherName = PATTERN_NAME.matcher(csrfPreProd);
        boolean avatarOnline = csrfPreProd.contains("avatar_online");
        apexStatusUserInfo.setOnline(avatarOnline);
        if (matcherRP.find() && matcherLevel.find() && matcherPUID.find() && matcherName.find()) {
            apexStatusUserInfo.setRp(Integer.parseInt(matcherRP.group(1).replace(",", "")));
            apexStatusUserInfo.setLevel(Integer.parseInt(matcherLevel.group(1)));
            apexStatusUserInfo.setPid(matcherPUID.group(1));
            apexStatusUserInfo.setName(matcherName.group(1));
        }
    }

    public String getPidFromUid(String platform, String uid) {
        String profile = apexStatusClient.profile(platform, uid);
        Matcher matcher = PATTERN_PROFILE.matcher(profile);
        if (matcher.find()) {
            return matcher.group(2);
        }
        return null;
    }
}
