package com.jdragon.apex.entity.vo;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.jdragon.apex.config.CustomLocalDateTimeDeserializer;
import com.jdragon.apex.config.CustomLocalDateTimeSerializer;
import lombok.Data;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
public class DiscordMessage {
    private int type;

    private String content;

    private List<Map<String, Object>> mentions;

    private List<String> mention_roles;

    private List<Attachments> attachments;

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

    // send
    private String nonce = String.valueOf(new BigInteger(62, new SecureRandom()).toString());

    private List<String> sticker_ids = new ArrayList<>();

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

    @Data
    public static class Attachments {

        private String id;

        private Integer content_scan_version;

        private String content_type;

        private String filename;

        private Integer height;

        private Integer width;

        private String placeholder;

        private Integer placeholder_version;

        private String proxy_url;

        private String url;

        private long size;


        // upload
        private long file_size;

        private boolean is_clip;


        // upload result
        private String upload_filename;

        private String upload_url;

    }
}
