package com.jdragon.test;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;

public class OCRExample {
    public static String orcToString(String url) throws TesseractException, IOException {
        Tesseract tesseract = new Tesseract();
        tesseract.setDatapath("C:\\Program Files\\Tesseract-OCR\\tessdata"); // 设置tessdata的路径
        tesseract.setLanguage("eng");
        // 加载图片文件
        URI uri = URI.create(url);
        URL imageUrl = uri.toURL();
        // 进行OCR识别
        try (InputStream imageStream = imageUrl.openStream()) {
            // 将输入流转换为BufferedImage
            BufferedImage image = ImageIO.read(imageStream);
            // 进行OCR识别
            String result = tesseract.doOCR(image);
            // 输出识别结果
            System.out.println("识别结果: " + result);
            return result;
        }
    }
}
