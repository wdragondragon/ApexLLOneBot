package com.jdragon.apex.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.jdragon.apex.config.CustomLocalDateTimeDeserializer;
import com.jdragon.apex.config.CustomLocalDateTimeSerializer;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

@Data
public class DiscordMessage {
    private int type;
    private String content;
    private List<Map<String, Object>> mentions;
    private List<String> mention_roles;
    private List<Map<String, Object>> attachments;
    private List<Embed> embeds;
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    private LocalDateTime timestamp;
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    private LocalDateTime edited_timestamp;
    private int flags;
    private List<Map<String, Object>> components;
    private String id;
    private String channel_id;
    private Author author;
    private boolean pinned;
    private boolean mention_everyone;
    private boolean tts;
    private String webhook_id;

    @Data
    public static class Embed {
        private String type;
        private String description;
        private int color;
        private int content_scan_version;
    }

    @Data
    public static class Author {
        private String id;
        private String username;
        private String avatar;
        private String discriminator;
        private int public_flags;
        private int flags;
        private boolean bot;
        private String global_name;
        private String clan;
    }
}
