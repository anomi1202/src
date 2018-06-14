package PlansExecutionDocuments.interfaces;

import Documents.Document;
import Documents.Enums.IncomingDocumentStatus;

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
     * Отражение документа
     * */
    boolean reflectDocument() throws Exception;

    /**
     * Отзыв документа
     * */
    boolean rollbackDocument() throws Exception;

    /**
     * Проверка статуса документа
     * */
    void waiteStatusOfDocument(IncomingDocumentStatus status) throws Exception;
}
