package snipurl.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UrlErrorResponseDto {
    private String status;
    private String error;
}