package snipurl.utils;

public class Constants {
    // Controller Path Constants
    public static final String GENERATE_SHORT_LINK_PATH = "/api/v1/generateShortLink";
    public static final String REDIRECT_SHORT_LINK_PATH = "/{shortLink}";

    // Error Constants
    public static final String EXPIRED_URL = "URL Expired. Please generate a fresh one.!";
    public static final String STATUS_CODE_400 = "400";
    public static final String STATUS_CODE_404 = "404";
    public static final String NOT_FOUND_ERROR_MESSAGE = "No %s found for value %s";

    // Error Codes
    public static final String NOT_FOUND = "NOT_FOUND";
    public static final String INVALID_INPUT = "INVALID_INPUT";

    // Environment Variables
    public static final String APPLICATION_URL = "${application.url}";

    // Other Constants
    public static final String SHORT_LINK = "Short-Link";

    private Constants() {
    }
}
