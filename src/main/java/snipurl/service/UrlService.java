package snipurl.service;

import snipurl.dto.UrlDto;
import snipurl.entity.Url;

public interface UrlService {
    Url generateShortLink(UrlDto urlDto);

    Url persistShortLink(Url url);

    Url getEncodedUrl(String url);

    void deleteShortLink(Url url);
}