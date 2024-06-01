package snipurl.utils;

public class Constants {
    // Controller Path Constants
    public static final String GENERATE_SHORT_LINK_PATH = "api/v1/generateShortLink";
    public static final String REDIRECT_SHORT_LINK_PATH = "/{shortLink}";

    // Error Constants
    public static final String INVALID_URL = "Invalid URL";
    public static final String URL_DOES_NOT_EXIST = "URL does not exist or it might have expired!";
    public static final String EXPIRED_URL = "URL Expired. Please generate a fresh one.!";
    public static final String STATUS_CODE_400 = "400";
    public static final String STATUS_CODE_200 = "200";

    //Environment Variables
    public static final String APPLICATION_URL = "${application.url}";

    private Constants() {
    }
}
