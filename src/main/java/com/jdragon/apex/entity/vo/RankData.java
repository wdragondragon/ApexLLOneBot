package com.jdragon.apex.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import lombok.Data;

import java.io.IOException;
import java.util.Date;

@Data
public class RankData {
    @JsonDeserialize(using = TimestampToDateDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date timestamp;

    private int rankScore;

    private int ladder;

    static class TimestampToDateDeserializer extends StdDeserializer<Date> {
        public TimestampToDateDeserializer() {
            this(null);
        }

        public TimestampToDateDeserializer(Class<?> vc) {
            super(vc);
        }

        @Override
        public Date deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            long timestamp = p.getLongValue();
            return new Date(timestamp * 1000); // 转为13位
        }
    }

}

