package uspn.test.service;

import org.slf4j.Logger;
import uspn.test.Enum.EnumServer;
import uspn.test.data.VioDocTypes;

import java.io.File;

import static java.lang.Thread.sleep;
import static org.slf4j.LoggerFactory.getLogger;
import static uspn.test.Enum.EnumServer.*;
import static uspn.test.service.MqSenderService.connectServiceVio;


/**
 * Created by maksim.zotov on 31.01.2017.
 */
public class USPNInboxDocumentsChecker {
    private static final Logger LOGGER = getLogger(USPNInboxDocumentsChecker.class);
    // Путь к папке с документами
    private static final String folder = "D:\\!Projects\\!USPN\\04_07-SUD\\regress\\7\\07_00001_upload_xml_to_DB";

    public static void main(String[] args) throws Exception {
        startImitationVio(USPNDEV);
    }
     /**
     * Метод интеграции с ВИО
     */
    public static void startImitationVio(EnumServer server) throws Exception {
        LOGGER.info("Запуск имитации ВИО");
        MqSenderService service = MqSenderService.getInstance(connectServiceVio(server));
        File dir = new File(folder);
        File uppFile = new File("D:\\!Projects\\!USPN\\04_07-SUD\\regress\\7\\upp-file_file_ZOONP_4.xml.gz");
        for (File file : dir.listFiles()) {
            if (file.getName().contains(".zip")) {
                LOGGER.info("---- Отправка через ВИО файла: {} ----", file.getName());
                service.sendContract(uppFile, file, VioDocTypes.V_UVED_COURT, null);
            }
        }
        sleep(3600);
    }


}
