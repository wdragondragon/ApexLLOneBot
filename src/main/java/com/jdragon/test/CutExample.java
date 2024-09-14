package com.jdragon.test;


import cn.hutool.core.io.FileUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.hutool.core.img.ImgUtil;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;

public class CutExample {
    public static void main(String[] args) throws IOException {
        // 1. 下载图片并获取 byte[]
        String url = "https://cdn.discordapp.com/attachments/1229490771426541580/1284416050275946526/Apex_Legends_2024_09_14_16_30_16.png?ex=66e68d29&is=66e53ba9&hm=10f3c912b6680b892201ab35388986c6f630d8cd70bf7dfd1a26e858460810fd&";
        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 7890));

        byte[] imageBytes = HttpRequest.get(url).setProxy(proxy)
                .execute().bodyBytes();

        // 2. 将 byte[] 转换为 BufferedImage
        BufferedImage image = ImgUtil.toImage(imageBytes);

        // 3. 获取图片的分辨率（宽度和高度）
        int width = image.getWidth();
        int height = image.getHeight();
        System.out.println("Image width: " + width + ", height: " + height);

        // 4. 根据给定的坐标 (x1, y1) 和 (x2, y2) 进行裁剪
        int x1 = 1551;  // 起始x坐标
        int y1 = 985;  // 起始y坐标
        int x2 = 1859; // 结束x坐标
        int y2 = 1008; // 结束y坐标
        Rectangle rectangle = new Rectangle(x1, y1, x2 - x1, y2 - y1);
        Image croppedImage = ImgUtil.cut(image, rectangle);
        byte[] bytes = ImgUtil.toBytes(croppedImage, "png");
        // 5. 将裁剪后的 byte[] 保存到文件
        File outputFile = new File("cropped_image.png");
        FileUtil.writeBytes(bytes, outputFile);
        System.out.println("Cropped image saved to " + outputFile.getAbsolutePath());
    }
}
