package PlansExecutionDocuments;

import Documents.Enums.IncomingDocType;
import Documents.Enums.IncomingDocumentStatus;

public class SvedeniaHandler extends AbstractHandlerService {

    private SvedeniaHandler(){
        super();
    }

    public static SvedeniaHandler getInstance() {
        return new SvedeniaHandler();
    }

    @Override
    public boolean reflectDocument() throws Exception{
        return docHandlerService.incomingService().reflectDocument(incomingDocList);
    }

    @Override
    public boolean rollbackDocument() throws Exception{
        return docHandlerService.incomingService().rollbackDocument(incomingDocList);
    }

    @Override
    public void waiteStatusOfDocument(IncomingDocumentStatus status) throws Exception {
        super.waiteStatusOfDocument(status, IncomingDocType.V_SVEDENIA);
    }
}
