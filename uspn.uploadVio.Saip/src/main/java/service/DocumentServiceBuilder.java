package service;

public class DocumentServiceBuilder {
    private static final String DEFAULT_CONTEXT = "vio-emulator";
    private static final int DEFAULT_THREAD_COUNT = 10;

    private String host;
    private int port;
    private String context;
    private int threadCount;

    private DocumentServiceBuilder() {
        this.context = DEFAULT_CONTEXT;
        this.threadCount = DEFAULT_THREAD_COUNT;
    }

    public static DocumentServiceBuilder create() {
        return new DocumentServiceBuilder();
    }

    public DocumentServiceBuilder host(String host) {
        this.host = host;
        return this;
    }

    public DocumentServiceBuilder port(int port) {
        this.port = port;
        return this;
    }

    public DocumentServiceBuilder context(String context) {
        this.context = context;
        return this;
    }

    public DocumentService build() {
        if (host == null) return null;
        if (port < 1 || port > 65535) return null;
        return new DocumentServiceImpl(host, port, context, threadCount);
    }
}
