import SenderService.VIOsender;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

public class Main {

    public static void main(String[] args) {

        VIOsender sender = VIOsender.newInstance()
                .documentToSend(Paths.get("src/main/resources/04-RNPF-S.zip"), 4)
                .upp(Paths.get("src/main/resources/upp-file_file_ZOONP_4.xml.gz"));
        sender.send();


        HashMap<Path, Integer> map = new HashMap<>();
        map.put(Paths.get("src/main/resources/04-RNPF-S.zip"), 4);
        map.put(Paths.get("src/main/resources/04-RNPF-S_1.zip"), 4);
        sender = VIOsender.newInstance()
                .documentsToSend(map)
                .upp(Paths.get("src/main/resources/upp-file_file_ZOONP_4.xml.gz"));
        sender.send();
    }

}
