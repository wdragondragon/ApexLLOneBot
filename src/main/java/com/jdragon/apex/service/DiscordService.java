package com.jdragon.apex.service;


import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.generator.SnowflakeGenerator;
import com.jdragon.apex.client.DiscordClient;
import com.jdragon.apex.client.DiscordAttachmentsUploadsClient;
import com.jdragon.apex.entity.vo.DiscordMessage;
import com.jdragon.apex.entity.vo.UploadAttachments;
import feign.Feign;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
public class DiscordService {
    private final DiscordClient discordClient;

    @Setter
    @Value("${dc.auth}")
    private String authorization;

    private final DiscordAttachmentsUploadsClient discordAttachmentsUploadsClient;

    public DiscordService(DiscordClient discordClient, DiscordAttachmentsUploadsClient discordAttachmentsUploadsClient) {
        this.discordClient = discordClient;
        this.discordAttachmentsUploadsClient = discordAttachmentsUploadsClient;
    }

    public List<DiscordMessage> getDiscordMessages(String channelId, Integer limit) {
        return discordClient.getDiscordMessage(channelId, limit, authorization);
    }

    public DiscordMessage sendDiscordMessage(String channelId, String content, byte[]... screenshots) {
        List<DiscordMessage.Attachments> attachments = new ArrayList<>();
        for (byte[] bytes : screenshots) {
            UploadAttachments uploadAttachments = new UploadAttachments();

            // 构建文件附件上传链接
            UploadAttachments.File file = new UploadAttachments.File();
            file.setId("100");
            file.setFilename("image.png");
            file.setFile_size(bytes.length);
            uploadAttachments.setFiles(List.of(file));
            DiscordMessage discordMessage = discordClient.sendDiscordAttachments(channelId, uploadAttachments, authorization);

            List<DiscordMessage.Attachments> attachmentsList = discordMessage.getAttachments();

            // 根据返回的上传链接上传图片。
            DiscordMessage.Attachments attachment = attachmentsList.getFirst();

            String uploadUrl = attachment.getUpload_url();
            String uploadId = extractUploadId(uploadUrl);
            String uploadFilename = attachment.getUpload_filename();

            ResponseEntity<Void> voidResponseEntity = discordAttachmentsUploadsClient.uploadImage(uploadFilename, bytes, uploadId);
            if (voidResponseEntity.getStatusCode().is2xxSuccessful()) {
                DiscordMessage.Attachments disAttach = new DiscordMessage.Attachments();
                disAttach.setUploaded_filename(uploadFilename);
                disAttach.setFilename("image.png");
                disAttach.setId("0");
                attachments.add(disAttach);
            } else {
                throw new RuntimeException("上传附件失败");
            }
        }


        DiscordMessage message = new DiscordMessage();
        message.setContent(content);
        message.setAttachments(attachments);
        message.setChannel_id(channelId);
        message.setNonce(String.valueOf(new SnowflakeGenerator().next()));
        return discordClient.sendDiscordMessage(channelId, message, authorization);
    }

    public DiscordMessage testUploadImage() {
        byte[] bytes = FileUtil.readBytes("C:\\Users\\jdrag\\Desktop\\1.png");
        return sendDiscordMessage("1129793224282738731", "举报内容", bytes);
    }

    public static String extractUploadId(String url) {
        Pattern pattern = Pattern.compile("upload_id=([^&]*)");
        Matcher matcher = pattern.matcher(url);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }
}
