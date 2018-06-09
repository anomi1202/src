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
    void authorization();

    /**
     * Получение списка входящий документов из формы
     * */
    void getIncomingDocument();

    /**
     * Отражение документа
     * */
    void reflectDocument();

    /**
     * Отзыв документа
     * */
    void rollbackDocument();

    /**
     * Добавление исторической записи об изменении статуса документа
     * */
    void addHistoryRecord(List<Document> documentList);
}
