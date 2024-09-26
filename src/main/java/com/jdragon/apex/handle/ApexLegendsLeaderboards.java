package com.jdragon.apex.handle;

import cn.hutool.http.HttpUtil;
import com.jdragon.apex.entity.vo.PlayerInfo;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ApexLegendsLeaderboards {
    public List<PlayerInfo> getLegendsLeaderboards() {
        // 读取网页内容
        String url = "https://apexlegendsstatus.com/live-ranked-leaderboards/Battle_Royale/PC";
        String htmlContent = HttpUtil.get(url, StandardCharsets.UTF_8);

        // 更新正则表达式，提取名字、在线状态、UID、等级和分数
        String regex = "target=\"_alsLeaderboard\">([^<]*)</a>.*?<img[^>]*class=\"avatar ([^\"]*)\".*?loadSum\\('([^']*)'\\).*?style=\"color: white;\">(\\d+)</span>.*?white; font-size: 20px;\">([\\d,]+)</span>";
        Pattern pattern = Pattern.compile(regex, Pattern.DOTALL);
        return getPlayerInfos(pattern, htmlContent);
    }

    private @NotNull List<PlayerInfo> getPlayerInfos(Pattern pattern, String htmlContent) {
        Matcher matcher = pattern.matcher(htmlContent);

        // 存储提取结果的列表
        List<PlayerInfo> playerInfos = new ArrayList<>();

        // 提取并存储结果
        while (matcher.find()) {
            String name = matcher.group(1).trim();
            String onlineStatus = matcher.group(2).trim();
            String uid = matcher.group(3).trim();
            String level = matcher.group(4).trim();
            String score = matcher.group(5).trim();
            playerInfos.add(new PlayerInfo(name, onlineStatus, uid, level, score));
        }
        return playerInfos;
    }

}
