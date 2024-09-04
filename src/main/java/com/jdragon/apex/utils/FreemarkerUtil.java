package com.jdragon.apex.utils;

import com.jdragon.apex.holder.SpringContextHolder;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;


/**
 * freemarker工具类
 */
@Slf4j
public class FreemarkerUtil {

    public static Configuration getDefaultConfig() {
        Configuration configuration = getConfigByVersion();
        configuration.setClassForTemplateLoading(
                FreemarkerUtil.class, "/templates/");
        configuration.setDefaultEncoding("UTF-8");
        return configuration;
    }

    /**
     * 获取模板的配置对象 注意：以下是官网说明（不需要重复创建 Configuration 实例；
     * 它的代价很高，尤其是会丢失缓存。Configuration 实例就是应用级别的单例。 不管一个系统有多少独立的组件来使用 FreeMarker，
     * 它们都会使用他们自己私有的 Configuration 实例）
     */
    public static Configuration getConfig() {
        Configuration cfg = null;
        try {
            try {
                cfg = SpringContextHolder.getBean(Configuration.class);
            } catch (IllegalStateException e) {
                cfg = getDefaultConfig();
            }
        } catch (Exception e) {
            log.info("系统异常：{}", e.getMessage(), e);
        }

        return cfg;
    }

    /**
     * 根据版本号获取config，该方式无需在容器中启动也可获取
     */
    public static Configuration getConfigByVersion() {
        return new Configuration(Configuration.getVersion());
    }

    /**
     * @param ftlPath 文件夹路径
     * @param ftlName 模板文件名
     */
    public static Template getTemplate(String ftlPath, String ftlName) {
        Configuration cfg = null;
        Template t = null;
        try {
            cfg = getConfig();
            t = cfg.getTemplate(ftlPath + ftlName);
        } catch (Exception e) {
            log.info("系统异常：{}", e.getMessage());
        }
        return t;
    }


    public static String printString(String path, String name, Map<String, Object> root) {
        StringWriter writer = new StringWriter();
        print(path, name, root, writer);
        return writer.toString();
    }

    public static void print(String path, String name, Map<String, Object> root, Writer writer) {
        try {
            Template template = getTemplate(path, name);
            template.process(root, writer);//在控制台输出内容
        } catch (TemplateException | IOException e) {
            log.error("", e);
        }
    }
}
