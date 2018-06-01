package Service.SendToUspn;

import Enums.DocumentType;

import java.io.File;
import java.util.List;
import java.util.Map;

public interface SendService {
    /**
     * Отправка файла из ВИО-эмулятора в приложение УСПН
     *
     * @param file отправляемый файл
     * @return идентификатор отправленного файла
     */
    String sendToUspn(File file, DocumentType type) throws Exception;

    /**
     * Загрузка файлов из ВИО-эмулятора в приложение УСПН
     *
     * @param files отправляемые файлы
     * @return идентификаторы отправленных файлов
     */
    List<String> sendToUspn(Map<File, DocumentType> files) throws Exception;
}
