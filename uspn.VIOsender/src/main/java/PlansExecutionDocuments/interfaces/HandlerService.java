package PlansExecutionDocuments.interfaces;

import Documents.Document;

import java.util.List;

public interface HandlerService {

    /**
     * Запуск обработчика документов
     * */
    void runHandler(List<Document> documentList);

    /**
     * Авторизаация и получение cookies
     * */
    void authorization() throws Exception;

    /**
     * Получение списка входящий документов из формы
     * */
    void getIncomingDocument() throws Exception;

    /**
     * Отражение документа
     * */
    void reflectDocument() throws Exception;

    /**
     * Отзыв документа
     * */
    void rollbackDocument() throws Exception;

    /**
     * Добавление исторической записи об изменении статуса документа
     * */
    void addHistoryRecord(List<Document> documentList) throws Exception;
}
