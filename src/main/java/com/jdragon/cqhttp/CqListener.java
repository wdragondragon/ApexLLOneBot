package com.jdragon.cqhttp;

import com.jdragon.cqhttp.constants.MessageType;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CqListener {
    @AliasFor("type")
    MessageType value() default MessageType.CHAT_MESSAGE;

    @AliasFor("value")
    MessageType type() default MessageType.CHAT_MESSAGE;
}
