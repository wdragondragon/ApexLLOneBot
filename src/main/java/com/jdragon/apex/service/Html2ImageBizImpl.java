package com.jdragon.apex.service;

import com.jdragon.apex.utils.FreemarkerUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * HTML转图片的默认实现，采用wkhtmltox实现
 * <p>
 * wkhtmltox基于webkit浏览器将HTML转为图片，需要一个程序来执行，对于window来说是一个无需安装的exe
 * 对于linux来说需要进行安装
 */
@Slf4j
@Service
public class Html2ImageBizImpl {

    /**
     * wkhtmltox的使用命令，对window来讲是exe的地址，对于linux是安装的命令地址（如/usr/local/bin/wkhtmltoimg）
     */
    private String wkcmd = "C:/Program Files/wkhtmltopdf/bin/wkhtmltoimage.exe";
    /**
     * 用于存放临时文件的文件夹，由于wkhtmltox为系统插件独立进程，只能作为文件操作后再按流进行读取
     */
    private String tmpFilePath = "C:/dev/tmp";

    public byte[] stringToPng(String htmlString, String... params) {
        if (StringUtils.isBlank(wkcmd)) {
            throw new UnsupportedOperationException("未配置wkhtmltox的使用命令参数，功能不可用");
        }
        if (StringUtils.isBlank(tmpFilePath)) {
            throw new UnsupportedOperationException("未配置HTML转图片的临时文件目录，功能不可用");
        }
        Assert.hasLength(htmlString, "参数错误，HTML文件没有内容");

        String htmlFileName = null;
        String pngFileName = null;
        try {
            // 存HTML文件
            htmlFileName = saveHtml2File(htmlString);
            // 转为PNG,获取其文件名
            pngFileName = html2Png(htmlFileName, params);
            // 将PNG读取为bytes
            return readFile2ByteArray(pngFileName);

        } finally {
            // 删除临时文件
            if (htmlFileName != null) {
                removeTmpFile(htmlFileName);
            }
            if (pngFileName != null) {
                removeTmpFile(pngFileName);
            }
        }
    }

    /**
     * 将HTML内容写入文件
     *
     * @param htmlString HTML文件内容
     */
    private String saveHtml2File(String htmlString) {
        String fileName = getRandomFileName(".html");
        try (FileWriter fileWriter = new FileWriter(new File(fileName))) {
            fileWriter.write(htmlString);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        log.info("保存HTML成功: {}", fileName);
        return fileName;
    }

    /**
     * 使用wkhtmltox将HTML转为PNG（都在硬盘上操作
     *
     * @param htmlFileName HTML文件路径
     */
    private String html2Png(String htmlFileName, String... params) {
        String pngFileName = getRandomFileName(".png");

        StringBuilder cmd = new StringBuilder();
        cmd.append(wkcmd);
        cmd.append(" ");
        for (String param : params) {
            cmd.append(param);
            cmd.append(" ");
        }
        cmd.append(htmlFileName);
        cmd.append(" ");
        cmd.append(pngFileName);
        try {
            Process proc = Runtime.getRuntime().exec(cmd.toString());
            proc.getInputStream();
            proc.waitFor();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        log.info("转PNG成功: {}, {}", htmlFileName, pngFileName);
        return pngFileName;
    }

    /**
     * 读取文件二进制数组
     *
     * @param fileName
     */
    private byte[] readFile2ByteArray(String fileName) {
        File file = new File(fileName);
        try {
            return Files.readAllBytes(file.toPath());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 删除临时文件
     *
     * @param fileName 文件名
     */
    private void removeTmpFile(String fileName) {
        Assert.hasLength(fileName, "临时文件参数为空");
        File file = new File(fileName);
        if (file.isDirectory()) {
            throw new IllegalArgumentException("不能删除文件夹");
        }
        try {
            Files.delete(file.toPath());
            log.info("删除临时文件成功：{}", fileName);
        } catch (Exception e) {
            log.warn("删除临时文件失败", e);
        }
    }

    /**
     * 构建随机的文件地址（采用IdGenerator）
     *
     * @param suffix 如.html
     */
    private String getRandomFileName(String suffix) {
        return tmpFilePath + "/" + "h2p" + UUID.randomUUID() + suffix;
    }

    public static void main(String[] args) throws FileNotFoundException {
        Map<String, Object> data = new HashMap<>();
        data.put("title", "User Information Table");
        data.put("tableHeaders", Arrays.asList("ID", "First Name", "Last Name", "Username"));
        data.put("tableRows", Arrays.asList(
                Arrays.asList(1, "John", "Doe", "@johndoe"),
                Arrays.asList(2, "Jane", "Smith", "@janesmith"),
                Arrays.asList(3, "Sam", "Brown", "@sambrown"),
                Arrays.asList(4, "Lisa", "Johnson", "@lisajohnson")
        ));


        String s = FreemarkerUtil.printString("", "table.ftl", data);
        Html2ImageBizImpl html2ImageBiz = new Html2ImageBizImpl();
        html2ImageBiz.stringToPng(s);
    }
}