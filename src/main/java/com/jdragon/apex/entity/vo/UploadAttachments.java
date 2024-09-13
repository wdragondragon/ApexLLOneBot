package com.jdragon.apex.entity.vo;

import lombok.Data;

@Data
public class UploadAttachments {

    private long file_size;

    private String filename;

    private String id;

    private boolean is_clip;
}
