package PlansExecutionDocuments;

import Documents.Document;
import Documents.Enums.DocumentType;
import Services.SenderService.SenderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class DocumentsSender {
    private final Logger logger = LoggerFactory.getLogger(DocumentsSender.class);
    private final static String USPN_PROPERTIES = "USPN.properties";
    private List<Document> documentsList;
    private Path UPP;
    private Path documentsDir;
    private SenderService senderService;

    public DocumentsSender() {
        this.documentsList = new ArrayList<>();
        initProp();
    }

    public void initSenderService() {
        senderService = SenderService.newInstance();
        Map<File, DocumentType> map = documentsList.stream()
                .collect(Collectors.toMap(
                        doc -> new File(documentsDir + "/" + doc.getArchiveName()),
                        Document::getTypeENG));

        try {
            senderService.upp(new File(documentsDir + "/" + UPP))
                    .documentsToSend(map)
                    .build();
        } catch (Exception e) {
            logger.error("FAILED", e);
            senderService = null;
        }
    }

    public Map<Document, Boolean> sendDocument(){
        //Map of pair file-senderStatus
        Map<File, Boolean> send = senderService.send();
        //Map of pair fileName-senderStatus
        Map<String, Boolean> collect = send.entrySet()
                .stream()
                .collect(Collectors.toMap(
                        pair -> pair.getKey().getName(),
                        Map.Entry::getValue));

        //Return map of pair Document-senderStatus
        return documentsList
                .stream()
                .collect(Collectors.toMap(
                        doc -> doc,
                        doc -> collect.get(doc.getArchiveName())));
    }

    private void initProp(){
        Properties prop = new Properties();

        try(BufferedReader reader = Files.newBufferedReader(Paths.get(USPN_PROPERTIES), Charset.forName("CP1251"))) {
            prop.load(reader);

            documentsDir = Paths.get(prop.getProperty("doc.directory"));
            UPP = Paths.get(prop.getProperty("doc.UPP"));
            prop.stringPropertyNames().stream()
                    .filter(key -> key.startsWith("doc.") && !key.equals("doc.directory") && !key.equals("doc.UPP"))
                    .forEach(key -> {
                        DocumentType docType = DocumentType.valueOf(key.split("doc.")[1]);
                        String[] docArchiveNames = prop.getProperty(key).split(",");
                        Arrays.stream(docArchiveNames)
                                .forEach(archiveName -> documentsList.add(new Document(archiveName.trim(), docType)));
                    });

            logger.info(String.format("Get documents list from %s\r\n\t" +
                            "folder: %s\r\n\t" +
                            "found %d documents"
                    , USPN_PROPERTIES
                    , documentsDir
                    , documentsList.size()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
