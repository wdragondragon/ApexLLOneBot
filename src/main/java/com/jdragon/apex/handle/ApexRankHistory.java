package com.jdragon.apex.handle;

import com.fasterxml.jackson.core.type.TypeReference;
import com.jdragon.apex.client.ApexStatusClient;
import com.jdragon.apex.entity.vo.RankData;
import com.jdragon.cqhttp.config.ObjectMapperHolder;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ApexRankHistory {

    private final ApexStatusClient apexStatusClient;

    public ApexRankHistory(ApexStatusClient apexStatusClient) {
        this.apexStatusClient = apexStatusClient;
    }

    @SneakyThrows
    public List<RankData> getRankHistory(String season, String uid) {
        String gameHistoryStr = apexStatusClient.gameHistory(season, uid);
        // 使用正则表达式提取 JSON 数据
        Pattern pattern = Pattern.compile("<div[^>]*id=\"mmChartv3JsonPayload\"[^>]*>(.*?)</div>", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(gameHistoryStr);

        List<RankData> rankDataList = new ArrayList<>();
        if (matcher.find()) {
            String json = matcher.group(1).trim();
            // 去掉字符串两端的引号
            rankDataList = ObjectMapperHolder.MAPPER.readValue(json, new TypeReference<>() {
            });
        }
        return rankDataList;
    }
}
