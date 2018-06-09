package PlansExecutionDocuments;

import Documents.Document;
import Documents.Enums.IncomingDocumentStatus;
import Documents.forJson.incoming.IncomingDocument;
import Documents.forJson.incoming.Status;
import PlansExecutionDocuments.interfaces.HandlerService;
import Services.USPNServices.DocHandlerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public abstract class AbstractHandlerService implements HandlerService {
    private final Logger logger = LoggerFactory.getLogger(AbstractHandlerService.class);
    private final long DEFAULT_WAIT = 1000;
    protected DocHandlerService docHandlerService;
    protected List<IncomingDocument> incomingDocList;
    protected final String currentDate =  new SimpleDateFormat("dd.MM.yyyy").format(new Date());

    AbstractHandlerService(){
        docHandlerService = DocHandlerService.create();
    }

    @Override
    public void runHandler(List<Document> documentList){
        try {
            authorization();
            addHistoryRecord(documentList);
            reflectDocument();
            boolean isAllDocumentsReflected = false;
            while (!isAllDocumentsReflected) {
                waitSeconds();
                isAllDocumentsReflected = checkStatusDocument(documentList, IncomingDocumentStatus.REFLECTED);
            }

            rollbackDocument();
            boolean isAllDocumentsRemoved = false;
            while (!isAllDocumentsRemoved) {
                waitSeconds();
                isAllDocumentsRemoved = checkStatusDocument(documentList, IncomingDocumentStatus.REMOVED);
            }
        } catch (Exception e) {
            logger.error("FAILED", e);
        }
    }

    @Override
    public void authorization() throws Exception{
        docHandlerService.authorizationService().doAuthorization();
    }

    @Override
    public void getIncomingDocument() throws Exception{
        incomingDocList = docHandlerService.incomingService().getIncomingDocList(currentDate);
    }

    @Override
    public void addHistoryRecord(List<Document> documentList) throws Exception{
        getIncomingDocument();
        documentList.forEach(
                document -> {
                    Optional<IncomingDocument> optionalDocument = incomingDocList.stream()
                            .filter(incDoc -> incDoc.getArchiveName().equals(document.getArchiveName()))
                            .findFirst();
                    if (optionalDocument.isPresent()) {
                        IncomingDocument incomingDoc = optionalDocument.get();
                        String dateChange = incomingDoc.getDatechange();
                        String statusDoc = incomingDoc.getStatus().getStatus();
                        logger.info(String.format("Document with number %s have status: %s at changeTime %s"
                                , incomingDoc.getNumber(), statusDoc, dateChange));

                        document.addHistoryRecord(statusDoc, dateChange);
                    }
                }
        );
    }

    private void waitSeconds(){
        try {
            Thread.sleep(DEFAULT_WAIT);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private boolean checkStatusDocument(List<Document> documentList, IncomingDocumentStatus status){
        try {
            addHistoryRecord(documentList);
        } catch (Exception e) {
            logger.error("FAILED", e);
        }
        long reflectedDoc = incomingDocList.stream().filter(doc -> doc.getStatus().getNameUI().equals(status.name())).count();
        return (long) incomingDocList.size() == reflectedDoc;
    }

}
