package ru.binarshiki.binmedia.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseDto {
    String id;
    String objectName;
    String bucket;
}
