package com.jdragon.apex.entity.vo;

import lombok.Data;

import java.util.List;

@Data
public class UploadAttachments {

    private List<File> files;

    @Data
    public static class File {
        private long file_size;

        private String filename;

        private String id;

        private boolean is_clip;
    }


}
