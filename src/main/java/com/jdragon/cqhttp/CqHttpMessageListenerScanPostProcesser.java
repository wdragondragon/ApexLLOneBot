package com.jdragon.cqhttp;

import com.jdragon.cqhttp.constants.MessageType;
import lombok.Getter;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class CqHttpMessageListenerScanPostProcesser implements BeanPostProcessor {

    @Getter
    private static final List<CqHttpListenerMethod> listenerMethods = new ArrayList<>();

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Class<?> clazz = bean.getClass();
        Method[] methods = ReflectionUtils.getAllDeclaredMethods(clazz);
        for (Method method : methods) {
            AnnotationAttributes annotationAttributes = AnnotatedElementUtils
                    .findMergedAnnotationAttributes(method, CqListener.class, false, false);
            if (null != annotationAttributes) {
                Class<?>[] parameterTypes = method.getParameterTypes();
                if (parameterTypes.length == 1) {
                    MessageType messageType = (MessageType) annotationAttributes.get("type");
                    Type[] genericParameterTypes = method.getGenericParameterTypes();
                    CqHttpListenerMethod rlm = new CqHttpListenerMethod();
                    rlm.setBeanName(beanName);
                    rlm.setTargetMethod(method);
                    rlm.setMethodParameterClassName(parameterTypes[0].getName());
                    rlm.setParameterClass(parameterTypes[0]);
                    rlm.setParameterType(genericParameterTypes[0]);
                    rlm.setMessageType(messageType);
                    listenerMethods.add(rlm);
                }
            }
        }
        return bean;
    }

}
