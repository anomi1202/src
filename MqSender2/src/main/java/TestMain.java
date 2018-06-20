import handlers.xml.XmlHandler;
import handlers.xml.interfaces.IXMLHandler;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TestMain {

    public static void main(String[] args) throws IOException, XMLStreamException {
        Path filePath = Paths.get("D:/!Projects/!USPN/Stress_test/saip/autotestFolder/RNPF-S_00003_sk_prf.XML");
        IXMLHandler xmlHandler = new XmlHandler(filePath);

        String param = null;
        try {
            param = xmlHandler.getParam("Реквизиты::Номер1");
        } catch (IllegalArgumentException e){}
        System.out.println(param);

        try {
            param = xmlHandler.getParam("НПФ1::id");
        } catch (IllegalArgumentException e){}
        System.out.println(param);

    }
}
