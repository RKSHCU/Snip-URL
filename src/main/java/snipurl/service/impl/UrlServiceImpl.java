package snipurl.service.impl;

import com.google.common.hash.Hashing;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import snipurl.dto.UrlDto;
import snipurl.entity.Url;
import snipurl.repository.UrlRepository;
import snipurl.service.UrlService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UrlServiceImpl implements UrlService {

    private final UrlRepository urlRepository;

    @Override
    public Url generateShortLink(UrlDto urlDto) {

        Url urlToPersist = null;

        if (StringUtils.isNotEmpty(urlDto.getUrl())) {
            String encodedUrl = encodeUrl(urlDto.getUrl());

            urlToPersist = new Url();
            urlToPersist.setCreationDate(LocalDateTime.now());
            urlToPersist.setOriginalUrl(urlDto.getUrl());
            urlToPersist.setShortLink(encodedUrl);
            urlToPersist.setExpirationDate(getExpirationDate(urlDto.getExpirationDate(), urlToPersist.getCreationDate()));
            Url urlToReturn = persistShortLink(urlToPersist);
        }

        return urlToPersist;
    }

    @Override
    public Url persistShortLink(Url url) {
        return urlRepository.save(url);
    }

    @Override
    public Url getEncodedUrl(String url) {
        Optional<Url> optionalUrlToReturn = urlRepository.findByShortLink(url);
        if (optionalUrlToReturn.isPresent()) {
            return optionalUrlToReturn.get();
        } else {
            return null;
        }
    }

    @Override
    public void deleteShortLink(Url url) {
        urlRepository.delete(url);
    }

    private LocalDateTime getExpirationDate(String expirationDate, LocalDateTime creationDate) {
        if (StringUtils.isBlank(expirationDate)) {
            return creationDate.plusMinutes(30);
        }
        return LocalDateTime.parse(expirationDate);
    }

    private String encodeUrl(String url) {
        /*
         * If multiple users wants to short same link, in that case we have to generate different short links for same url
         * So we are concatenating the current time with the given url
         */
        String encodedUrl = null;
        LocalDateTime currentTime = LocalDateTime.now();
        encodedUrl = Hashing.murmur3_32_fixed()
                .hashString(url.concat(currentTime.toString()), StandardCharsets.UTF_8)
                .toString();
        return encodedUrl;
    }
}