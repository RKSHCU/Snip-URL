package snipurl.service;

import snipurl.dto.UrlDto;
import snipurl.dto.UrlResponseDto;
import snipurl.entity.Url;

public interface SnipUrlService {
    UrlResponseDto generateShortLink(UrlDto urlDto);

    Url persistShortLink(Url url);

    Url getEncodedUrl(String url);

    void deleteShortLink(Url url);
}