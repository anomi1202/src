package PlansExecutionDocuments;

public class SvedeniaHandler extends AbstractHandlerService {

    private SvedeniaHandler(){
        super();
    }

    public static SvedeniaHandler getInstance() {
        return new SvedeniaHandler();
    }

    @Override
    public void reflectDocument() {
        docHandlerService.incomingService().reflectDocument(incomingDocList);
    }

    @Override
    public void rollbackDocument() {
        docHandlerService.incomingService().rollbackDocument(incomingDocList);
    }
}
