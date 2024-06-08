package snipurl.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ErrorDto {
    private String statusCode;
    private LocalDateTime timestamp;
    private String path;
    private String errorMessage;
    private String errorCode;
}
