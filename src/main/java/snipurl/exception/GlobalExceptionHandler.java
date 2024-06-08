package snipurl.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import snipurl.dto.ErrorDto;

import java.time.LocalDateTime;

import static snipurl.utils.Constants.*;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorDto> resourceNotFoundExceptionHandler(ResourceNotFoundException e, WebRequest webRequest) {
        ErrorDto errorDto = ErrorDto.builder()
                .timestamp(LocalDateTime.now())
                .statusCode(STATUS_CODE_404)
                .errorMessage(String.format(NOT_FOUND_ERROR_MESSAGE, e.getResourceName(), e.getResourceValue()))
                .errorCode(NOT_FOUND)
                .path(webRequest.getDescription(false))
                .build();
        return new ResponseEntity<>(errorDto, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ExpiredURLException.class)
    public ResponseEntity<ErrorDto> emptyInputExceptionHandler(ExpiredURLException e, WebRequest webRequest) {
        ErrorDto errorDto = ErrorDto.builder()
                .timestamp(LocalDateTime.now())
                .statusCode(STATUS_CODE_400)
                .errorMessage(e.getErrorMessage())
                .errorCode(INVALID_INPUT)
                .path(webRequest.getDescription(false))
                .build();
        return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
    }
}
