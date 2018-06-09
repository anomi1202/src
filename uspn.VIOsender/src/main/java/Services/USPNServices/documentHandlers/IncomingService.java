package Services.USPNServices.documentHandlers;

import Documents.Enums.IncomingDocType;
import Documents.Enums.IncomingDocumentStatus;
import Documents.forJson.incoming.IncomingDocument;
import Services.USPNServices.documentHandlers.incoming.AbstractIncomingService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import okhttp3.OkHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit2.Response;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

public class IncomingService extends AbstractIncomingService {
    private final Logger logger = LoggerFactory.getLogger(IncomingService.class);

    public IncomingService(String uri, OkHttpClient client) {
        super(uri, client);
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
            Response<List<IncomingDocument>> v_documents = super.getIncomingDocList(createDateRangeStart, "V_DOCUMENTS").execute();
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
            Boolean body = super.checkOnBaseControl(json).execute().body();
            bodyResponse = body != null ? body : false;
        } catch (Exception e) {
            logger.error("FAILED", e);
        }

        return bodyResponse;
    }

    /**
     * Метод для работы с входящими документами
     * Создание распоряжения на основе входящего документа (реестра)
     *
     * @param baseDocumentNumber дата создания распоряжения - baseDocumentNumber=суды-06-2018
     * @param baseDocumentDate дата распоряжения - baseDocumentDate=2018-06-07
     * @param incomingDocuments список входящих документов - из низ составляется запрос, формата documentIds=9140,9141,9142
     * */
    public boolean prepareAndCreateDisposal(String baseDocumentNumber, String baseDocumentDate, List<IncomingDocument> incomingDocuments){
        boolean bodyResponse = false;
        String documentIds = incomingDocuments.stream()
                .filter(doc -> doc.getType().getTypeName().equals(IncomingDocType.V_REGISTERS.name()))
                .map(doc -> String.valueOf(doc.getId()))
                .collect(Collectors.joining(","));
        try {
            Response<String> v_documents = super.createDisposal(baseDocumentNumber, baseDocumentDate, documentIds).execute();
            if (v_documents.body().equals("")){
                bodyResponse = true;
            } else {
                throw new Exception(String.format("Disposal with number %s and date %s is already exists!", baseDocumentNumber, baseDocumentDate));
            }
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
                .filter(doc ->
                        doc.getType().getTypeName().equals(IncomingDocType.V_SVEDENIA.name())
                                && doc.getStatus().getNameUI().equals(IncomingDocumentStatus.SAVE)
                )
                .flatMapToLong(doc -> LongStream.of(doc.getId()))
                .boxed().collect(Collectors.toList());
        JsonObject json = new JsonObject();
        json.add("documentIds", new Gson().toJsonTree(collectID));

        try {
            Boolean body = super.reflect(json).execute().body();
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
                .filter(doc -> doc.getStatus().getNameUI().equals(IncomingDocumentStatus.REFLECTED))
                .flatMapToLong(doc -> LongStream.of(doc.getId()))
                .boxed().collect(Collectors.toList());
        JsonObject json = new JsonObject();
        json.add("documentIds", new Gson().toJsonTree(collectID));

        try {
            Boolean body = super.rollback(json).execute().body();
            bodyResponse = body != null ? body : false;
        } catch (Exception e) {
            logger.error("FAILED", e);
        }

        return bodyResponse;
    }

}
