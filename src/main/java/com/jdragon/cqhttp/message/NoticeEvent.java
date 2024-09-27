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
public class NoticeEvent extends BaseMessage {
    private long time;
    private long selfId;
    private String postType;
    private long groupId;
    private long userId;
    private String noticeType;
    private String cardNew;
    private String cardOld;

    public static NoticeEvent fromJson(String json) throws IOException {
        NoticeEvent noticeEvent = ObjectMapperHolder.SNAKE_CASE_MAPPER.readValue(json, NoticeEvent.class);
        noticeEvent.validateAttributes(ObjectMapperHolder.SNAKE_CASE_MAPPER.convertValue(noticeEvent, new TypeReference<Map<String, Object>>() {
        }));
        return noticeEvent;
    }

    @Override
    public String getType() {
        return noticeType;
    }
}
