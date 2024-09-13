package com.jdragon.apex.entity.vo;

import lombok.Data;

@Data
public class UploadAttachmentsResult {

    private int id;

    private String upload_filename;

    private String upload_url;
}
