package PlansExecutionDocuments;

import Documents.Document;
import Documents.Enums.IncomingDocumentStatus;
import Documents.forJson.incoming.IncomingDocument;
import PlansExecutionDocuments.interfaces.HandlerService;
import Services.USPNServices.DocHandlerService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

public abstract class AbstractHandlerService implements HandlerService {
    private final long DEFAULT_WAIT = 30000;
    protected DocHandlerService docHandlerService;
    protected List<IncomingDocument> incomingDocList;
    protected final String currentDate =  new SimpleDateFormat("dd.mm.yyyy").format(new Date());

    AbstractHandlerService(){
        docHandlerService = DocHandlerService.create();
        authorization();
    }

    @Override
    public void runHandler(List<Document> documentList){
        authorization();
        addHistoryRecord(documentList);
        reflectDocument();

        boolean isAllDocumentsReflected  = false;
        while (!isAllDocumentsReflected){
            waitSeconds();
            isAllDocumentsReflected = checkStatusDocument(documentList, IncomingDocumentStatus.REMOVED);
        }

        rollbackDocument();
        boolean isAllDocumentsRemoved  = false;
        while (!isAllDocumentsRemoved){
            waitSeconds();
            isAllDocumentsRemoved = checkStatusDocument(documentList, IncomingDocumentStatus.REMOVED);
        }
    }

    @Override
    public void authorization() {
        docHandlerService.authorizationService().doAuthorization();
    }

    @Override
    public void getIncomingDocument() {
        incomingDocList = docHandlerService.incomingService().getIncomingDocList(currentDate);
    }

    @Override
    public void addHistoryRecord(List<Document> documentList){
        getIncomingDocument();
        documentList.forEach(
                document -> {
                    Stream<IncomingDocument> incomingDocCStream = incomingDocList.stream()
                            .filter(incDoc -> incDoc.getArchiveName().equals(document.getArchiveName()));

                    String dateChange = incomingDocCStream.map(IncomingDocument::getDatechange).toString();
                    IncomingDocumentStatus status = IncomingDocumentStatus
                            .valueOf(incomingDocCStream.map(IncomingDocument::getStatus).findAny().get().getNameUI());

                    document.addHistoryRecord(status, dateChange);
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
        addHistoryRecord(documentList);
        long reflectedDoc = incomingDocList.stream().filter(doc -> doc.getStatus().getNameUI().equals(status.name())).count();
        return (long) incomingDocList.size() == reflectedDoc;
    }

}
