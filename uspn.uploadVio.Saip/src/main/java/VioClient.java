import service.DocumentServiceBuilder;

import java.nio.file.Paths;

public class VioClient {

    public static void main(String[] args) throws Exception {
        DocumentServiceBuilder vioService  = DocumentServiceBuilder.create();
        vioService
                .host("172.22.116.68")
                .port(9080)
                .context("vio-emulator");

        System.out.println(vioService.build().upload(Paths.get("src/main/resources/04-RNPF-S.zip").toFile()));
    }
}
