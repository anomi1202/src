package Service.SendToUspn;

import Enums.DocumentType;
import Service.UploadToVio.UploadService;
import com.google.gson.GsonBuilder;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.fluent.Executor;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

public class SendServiceImpl implements SendService {
    private final String uri;
    private final int threadCount;
    private final UploadService uploadService;

    public SendServiceImpl(String uri, int threadCount, UploadService uploadService) {
        this.uri = uri + "/backend/process/sendNpfDocument";
        this.threadCount = threadCount;
        this.uploadService = uploadService;
    }

    @Override
    public String sendToUspn(File file, DocumentType type) throws Exception {
//        String uploadedFileID = uploadService.upload(file);
        String uploadedFileID = "1482";
//        FileBody fileBody = new FileBody(json, ContentType.APPLICATION_JSON);
//        HttpEntity entity = MultipartEntityBuilder.create()
//                .setCharset(Charset.forName("UTF-8"))
//                .setMode(HttpMultipartMode.BROWSER_COMPATIBLE)
//                .addPart("file", fileBody)
//                .build();

        String json = String.format("{\"uppFileId\":%d,\"documentFileId\":\"%s\",\"documentType\":\"%s\"}", 1444, uploadedFileID, type.name());
        Request post = Request.Post(uri)
                .addHeader("Content-type", "application/json")
                .bodyByteArray(json.getBytes(), ContentType.APPLICATION_JSON);

        return Executor.newInstance().execute(post).returnContent().asString();
    }

    @Override
    public List<String> sendToUspn(Map<File, DocumentType> files) throws Exception {
        List<String> uploadedFilesID = uploadService.upload(new ArrayList<>(files.keySet()));

        List<String> result = new ArrayList<>();
        List<Future<String>> threadResults = new ArrayList<>();
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);

        for (Map.Entry<File, DocumentType> pair  : files.entrySet()) {
            File file = pair.getKey();
            DocumentType docType = pair.getValue();

            threadResults.add(executorService.submit(sendToUspn(file, docType)::toString));
        }

        for (Future<String> threadResult : threadResults) {
            result.add(threadResult.get());
        }
        return result;
    }

}
