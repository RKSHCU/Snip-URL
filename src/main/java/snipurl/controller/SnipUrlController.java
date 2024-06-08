package snipurl.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import snipurl.dto.UrlDto;
import snipurl.entity.Url;
import snipurl.exception.ExpiredURLException;
import snipurl.service.SnipUrlService;

import java.io.IOException;
import java.time.LocalDateTime;

import static snipurl.utils.Constants.*;

@RestController
@RequiredArgsConstructor
public class SnipUrlController {

    private final SnipUrlService snipUrlService;
    private final HttpServletResponse httpServletResponse;

    /**
     * Generates a short link for the provided URL.
     * This method accepts an incoming request containing details of a URL and generates a short link for that URL.
     *
     * @param urlDto contains the details of url to create short-link of that url
     * @return ResponseEntity of {code UrlDto}
     */
    @PostMapping(GENERATE_SHORT_LINK_PATH)
    public ResponseEntity<?> generateShortLink(@RequestBody UrlDto urlDto) {
        return ResponseEntity.ok(snipUrlService.generateShortLink(urlDto));
    }

    /**
     * Redirects to the original URL corresponding to the provided short link.
     *
     * @param shortLink contains the short url
     * @throws IOException         If an I/O exception occurs while redirecting.
     * @throws ExpiredURLException If the URL associated with the short link has expired.
     */
    @GetMapping(REDIRECT_SHORT_LINK_PATH)
    public void redirectToOriginalUrl(@PathVariable String shortLink) throws IOException {

        Url urlToRedirect = snipUrlService.getEncodedUrl(shortLink);

        if (urlToRedirect.getExpirationDate().isBefore(LocalDateTime.now())) {
            //deleting the record from db
            snipUrlService.deleteShortLink(urlToRedirect);

            //Throwing Exception for expired Url
            throw new ExpiredURLException(EXPIRED_URL);
        }

        httpServletResponse.sendRedirect(urlToRedirect.getOriginalUrl());
    }

}
