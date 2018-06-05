import SenderService.SenderServiceBuilder;
import USPN_WEB.DocHandlerServiceBuilder;
import USPN_WEB.documentHandlers.interfaces.AuthorizationService;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

public class Main {
    private static SenderServiceBuilder sender;
    private static DocHandlerServiceBuilder docHandlerService;

    public static void main(String[] args) throws Exception {

        docHandlerService = DocHandlerServiceBuilder.create();
        docHandlerService.getIncomingDocList("01.06.2018").forEach(doc -> System.out.println(doc.getDatechange()));

        docHandlerService.getDisposalList("01.05.2018").forEach((disp -> System.out.println(disp.getId())));
    }

}
