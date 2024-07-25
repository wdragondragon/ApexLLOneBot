package com.jdragon.cqhttp.message;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.jdragon.cqhttp.config.ObjectMapperHolder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.IOException;
import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class MetaEvent extends BaseMessage {
    private long interval;
    private String metaEventType;
    private String postType;
    private long selfId;
    private Map<String, Boolean> status;
    private long time;

    public static MetaEvent fromJson(String json) throws IOException {
        MetaEvent metaEvent = ObjectMapperHolder.MAPPER.readValue(json, MetaEvent.class);
        metaEvent.validateAttributes(ObjectMapperHolder.MAPPER.convertValue(metaEvent, new TypeReference<Map<String, Object>>() {
        }));
        return metaEvent;
    }
}
