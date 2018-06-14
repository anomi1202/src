package Services.USPNServices;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractHandlerService {
    private final Logger logger = LoggerFactory.getLogger(AbstractHandlerService.class);
    private final String USPN_PROPERTIES = "USPN.properties";
    protected final String DEFAULT_CONTEXT = "uspn/forms";
    public final ConcurrentHashMap<String, List<Cookie>> cookieStore = new ConcurrentHashMap<>();

    protected CookieJar initCookieJar(){
        CookieJar cookieJar = new CookieJar() {
            @Override
            public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                cookies.forEach(cookie -> logger.trace("Save cookie:" + cookie.toString()));
                cookieStore.put(url.host(), cookies);
            }

            @Override
            public List<Cookie> loadForRequest(HttpUrl url) {
                List<Cookie> cookies = cookieStore.get(url.host());
                if (cookies != null) {
                    cookies.forEach(cookie -> logger.trace("Load cookie:" + cookie.toString()));
                }
                return cookies != null ? cookies : new ArrayList<>();
            }
        };

        return cookieJar;
    }

    protected String getUri() {
        String uri = null;
        Properties prop = new Properties();

        try (InputStream is = Files.newInputStream(Paths.get(USPN_PROPERTIES))) {
            prop.load(is);

            String host = prop.getProperty("uspn.host");
            int port = Integer.parseInt(prop.getProperty("uspn.port", "8080"));
            String context = prop.getProperty("uspn.context");
            context = context == null ? DEFAULT_CONTEXT : context;

            logger.info(String.format("Read properties file: %s" +
                            "\r\n\thost: %s" +
                            "\r\n\tport: %s" +
                            "\r\n\tcontext: %s"
                    , USPN_PROPERTIES, host, port, context));

            if (host != null
                    && port > 0 && port < 65536) {
                uri = "http://" + host + ":" + port + "/" + context + "/";
                logger.info(String.format("Generate URL link to webApp: %s", uri));
            }
        } catch (IOException | NumberFormatException e) {
            logger.error("FAILED", e);
        }

        return uri;
    }
}
