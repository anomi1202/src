package PlansExecutionDocuments;

import Documents.Document;
import Documents.Enums.IncomingDocType;
import Documents.Enums.IncomingDocumentStatus;
import Documents.forJson.IncomingDocument;
import PlansExecutionDocuments.interfaces.HandlerService;
import Services.USPNServices.DocHandlerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public abstract class AbstractHandlerService implements HandlerService {
    private final Logger logger = LoggerFactory.getLogger(AbstractHandlerService.class);
    private final long DEFAULT_WAIT = 3000; // 3 seconds
    private final long DEFAULT_COMMON_WAIT = 1800000; // 30 minute
    protected DocHandlerService docHandlerService;
    protected List<IncomingDocument> incomingDocList;
    protected List<Document> documentList;
    protected final String currentDate =  new SimpleDateFormat("dd.MM.yyyy").format(new Date());

    AbstractHandlerService(){
        docHandlerService = DocHandlerService.create();
    }

    @Override
    public void runHandler(List<Document> documentList){
        this.documentList = documentList;
        try {
            authorization();
            getIncomingDocument();
            boolean isReflectingStart = reflectDocument();
            if (isReflectingStart) {
                waiteStatusOfDocument(IncomingDocumentStatus.REFLECTED);

                rollbackDocument();
                waiteStatusOfDocument(IncomingDocumentStatus.REMOVED);
            }
        } catch (Exception e) {
            logger.error("FAILED", e);
        }
    }

    @Override
    public void authorization() throws Exception{
        docHandlerService.authorizationService().doAuthorization();
    }

    private void getIncomingDocument() throws Exception{
        incomingDocList = docHandlerService.incomingService().getIncomingDocList(currentDate)
                .stream()
                .filter(incDoc ->
                        documentList.stream().map(Document::getArchiveName).collect(Collectors.toList())
                                .contains(incDoc.getArchiveName()))
                .collect(Collectors.toList());
        addHistoryRecord();

        incomingDocList = incomingDocList
                .stream()
                .filter(inDoc -> {
                    if (inDoc.getStatusENG().equals(IncomingDocumentStatus.BK_REJECTED.name())) {
                        logger.warn(String.format("WARN! Document %s status: %s(%s)\r\n\tReflecting of this document is STOPPED!"
                                , inDoc.getNumber()
                                , inDoc.getStatusRUS()
                                , inDoc.getStatusENG()));
                        return false;
                    }

                    return true;
                })
                .collect(Collectors.toList());

    }

    private void addHistoryRecord() throws Exception{
        documentList.forEach(
                document -> {
                    Optional<IncomingDocument> optionalDocument = incomingDocList.stream()
                            .filter(incDoc -> incDoc.getArchiveName().equals(document.getArchiveName()))
                            .findFirst();
                    if (optionalDocument.isPresent()) {
                        IncomingDocument incomingDoc = optionalDocument.get();
                        String dateChange = incomingDoc.getDatechange();
                        String statusDoc = incomingDoc.getStatusRUS();

                        if (!document.getHistory().containsKey(statusDoc)) {
                            document.addHistoryRecord(statusDoc, dateChange);
                            logger.info(String.format("Document with number %s have status: %s at changeTime %s"
                                    , incomingDoc.getNumber(), statusDoc, dateChange));
                        }
                    }
                }
        );
    }

    protected void waiteStatusOfDocument(IncomingDocumentStatus status, IncomingDocType docType) throws Exception {
        boolean isDocHaveStatus = false;

        long startTime = new Date().getTime();
        while (!isDocHaveStatus) {
            waitSeconds();
            getIncomingDocument();
            isDocHaveStatus = incomingDocList.stream()
                    .filter(doc -> doc.getType().getTypeName().equals(docType.name()))
                    .allMatch(doc -> doc.getStatusENG().equals(status.name()));

            if (isTotalWaitTimeEnd(startTime)){
                throw new Exception(String.format("The process is working very long. Wort time: %d minutes. The process will be STOPPED!"
                        , DEFAULT_COMMON_WAIT/1000/60));
            }
        }

    }

    private void waitSeconds(){
        try {
            Thread.sleep(DEFAULT_WAIT);
        } catch (InterruptedException e) {
            logger.error("FAILED", e);
        }
    }

    private boolean isTotalWaitTimeEnd(long startTime){
        return startTime + DEFAULT_COMMON_WAIT <= new Date().getTime();
    }

}
