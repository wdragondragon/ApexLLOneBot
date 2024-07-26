package com.jdragon.cqhttp.message;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class BaseMessage {
    @JsonIgnore
    private Map<String, Object> missingAttributes = new HashMap<>();

    @JsonIgnore
    private Map<String, Object> extraAttributes = new HashMap<>();

    private String type;

    @JsonAnySetter
    public void setExtraAttributes(String key, Object value) {
        extraAttributes.put(key, value);
    }

    @JsonAnyGetter
    public Map<String, Object> getExtraAttributes() {
        return extraAttributes;
    }

    @JsonIgnore
    public void validateAttributes(Map<String, Object> attributes) {
        attributes.forEach((key, value) -> {
            if (value == null) {
                missingAttributes.put(key, null);
            }
        });
    }
}
