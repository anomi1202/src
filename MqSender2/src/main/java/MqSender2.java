import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.nio.ch.IOUtil;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.XMLEvent;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Properties;
import java.util.regex.Pattern;

public class MqSender2 {
    private enum SenderDocType {JSON, SOAP};
    private Logger logger = LoggerFactory.getLogger(MqSender2.class);

    @Parameter(names = {"JSON", "json", "-j"}, description = "Path to JSON file")
    private String PATH_FILE_JSON;

    @Parameter(names = {"SOAP", "soap", "-s"}, description = "Path to SOAP file")
    private String PATH_FILE_SOAP;

    public static void main(String[] args) throws Exception {
        MqSender2 mqSender2 = new MqSender2();
        JCommander jCommander = new JCommander(mqSender2);
        try {
            jCommander.parse(args);
            mqSender2.setParamJSONDocToSOAP(1, "42ae3d2a70f24cf7a92130e71d793ca8");
//            mqSender2.run();
        } catch (ParameterException e) {
            jCommander.usage();
        }
    }

    public void run() {
        try {
            if (PATH_FILE_JSON != null) {
                logger.info(String.format("Send JSON file: %s", PATH_FILE_JSON));
                initPropForMQSender(SenderDocType.JSON);
                send(SenderDocType.JSON);

                if (PATH_FILE_SOAP != null) {
                    initPropForMQSender(SenderDocType.SOAP);
                    int docID = askUserAboutID();
                    String requestID = getRequestIdFromJSON();
                    logger.info(String.format("Entered ID of document: %d", docID));

                    setParamJSONDocToSOAP(docID, requestID);
                    send(SenderDocType.SOAP);
                    logger.info(String.format("Send SOAP file: %s\r\n\twith DocumentRef id: %d\r\n\twith RequestId: %s.", PATH_FILE_SOAP, docID, requestID));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int askUserAboutID() throws IOException {
        String docID = "";
        System.out.print("Enter the ID of document in EHD: ");
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            docID = reader.readLine();
        } catch (NumberFormatException e){
            logger.error(String.format("Incorrect ID: '%s'", docID));
            throw new NumberFormatException(e.getMessage());
        }

        return Integer.parseInt(docID);
    }

    private String getRequestIdFromJSON() throws IOException {
        String requestId;
        try (BufferedReader fileReader = Files.newBufferedReader(Paths.get(PATH_FILE_JSON))){
            Gson jsonDoc = new GsonBuilder().create();
            JsonObject jsonObject = jsonDoc.fromJson(fileReader, JsonElement.class).getAsJsonObject();
            requestId = jsonObject.get("requestId").getAsString();
        } catch (IOException e) {
            logger.error("FAILDE", e);
            throw new IOException(e.getMessage());
        }

        return requestId;
    }

    private void setParamJSONDocToSOAP(int documentRefId, String requestID) throws Exception {
        String newRequestID = String.format("<ns1:RequestId>%saaaaaaaaaaaaaaaaaaaaaaaaa</ns1:RequestId>", requestID);
        String newDocumentRefId = String.format("DocumentRef id=\"%daaaaaaaaaaaaaaaaaaaa\"", documentRefId);

        Pattern patternRequestID = Pattern.compile("\t\t<n1:DocumentRef id=\"[0-9]+\" contentNumber=\"1\"/>");
        Pattern patternDocumentRefId = Pattern.compile("\t\t<ns1:RequestId>[0-9 A-Z a-z]+</ns1:RequestId>");

        ArrayList<String> xmlLines = (ArrayList<String>) Files.readAllLines(Paths.get(PATH_FILE_SOAP));
        xmlLines.forEach((value) -> {
            if (patternDocumentRefId.matcher(value).matches()){
                value += newDocumentRefId;
            } else if (patternRequestID.matcher(value).matches()){
                value += newRequestID;
            }
        });

        System.out.println(xmlLines);
    }

    private void setParamJSONDocToSOAP() throws Exception {
        try (InputStream is = Files.newInputStream(Paths.get(PATH_FILE_SOAP))){
            XMLStreamReader xmlReader = XMLInputFactory.newInstance().createXMLStreamReader(is);
            while (xmlReader.hasNext()){
                int event = xmlReader.next();
                if (event == XMLEvent.START_ELEMENT) {
                    String tagName = xmlReader.getLocalName();
                    String valueText = "";
                    if (tagName.equals("RequestId")) {
                        valueText = xmlReader.getElementText();
                    } else if (tagName.equals("DocumentRef")){
                        valueText = xmlReader.getAttributeValue(null, "id");
                    }
                    System.out.println(tagName + " - " + valueText);
                }
            }
        }
    }

    private void send(SenderDocType type) {
        switch (type){
            case JSON:
                new MQSender().run(new String[]{PATH_FILE_JSON});
                break;
            case SOAP:
                new MQSender().run(new String[]{PATH_FILE_SOAP});
                break;
            default:
                break;
        }
    }

    private void initPropForMQSender(SenderDocType type) throws Exception {
        String propFileNameMQSender = "MQSender.properties";
        String propFileNameMQSender2 = "MQSender2.properties";
        Properties propFile = new Properties();

        try (InputStream is = Files.newInputStream(Paths.get(propFileNameMQSender2))){
            propFile.load(is);
        } catch (IOException e){
            logger.error("FAILED", e);
            throw new IOException(e.getMessage());
        }

        try (OutputStream os = Files.newOutputStream(Paths.get(propFileNameMQSender))){
            String propDestination = "";
            switch (type) {
                case JSON:
                    propDestination = propFile.getProperty("mq.destinationJSON");
                    break;
                case SOAP:
                    propDestination = propFile.getProperty("mq.destinationSOAP");
                    break;
                default:
                    break;
            }

            propFile.remove("mq.destinationSOAP");
            propFile.remove("mq.destinationJSON");
            propFile.setProperty("mq.destination", propDestination);
            propFile.store(os, "Set properties 'mq.destination': " + propDestination);
        } catch (IOException| NullPointerException ex){
            logger.error("FAILED", ex);
            throw (ex instanceof IOException) ? new IOException(ex): new NullPointerException(ex.getMessage());
        }

    }
}
