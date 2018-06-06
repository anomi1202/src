package USPN_WEB;

import Documents.disposal.DisposalDocument;
import Documents.incoming.IncomingDocument;
import Documents.payment.Payment;
import Enums.DisposalStatus;
import Enums.IncomingDocType;
import USPN_WEB.documentHandlers.AuthorizationServiceImpl;
import USPN_WEB.documentHandlers.DisposalHandlerServiceImpl;
import USPN_WEB.documentHandlers.IncomingDocHandlerServiceImpl;
import USPN_WEB.documentHandlers.PaymentHandlerServiceImpl;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.sun.org.apache.xpath.internal.operations.Bool;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit2.Call;
import retrofit2.Response;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
import java.util.stream.Stream;


public class DocHandlerServiceBuilder extends AbstractHandlerService{
    private final Logger logger = LoggerFactory.getLogger(DocHandlerServiceBuilder.class);

    private IncomingDocHandlerServiceImpl inDocService;
    private DisposalHandlerServiceImpl disposalService;
    private PaymentHandlerServiceImpl paymentService;
    private AuthorizationServiceImpl authService;
    private OkHttpClient client;

    private DocHandlerServiceBuilder() {
        this.context = DEFAULT_CONTEXT;
        initProp();
    }

    public static DocHandlerServiceBuilder create() {
        return new DocHandlerServiceBuilder().build();
    }

