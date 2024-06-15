package snipurl.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import snipurl.dto.UrlDto;
import snipurl.entity.Url;
import snipurl.exception.ExpiredURLException;
import snipurl.service.SnipUrlService;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static snipurl.utils.Constants.GENERATE_SHORT_LINK_PATH;
import static snipurl.utils.TestConstants.*;

@WebMvcTest(controllers = SnipUrlController.class)
@ExtendWith(MockitoExtension.class)
class SnipUrlControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    SnipUrlService snipUrlService;

    UrlDto urlDto;
    Url persistedUrl;

    @BeforeEach
    void setUp() {
        urlDto = new UrlDto();
        urlDto.setUrl(TEST_URL);

        persistedUrl = new Url();
        persistedUrl.setId(1);
        persistedUrl.setOriginalUrl(TEST_URL);
        persistedUrl.setCreationDate(LocalDateTime.now().minusMinutes(50));
        persistedUrl.setExpirationDate(persistedUrl.getCreationDate().plusDays(1));
        persistedUrl.setShortLink(TEST_SHORT_URL);
    }

    @Test
    void generateShortLinkTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(GENERATE_SHORT_LINK_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(urlDto))
                )
                .andExpect(status().isCreated());
    }

    @Test
    void redirectToOriginalUrlTest() throws Exception {
        Mockito.when(snipUrlService.getEncodedUrl(TEST_SHORT_URL)).thenReturn(persistedUrl);

        mockMvc.perform(MockMvcRequestBuilders.get(SLASH + TEST_SHORT_URL))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    void redirectToOriginalUrlTestWhileGivenLinkIsExpired() throws Exception {
        persistedUrl.setExpirationDate(LocalDateTime.now().minusMinutes(1));
        Mockito.when(snipUrlService.getEncodedUrl(TEST_SHORT_URL)).thenReturn(persistedUrl);

        mockMvc.perform(MockMvcRequestBuilders.get(SLASH + TEST_SHORT_URL))
                .andExpect(status().isBadRequest())
                .andExpect(result -> Assertions.assertInstanceOf(ExpiredURLException.class, result.getResolvedException()));
    }
}