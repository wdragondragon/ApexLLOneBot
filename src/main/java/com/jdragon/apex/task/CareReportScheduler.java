package com.jdragon.apex.task;

import cn.hutool.core.io.IoUtil;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.jdragon.apex.config.SystemConstants;
import com.jdragon.apex.entity.DcReport;
import com.jdragon.apex.entity.vo.DiscordMessage;
import com.jdragon.apex.service.DcReportService;
import com.jdragon.apex.service.DiscordService;
import com.jdragon.cqhttp.entity.Message;
import com.jdragon.cqhttp.entity.msg.ImageMessage;
import com.jdragon.cqhttp.entity.msg.TextMessage;
import com.jdragon.cqhttp.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

@Slf4j
@Component
public class CareReportScheduler {

    private final SystemConstants systemConstants;

    private final DiscordService discordService;

    private final MessageService messageService;

    private final DcReportService dcReportService;

    public CareReportScheduler(SystemConstants systemConstants, DiscordService discordService, MessageService messageService, DcReportService dcReportService) {
        this.systemConstants = systemConstants;
        this.discordService = discordService;
        this.messageService = messageService;
        this.dcReportService = dcReportService;
    }

    @Scheduled(fixedRate = 300000)
    public void performTask() {
        if (!systemConstants.isScheduler()) {
            return;
        }
        List<DiscordMessage> messages = discordService.getDiscordMessages("1229490771426541580", 10);

        for (DiscordMessage message : messages) {
            List<String> picUrlList = new LinkedList<>();
            for (DiscordMessage.Attachments attachment : message.getAttachments()) {
                if (!attachment.getContent_type().contains("image")) {
                    continue;
                }
                String id = attachment.getId();
                String proxyUrl = attachment.getProxy_url();
                DcReport byId = dcReportService.getById(id);
                if (byId == null) {
                    DcReport dcReport = new DcReport();
                    dcReport.setId(id);
                    dcReport.setUrl(proxyUrl);
                    picUrlList.add(proxyUrl);
                    dcReportService.save(dcReport);
                }
            }
            if (!picUrlList.isEmpty()) {
                List<Message> imageMessages = new LinkedList<>();
                imageMessages.add(new TextMessage("收到dc举报"));
                for (String picUrl : picUrlList) {
                    byte[] bytes = downloadImageAsBytes(picUrl);
                    imageMessages.add(new ImageMessage(bytes));
                }
                messageService.sendGroupMsg(206666041, imageMessages.toArray(new Message[0]));
            }
        }
    }

    public static byte[] downloadImageAsBytes(String imageUrl) {
        try (HttpResponse execute = HttpUtil.createGet(imageUrl).setHttpProxy("127.0.0.1", 7890).execute();
             InputStream inputStream = execute.bodyStream()) {
            // 将 InputStream 转换为 byte[]
            return IoUtil.readBytes(inputStream);
        } catch (Exception e) {
            log.error("", e);
            return null;
        }
    }
}
