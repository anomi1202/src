package Services.SenderService.SendToUspn.interfaces;

import Documents.Enums.DocumentType;

import java.util.Map;

public interface SendService {
    /**
     * Загрузка файлов из ВИО-эмулятора в приложение УСПН
     *
     * @param fileId отправляемый файл документа
     * @param type тип отправляемого документа
     * @param uppId отправляемый файл УПП
     * @return ответ сервера об статусе отправки
     */
    String send(String fileId, DocumentType type, String uppId) throws Exception;
    /**
     * Загрузка файлов из ВИО-эмулятора в приложение УСПН
     *
     * @param files словарь отправляемых файлов с парама ID_загруженного_файлa-тип_документа
     * @param uppId отправляемый файл УПП
     * @return словать с ответами сервера об статусе отправки (пары файл-статус)
     */
    Map<String, String> send(Map<String, DocumentType> files, String uppId) throws Exception;
}
