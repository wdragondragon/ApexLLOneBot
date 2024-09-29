package com.jdragon.test;

import cn.hutool.core.io.FileUtil;
import com.jdragon.apex.service.Html2ImageBizImpl;

import java.nio.charset.StandardCharsets;


public class HtmlTest {
    public static void main(String[] args) {
        Html2ImageBizImpl html2ImageBiz = new Html2ImageBizImpl();
        String s = FileUtil.readString("C:\\Users\\jdrag\\Desktop\\d.html", StandardCharsets.UTF_8);
        byte[] imageBytes = html2ImageBiz.stringToPng(s, "--javascript-delay 3000");
    }
}
