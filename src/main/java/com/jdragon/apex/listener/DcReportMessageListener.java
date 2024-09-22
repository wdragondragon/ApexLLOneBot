package com.jdragon.apex.listener;


import cn.hutool.core.img.ImgUtil;
import com.jdragon.apex.entity.ApexStatusUserInfo;
import com.jdragon.apex.handle.ApexUserInfoHandler;
import com.jdragon.apex.service.DiscordService;
import com.jdragon.apex.utils.ImageUtil;
import com.jdragon.cqhttp.CqListener;
import com.jdragon.cqhttp.constants.MessageType;
import com.jdragon.cqhttp.entity.Message;
import com.jdragon.cqhttp.entity.msg.AtMessage;
import com.jdragon.cqhttp.entity.msg.ImageMessage;
import com.jdragon.cqhttp.entity.msg.TextMessage;
import com.jdragon.cqhttp.message.ChatMessage;
import com.jdragon.test.OCRExample;
import lombok.extern.slf4j.Slf4j;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

@Slf4j
@Component
public class DcReportMessageListener {

    private final DiscordService discordService;

    private final ApexUserInfoHandler apexUserInfoHandler;

    public DcReportMessageListener(DiscordService discordService, ApexUserInfoHandler apexUserInfoHandler) {
        this.discordService = discordService;
        this.apexUserInfoHandler = apexUserInfoHandler;
    }


    @CqListener(type = MessageType.CHAT_MESSAGE, subType = "group")
    public void reportMessage(final ChatMessage message) throws TesseractException, IOException {
        List<Message> messages = message.getMessageChain().getMessages();
        if (messages.size() > 2 && messages.getFirst() instanceof AtMessage atMessage && messages.get(1) instanceof TextMessage textMessage) {
            if (atMessage.getQq() == message.getSelfId() && textMessage.eqStr("dc举报")) {
                List<byte[]> imageBytesList = new LinkedList<>();
                List<TextMessage> replyMsg = new LinkedList<>();
                replyMsg.add(new TextMessage("举报账号详情\n---------\n"));
                for (Message imageMsg : messages) {
                    if (imageMsg instanceof ImageMessage imageMessage) {
                        byte[] imageBytes = imageMessage.getImageBytes();
                        BufferedImage image = ImgUtil.toImage(imageBytes);
                        int width = image.getWidth();
                        int height = image.getHeight();
//                        if (width != 1920 || height != 1080) {
                        if (!ImageUtil.isAspectRatio16x9(width, height)) {
                            message.reply("只支持16:9的图片");
                            return;
                        }
                        int x1 = 1551;  // 起始x坐标
                        int y1 = 985;  // 起始y坐标
                        int x2 = 1859; // 结束x坐标
                        int y2 = 1008; // 结束y坐标
                        Rectangle rectangle = new Rectangle(x1, y1, x2 - x1, y2 - y1);
                        Image croppedImage = ImgUtil.cut(ImgUtil.scale(image, 1920, 1080), rectangle);
                        byte[] curBytes = ImgUtil.toBytes(croppedImage, "png");
                        String orcToString = OCRExample.orcToString(curBytes).trim();
                        ApexStatusUserInfo userInfo = apexUserInfoHandler.getUserInfo(orcToString);
                        if (userInfo == null) {
                            replyMsg.add(new TextMessage("未查询到uid：" + orcToString));
                        } else {
                            replyMsg.add(new TextMessage(userInfo + "\n---------\n"));
                        }
                        imageBytesList.add(imageBytes);
                    }
                }
                discordService.sendDiscordMessage("1229490771426541580", "", imageBytesList.toArray(new byte[0][]));
                message.reply(replyMsg.toArray(new TextMessage[0]));
            }
        }
    }
}
