package com.jdragon.apex.client;

import com.jdragon.apex.config.ProxyFeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@FeignClient(name = "discordAttachmentsUploadsClient", url = "https://discord-attachments-uploads-prd.storage.googleapis.com", configuration = ProxyFeignConfig.class)
public interface DiscordAttachmentsUploadsClient {

    @PutMapping(value = "/{imagePath}", consumes = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    ResponseEntity<Void> uploadImage(
            @PathVariable String imagePath,
            @RequestBody byte[] imageBytes,
            @RequestParam String upload_id
    );
}
