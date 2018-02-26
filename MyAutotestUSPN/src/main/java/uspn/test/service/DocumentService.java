package uspn.test.service;

import java.io.File;

public interface DocumentService {
    /**
     * Загрузка файла в ВИО-эмулятор
     *
     * @param file загружаемый файл
     * @return идентификатор загруженного файла
     */
    String upload(File file) throws Exception;
}
