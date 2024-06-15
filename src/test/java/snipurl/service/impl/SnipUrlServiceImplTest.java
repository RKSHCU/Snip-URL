package snipurl.service.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;
import snipurl.dto.UrlDto;
import snipurl.dto.UrlResponseDto;
import snipurl.entity.Url;
import snipurl.exception.ResourceNotFoundException;
import snipurl.repository.UrlRepository;

import java.time.LocalDateTime;
import java.util.Optional;

import static snipurl.utils.TestConstants.*;

class SnipUrlServiceImplTest {

    @InjectMocks
    private SnipUrlServiceImpl snipUrlService;

    @Mock
    private UrlRepository urlRepository;

    Url url;
    UrlDto urlDto;
    Url persistedUrl;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        url = new Url();
        url.setOriginalUrl(TEST_URL);
        url.setShortLink(TEST_SHORT_URL);
        url.setCreationDate(LocalDateTime.now());

        persistedUrl = new Url();
        persistedUrl.setId(1);
        persistedUrl.setOriginalUrl(url.getOriginalUrl());
        persistedUrl.setCreationDate(url.getCreationDate());
        persistedUrl.setExpirationDate(url.getExpirationDate());
        persistedUrl.setShortLink(url.getShortLink());
        persistedUrl.setId(1);

        urlDto = new UrlDto();
        urlDto.setUrl(TEST_URL);
    }

    @Test
    void generateShortLinkTest() {

        ReflectionTestUtils.setField(snipUrlService, APPLICATION_URL, APPLICATION_URL_VALUE);
        UrlResponseDto urlResponseDto = snipUrlService.generateShortLink(urlDto);

        Assertions.assertEquals(urlResponseDto.getOriginalUrl(), TEST_URL);
        Assertions.assertNotNull(urlResponseDto.getShortLink());
    }

    @Test
    void generateShortLinkWithExpirationDateTest() {
        urlDto.setExpirationDate(LocalDateTime.now().plusDays(1).toString());

        ReflectionTestUtils.setField(snipUrlService, APPLICATION_URL, APPLICATION_URL_VALUE);
        UrlResponseDto urlResponseDto = snipUrlService.generateShortLink(urlDto);

        Assertions.assertEquals(urlResponseDto.getOriginalUrl(), TEST_URL);
        Assertions.assertNotNull(urlResponseDto.getShortLink());
    }

    @Test
    void persistShortLinkTest() {
        Mockito.when(urlRepository.save(url)).thenReturn(persistedUrl);
        Url savedUrl = snipUrlService.persistShortLink(url);

        Mockito.verify(urlRepository, Mockito.times(1)).save(url);
    }

    @Test
    void getEncodedUrlTest() {

        Mockito.when(urlRepository.findByShortLink(TEST_SHORT_URL)).thenReturn(Optional.of(persistedUrl));

        Url encodedUrl = snipUrlService.getEncodedUrl(TEST_SHORT_URL);

        Assertions.assertEquals(encodedUrl.getShortLink(), TEST_SHORT_URL);
        Mockito.verify(urlRepository, Mockito.times(1)).findByShortLink(TEST_SHORT_URL);
    }

    @Test
    void getEncodedUrlTestWhileThrowingException() {

        Mockito.when(urlRepository.findByShortLink(TEST_SHORT_URL)).thenReturn(Optional.ofNullable(null));

        Assertions.assertThrows(ResourceNotFoundException.class, () -> snipUrlService.getEncodedUrl(TEST_SHORT_URL));
    }

    @Test
    void deleteShortLinkTest() {
        snipUrlService.deleteShortLink(url);
        Mockito.verify(urlRepository, Mockito.times(1)).delete(url);
    }
}