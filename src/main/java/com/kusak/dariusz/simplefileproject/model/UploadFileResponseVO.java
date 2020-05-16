package com.kusak.dariusz.simplefileproject.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UploadFileResponseVO {

    private String fileName;
    private String fileDownloadUri;
    private String fileType;
    private long size;
}
