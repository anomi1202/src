package USPN_WEB;

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
    protected String host;
    protected int port;
    protected String context;
    public final ConcurrentHashMap<String, List<Cookie>> cookieStore = new ConcurrentHashMap<>();

    protected CookieJar initCookieJar(){
        CookieJar cookieJar = new CookieJar() {
            @Override
            public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                cookies.forEach(cookie -> logger.info("Save cookie:" + cookie.toString()));
                cookieStore.put(url.host(), cookies);
            }

            @Override
            public List<Cookie> loadForRequest(HttpUrl url) {
                List<Cookie> cookies = cookieStore.get(url.host());
                if (cookies != null) {
                    cookies.forEach(cookie -> logger.info("Load cookie:" + cookie.toString()));
                }
                return cookies != null ? cookies : new ArrayList<>();
            }
        };

        return cookieJar;
    }

    protected void initProp() {
        Properties prop = new Properties();
        try (InputStream is = Files.newInputStream(Paths.get(USPN_PROPERTIES))) {
            prop.load(is);

            host = prop.getProperty("uspn.host");
            port = Integer.parseInt(prop.getProperty("uspn.port", "8080"));
            context = prop.getProperty("uspn.context");

            logger.info(String.format("Read properties file: %s" +
                            "\r\n\thost: %s" +
                            "\r\n\tport: %s" +
                            "\r\n\tcontext: %s"
                    , USPN_PROPERTIES, host, port, context));
        } catch (IOException | NumberFormatException e) {
            logger.error("FAILED", e);
        }
    }
}