    private DocHandlerServiceBuilder build() {
        String uri = null;
        if (host != null
                && port > 0 && port < 65536) {
            uri = "http://" + host + ":" + port + "/" + context + "/";
            logger.info(String.format("Generate URL link to webApp: %s", uri));
        }

        try {
            client = new OkHttpClient.Builder().cookieJar(initCookieJar()).build();

            authService = new AuthorizationServiceImpl(uri, client);
            inDocService = new IncomingDocHandlerServiceImpl(uri, client);
            disposalService = new DisposalHandlerServiceImpl(uri, client);
            paymentService = new PaymentHandlerServiceImpl(uri, client);

            return this;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Метод для авторизации в УСПН
     * Получение cookie's используемых для последующих POST/GET запросах
     * Cookie's охраняются в словаре родительского абстрактного класса AbstractHandlerService
     * */
    public void doAuthorization(){
        try {
            authService.getAuthorizationCookies();
        } catch (Exception e) {
            logger.error("FAILED", e);
        }
    }

    /**
     * Метод для работы с входящими документами
     * Получение списка документов с формы входящих документов
     * @param createDateRangeStart - Дата загрузки документа
     *                             отбираются док-ты дата загрузки которых >= указанной даты
     * */
    public List<IncomingDocument> getIncomingDocList(String createDateRangeStart){
        List<IncomingDocument> incomingDocuments = null;
        try {
            Response<List<IncomingDocument>> v_documents = inDocService.getIncomingDocList(createDateRangeStart, "V_DOCUMENTS").execute();
            incomingDocuments = v_documents.body();
        } catch (Exception e) {
            logger.error("FAILED", e);
        }
        return incomingDocuments;
    }

    /**
     * Метод для работы с входящими документами
     * Постановка входящих документов на проверку БК (на проверку ставятся док-ты с типом РЕЕСТРЫ - V_REGISTERS
     * @param incomingDocList - список документов
     * */
    public boolean checkOnBaseControl(List<IncomingDocument> incomingDocList){
        boolean bodyResponse = false;

        List<Object> collectID = incomingDocList.stream()
                .filter(doc -> doc.getType().getTypeName().equals(IncomingDocType.V_REGISTERS.name()))
                .flatMapToLong(doc -> LongStream.of(doc.getId()))
                .boxed().collect(Collectors.toList());
        JsonObject json = new JsonObject();
        json.add("documentIds", new Gson().toJsonTree(collectID));

        try {
            Boolean body = inDocService.checkOnBaseControl(json).execute().body();
            bodyResponse = body != null ? body : false;
        } catch (Exception e) {
            logger.error("FAILED", e);
        }

        return bodyResponse;
    }

    /**
     * Метод для работы с входящими документами
     * Постановка входящих документов на отраженме (на проверку ставятся док-ты с типом СВЕДЕНИЯ - V_SVEDENIA
     * @param incomingDocList - список документов
     * */
    public boolean reflectDocument(List<IncomingDocument> incomingDocList){
        boolean bodyResponse = false;

        List<Object> collectID = incomingDocList.stream()
                .filter(doc -> doc.getType().getTypeName().equals(IncomingDocType.V_SVEDENIA.name()))
                .flatMapToLong(doc -> LongStream.of(doc.getId()))
                .boxed().collect(Collectors.toList());
        JsonObject json = new JsonObject();
        json.add("documentIds", new Gson().toJsonTree(collectID));

        try {
            Boolean body = inDocService.reflect(json).execute().body();
            bodyResponse = body != null ? body : false;
        } catch (Exception e) {
            logger.error("FAILED", e);
        }

        return bodyResponse;
    }

    /**
     * Метод для работы с входящими документами
     * Постановка входящих документов на отзыв (на проверку ставятся док-ты с типом РСВЕДЕНИЯ - V_SVEDENIA
     * @param incomingDocList - список документов
     * */
    public boolean rollbackDocument(List<IncomingDocument> incomingDocList){
        boolean bodyResponse = false;

        List<Object> collectID = incomingDocList.stream()
                .filter(doc -> doc.getType().getTypeName().equals(IncomingDocType.V_SVEDENIA.name()))
                .flatMapToLong(doc -> LongStream.of(doc.getId()))
                .boxed().collect(Collectors.toList());
        JsonObject json = new JsonObject();
        json.add("documentIds", new Gson().toJsonTree(collectID));

        try {
            Boolean body = inDocService.rollback(json).execute().body();
            bodyResponse = body != null ? body : false;
        } catch (Exception e) {
            logger.error("FAILED", e);
        }

        return bodyResponse;
    }

    /**
     * Метод для работы с распоряжениями
     * Получение списка документов с формы входящих документов
     * @param createDateRangeStart - Дата создания распоряжения
     *                             отбираются распоряжения дата создания которых >= указанной даты
     * */
    public List<DisposalDocument> getDisposalList(String createDateRangeStart){
        List<DisposalDocument> disposalList = null;
        try {
            Response<List<DisposalDocument>> disposalListResponse = disposalService.getDisposalList(createDateRangeStart).execute();
            disposalList = disposalListResponse.body();
        } catch (Exception e) {
            logger.error("FAILED", e);
        }
        return disposalList;
    }

    /**
     * Метод для работы с распоряжениями
     * Потдветрждение распоряжения
     * @param disposalList - список распоряжений
     * */
    public Map<DisposalDocument, String> confirmDisposal(List<DisposalDocument> disposalList){

        Map<DisposalDocument, String> requestResultMap = disposalList.stream()
                .filter(disp -> disp.getStatusENG().equals(DisposalStatus.PREPARED.name()))
                .collect(Collectors.toMap(
                        disp -> disp,
                        disp -> {
                            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.mm.yyyy");
                            String resultBody = null;

                            long id = disp.getId();
                            String number = disp.getNumber();
                            try {
                                Date disposalDate = dateFormat.parse(disp.getDate());
                                disposalDate.setTime(disposalDate.getTime() + (long) 86400000); //add 1 day

                                String date = dateFormat.format(disposalDate);
                                resultBody =  disposalService.confirmDisposal(id, number, date)
                                        .execute().body();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            return resultBody;
                        }
                ));




        return requestResultMap;
    }

    /**
     * Метод для работы с распоряжениями
     * Постановка распоряжения на отраженме
     * @param disposalList - список распоряжений
     * */
    public boolean reflectDisposal(List<DisposalDocument> disposalList){
        boolean bodyResponse = false;

        List<Long> collectID = disposalList.stream()
                .filter(disp -> !disp.getStatusENG().equals(DisposalStatus.CONFIRMED.name()))
                .flatMapToLong(disp -> LongStream.of(disp.getId()))
                .boxed().collect(Collectors.toList());
        JsonObject json = new JsonObject();
        json.add("documentIds", new Gson().toJsonTree(collectID));

        try {
            Boolean body = disposalService.reflectDisposal(json).execute().body();
            bodyResponse = body != null ? body : false;
        } catch (Exception e) {
            logger.error("FAILED", e);
        }

        return bodyResponse;
    }

    /**
     * Метод для работы с распоряжениями
     * Постановка распоряжения на отзыв
     * @param disposalList - список распоряжений
     * */
    public boolean rollbackDisposal(List<DisposalDocument> disposalList){
        boolean bodyResponse = false;

        List<Long> collectID = disposalList.stream()
                .filter(disp -> !disp.getStatusENG().equals(DisposalStatus.PREPARED.name()))
                .flatMapToLong(disp -> LongStream.of(disp.getId()))
                .boxed().collect(Collectors.toList());
        JsonObject json = new JsonObject();
        json.add("documentIds", new Gson().toJsonTree(collectID));

        try {
            Boolean body = disposalService.rollbackDisposal(json).execute().body();
            bodyResponse = body != null ? body : false;
        } catch (Exception e) {
            logger.error("FAILED", e);
        }

        return bodyResponse;
    }

    public Payment createNewPayment(Payment newPayment){
        try {
            Response<Payment> paymentResponse = paymentService.createPayment(newPayment).execute();
            newPayment = paymentResponse.body();
        } catch (Exception e) {
            logger.error("FAILED", e);
        }
        return newPayment;
    }


}
