package snipurl.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import snipurl.dto.UrlDto;
import snipurl.dto.UrlErrorResponseDto;
import snipurl.entity.Url;
import snipurl.service.SnipUrlService;

import java.io.IOException;
import java.time.LocalDateTime;

import static snipurl.utils.Constants.*;

@RestController
@RequiredArgsConstructor
public class SnipUrlController {

    private final SnipUrlService snipUrlService;
    private final HttpServletResponse httpServletResponse;

    @PostMapping(GENERATE_SHORT_LINK_PATH)
    public ResponseEntity<?> generateShortLink(@RequestBody UrlDto urlDto) {
        return ResponseEntity.ok(snipUrlService.generateShortLink(urlDto));
    }

    @GetMapping(REDIRECT_SHORT_LINK_PATH)
    public ResponseEntity<?> redirectToOriginalUrl(@PathVariable String shortLink) throws IOException {
        if (StringUtils.isEmpty(shortLink)) {
            UrlErrorResponseDto invalidUrl = UrlErrorResponseDto.builder()
                    .error(INVALID_URL)
                    .status(STATUS_CODE_400)
                    .build();
            return new ResponseEntity<>(invalidUrl, HttpStatus.OK);
        }

        Url urlToRedirect = snipUrlService.getEncodedUrl(shortLink);

        if (urlToRedirect == null) {
            UrlErrorResponseDto expiredUrl = UrlErrorResponseDto.builder()
                    .error(URL_DOES_NOT_EXIST)
                    .status(STATUS_CODE_400)
                    .build();
            return new ResponseEntity<>(expiredUrl, HttpStatus.OK);
        }

        if (urlToRedirect.getExpirationDate().isBefore(LocalDateTime.now())) {
            UrlErrorResponseDto expiredUrl = UrlErrorResponseDto.builder()
                    .error(EXPIRED_URL)
                    .status(STATUS_CODE_200)
                    .build();
            //deleting the record from db
            snipUrlService.deleteShortLink(urlToRedirect);
            return new ResponseEntity<>(expiredUrl, HttpStatus.OK);
        }

        httpServletResponse.sendRedirect(urlToRedirect.getOriginalUrl());
        return null;
    }
}
