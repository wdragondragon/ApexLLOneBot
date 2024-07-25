package com.jdragon.cqhttp;

import com.jdragon.cqhttp.constants.MessageType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CqHttpListenerMethod {
    private volatile Object bean;

    private String beanName;

    private Method targetMethod;

    private String methodParameterClassName;

    private Class<?> parameterClass;

    private Type parameterType;

    private MessageType messageType;

    public Object getBean(ApplicationContext applicationContext) {
        if (bean == null) {
            synchronized (this) {
                if (bean == null) {
                    bean = applicationContext.getBean(beanName);
                }
            }
        }
        return bean;
    }
}
