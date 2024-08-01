package com.jdragon.apex.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.jayway.jsonpath.JsonPath;
import com.jdragon.apex.client.OpenAiClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

@Service
public class OpenAiService {
    private final Map<String, JsonNode> historyMessagesMap = new HashMap<>();
    private final int MAX_HISTORY_LENGTH;
    private final int MAX_TOTAL_WORDS;
    private final String API_KEY;

    private final OpenAiClient openAiClient;

    private final ObjectMapper objectMapper;

    @Autowired
    public OpenAiService(
            @Value("${max.history.length:50}") int maxHistoryLength,
            @Value("${max.total.words:5000}") int maxTotalWords,
            @Value("${api.key:}") String apiKey, OpenAiClient openAiClient, ObjectMapper objectMapper) {
        this.MAX_HISTORY_LENGTH = maxHistoryLength;
        this.MAX_TOTAL_WORDS = maxTotalWords;
        this.API_KEY = apiKey;
        this.openAiClient = openAiClient;
        this.objectMapper = objectMapper;
    }

    public String aiMsg(String id, String msg) {
        JsonNode historyMessages = historyMessagesMap.computeIfAbsent(id, k -> objectMapper.createArrayNode());

        // 添加新的消息
        JsonNode newMessage = objectMapper.createObjectNode()
                .put("role", "user")
                .put("content", msg);
        ((ArrayNode) historyMessages).add(newMessage);

        // 计算总字数
        int totalWords = 0;
        for (JsonNode message : historyMessages) {
            totalWords += message.get("content").asText().length();
        }

        // 检查历史消息数量和总字数，并移除最旧的消息以保持长度和字数限制
        while (historyMessages.size() > MAX_HISTORY_LENGTH || totalWords > MAX_TOTAL_WORDS) {
            JsonNode removedMessage = ((ArrayNode) historyMessages).remove(0);
            totalWords -= removedMessage.get("content").asText().length();
        }

        // 构造 JSON 请求体
        ObjectNode requestBody = objectMapper.createObjectNode()
                .put("model", "gpt-3.5-turbo")
                .set("messages", historyMessages);
        String data = requestBody.toString();

        int retry = 0;
        while (retry < 3) {
            try {
                String response = openAiClient.sendMessage("Bearer " + API_KEY, data);
                // 使用 JsonPath 解析 JSON 响应
                return JsonPath.read(response, "$.choices[0].message.content");
            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
                retry++;
            }
        }
        return "";
    }
}
