package service;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.File;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

class DocumentServiceImpl implements DocumentService {
    private final String host;
    private final int port;
    private final String context;
    private final HttpClient client;
    private final int threadCount;

    DocumentServiceImpl(String host, int port, String context, int threadCount) {
        this.host = host;
        this.port = port;
        this.context = context;
        this.threadCount = threadCount;
        this.client = HttpClientBuilder.create().build();
    }

    @Override
    public String upload(File file) throws Exception {
        HttpPost post = new HttpPost("http://" + host + ":" + port + "/" + context + "/backend/userFile");
        FileBody fileBody = new FileBody(file, ContentType.DEFAULT_BINARY);

        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setCharset(Charset.forName("UTF-8"));
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        builder.addPart("file", fileBody);
        HttpEntity entity = builder.build();

        post.setEntity(entity);
        HttpResponse response = client.execute(post);

        return IOUtils.toString(response.getEntity().getContent(), "UTF-8");
    }

    @Override
    public List<String> upload(List<File> files) throws Exception {
        List<String> result = new ArrayList<>();
        List<Future<String>> threadResults = new ArrayList<>();
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);

        for (File file : files) {
            threadResults.add(executorService.submit(upload(file)::toString));
        }

        for (Future<String> threadResult : threadResults) {
            result.add(threadResult.get());
        }
        return result;
    }
}
