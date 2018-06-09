package Services.USPNServices.documentHandlers;

import Documents.Enums.DisposalStatus;
import Documents.forJson.disposal.DisposalDocument;
import Services.USPNServices.documentHandlers.disposal.AbstractIDisposalService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import okhttp3.OkHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit2.Response;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

public class DisposalService extends AbstractIDisposalService {
    private Logger logger = LoggerFactory.getLogger(AbstractIDisposalService.class);

    public DisposalService(String uri, OkHttpClient client) {
        super(uri, client);
    }

    /**
     * Метод для работы с распоряжениями
     * Получение списка документов с формы входящих документов
     * @param createDateRangeStart - Дата создания распоряжения
     *                             отбираются распоряжения дата создания которых >= указанной даты
     * */
    public List<DisposalDocument> getDisposalsList(String createDateRangeStart){
        List<DisposalDocument> disposalList = null;
        try {
            Response<List<DisposalDocument>> disposalListResponse = super.getDisposalList(createDateRangeStart).execute();
            disposalList = disposalListResponse.body();
        } catch (Exception e) {
            logger.error("FAILED", e);
        }
        return disposalList;
    }

    /**
     * Метод для работы с распоряжениями
     * Потдветрждение распоряжения
     * @param disposalList - список распоряжений со статусоп подтрвеждения
     *                     "" - OK
     *                     иначе текст ошибки
     * */
    public Map<DisposalDocument, Boolean> confirmDisposal(List<DisposalDocument> disposalList){

        Map<DisposalDocument, Boolean> requestResultMap = disposalList.stream()
                .filter(disp -> disp.getStatusENG().equals(DisposalStatus.PREPARED.name()))
                .collect(Collectors.toMap(
                        disp -> disp,
                        disp -> {
                            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.mm.yyyy");
                            Response<String> stringResponse = null;
                            try {
                                Date disposalDate = dateFormat.parse(disp.getDate());
                                disposalDate.setTime(disposalDate.getTime() - (long) 86400000); //minus 1 day
                                String date = dateFormat.format(disposalDate);

                                stringResponse = super.confirmDisposal(disp.getId(), disp.getNumber(), date).execute();
                            } catch (Exception e) {
                                logger.error("FAILED", e);
                            }

                            if(stringResponse.equals("")){
                                return true;
                            } else {
                                return false;
                            }
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
            Boolean body = super.reflectDisposal(json)
                    .execute().body();
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
            Boolean body = super.rollbackDisposal(json)
                    .execute().body();
            bodyResponse = body != null ? body : false;
        } catch (Exception e) {
            logger.error("FAILED", e);
        }

        return bodyResponse;
    }

}
