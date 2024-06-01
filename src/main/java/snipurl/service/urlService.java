package snipurl.service;

import org.springframework.stereotype.Service;
import snipurl.dto.UrlDto;
import snipurl.entity.Url;

@Service
public interface urlService {
    Url generateShortLink(UrlDto urlDto);

    Url persistShortLink(Url url);

    Url getEncodedUrl(String Url);

    void deleteShortLink(Url url);
}