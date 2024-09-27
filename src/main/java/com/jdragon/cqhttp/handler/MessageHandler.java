package com.jdragon.cqhttp.handler;

import com.fasterxml.jackson.core.type.TypeReference;
import com.jdragon.cqhttp.CqHttpListenerMethod;
import com.jdragon.cqhttp.CqHttpMessageListenerScanPostProcesser;
import com.jdragon.cqhttp.config.ObjectMapperHolder;
import com.jdragon.cqhttp.constants.MessageType;
import com.jdragon.cqhttp.message.BaseMessage;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;


import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

@Slf4j
public class MessageHandler implements ApplicationContextAware {

    private static final List<BaseHandler<? extends BaseMessage>> handlers = new ArrayList<>();

    static {
        reg(new ChatMessageHandler());
        reg(new MetaEventHandler());
        reg(new NoticeEventHandler());
    }

    private ApplicationContext applicationContext;

    public static void reg(BaseHandler<? extends BaseMessage> handler) {
        handlers.add(handler);
    }

    public void handle(String json) throws IOException {
        Map<String, Object> map = ObjectMapperHolder.SNAKE_CASE_MAPPER.readValue(json, new TypeReference<>() {
        });
        String postType = (String) map.get("post_type");

        MessageType messageType = MessageType.getByCode(postType);
        if (messageType == null) {
            log.warn("Unsupported post_type: {}", postType);
            return;
        }
        handlers.stream().filter(handler -> Objects.equals(messageType, handler.getType())).forEach(handler -> {
            try {
                handler.handleMsg(json);
            } catch (IOException e) {
                log.error("[{}][{}]处理器处理异常：{}", postType, handler.getClass().getName(), e.getMessage(), e);
            }
        });

        List<CqHttpListenerMethod> listenerMethods = CqHttpMessageListenerScanPostProcesser.getListenerMethods();
        listenerMethods.stream().filter(listener -> listener.getMessageType() == messageType).forEach(rlm -> {
            Method targetMethod = rlm.getTargetMethod();
            if (BaseMessage.class.isAssignableFrom(rlm.getParameterClass())) {
                for (BaseHandler<? extends BaseMessage> handler : handlers) {
                    if (Objects.equals(messageType, handler.getType())) {
                        try {
                            BaseMessage msgObject = handler.createMsgObject(json);
                            if (StringUtils.isAnyBlank(msgObject.getType(), rlm.getSubType())
                                    || Objects.equals(msgObject.getType(), rlm.getSubType())) {
                                targetMethod.invoke(rlm.getBean(applicationContext), msgObject);
                            }
                        } catch (IOException | IllegalAccessException | InvocationTargetException e) {
                            log.error("[{}][{}]处理器处理异常：{}", postType, handler.getClass().getName(), e.getMessage(), e);
                        }
                    }
                }
            }
        });
    }

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
