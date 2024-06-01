package snipurl.utils;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.context.annotation.Import;
import snipurl.dto.UrlDto;
import snipurl.dto.UrlResponseDto;
import snipurl.entity.Url;

import java.time.LocalDateTime;

@Mapper
@Import({LocalDateTime.class})
public interface SnipUrlMapper {
    SnipUrlMapper MAPPER = Mappers.getMapper(SnipUrlMapper.class);

    @Mapping(target = "creationDate", expression = "java(LocalDateTime.now())")
    @Mapping(target = "originalUrl", source = "urlDto.url")
    @Mapping(target = "shortLink", source = "encodedUrl")
    Url mapToUrl(UrlDto urlDto, String encodedUrl);

    @Mapping(target = "originalUrl", source = "url.originalUrl")
    @Mapping(target = "shortLink", expression = "java(applicationUrl.concat(url.getShortLink()))")
    @Mapping(target = "expirationDate", source = "url.expirationDate")
    UrlResponseDto mapToUrlResponseDto(Url url, String applicationUrl);
}
