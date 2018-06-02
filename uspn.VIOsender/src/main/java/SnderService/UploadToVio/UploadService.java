package SnderService.UploadToVio;

import java.io.File;
import java.util.List;
import java.util.Map;

public interface UploadService {
    /**
     * Загрузка файла в ВИО-эмулятор
     *
     * @param file загружаемый файл
     * @return идентификатор загруженного файла
     */
    String upload(File file) throws Exception;

    /**
     * Загрузка файлов в ВИО-эмулятор
     *
     * @param files загружаемые файлы
     * @return идентификаторы загруженных файлов
     */
    Map<File, String> upload(List<File> files) throws Exception;
}
