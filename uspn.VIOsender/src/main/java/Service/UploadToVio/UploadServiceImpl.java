package Service.UploadToVio;

import Service.common.UploadedFile;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.http.HttpEntity;
import org.apache.http.client.fluent.Executor;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;

import java.io.File;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

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

        return Executor.newInstance().execute(Request.Post(uri).body(entity)).returnContent().asString();
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
