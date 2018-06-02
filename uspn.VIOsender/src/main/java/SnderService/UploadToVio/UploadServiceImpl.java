package SnderService.UploadToVio;

import org.apache.http.HttpEntity;
import org.apache.http.client.fluent.Executor;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;

import java.io.File;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UploadServiceImpl implements UploadService {
    private final String uri;
    private final int threadCount;

    public UploadServiceImpl(String uri, int threadCount) {
        this.uri = uri + "/backend/userFile";
        this.threadCount = threadCount;
    }

    @Override
    public String upload(File file) throws Exception {
        FileBody fileBody = new FileBody(file, ContentType.DEFAULT_BINARY);
        HttpEntity entity = MultipartEntityBuilder.create()
                .setCharset(Charset.forName("UTF-8"))
                .setMode(HttpMultipartMode.BROWSER_COMPATIBLE)
                .addPart("file", fileBody)
                .build();

        return Executor.newInstance().execute(Request.Post(uri).body(entity)).returnContent().asString().replaceAll("\"", "");
    }

    @Override
    public Map<File, String> upload(List<File> files) throws Exception {
        Map<File, String> resultMap = new HashMap<>();
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);

        for (File file : files) {
            resultMap.put(file, executorService.submit(upload(file)::toString).get().replaceAll("\"", ""));
        }

        executorService.shutdown();
        return resultMap;
    }

}
