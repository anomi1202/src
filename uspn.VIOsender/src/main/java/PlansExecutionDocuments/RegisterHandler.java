package PlansExecutionDocuments;

import Documents.Enums.IncomingDocType;
import Documents.Enums.IncomingDocumentStatus;
import Documents.forJson.DisposalDocument;
import Documents.forJson.IncomingDocument;
import Documents.forJson.Payment;
import Documents.forJson.interfaces.IDocuments;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

public class RegisterHandler extends AbstractHandlerService {
    private final Logger logger = LoggerFactory.getLogger(RegisterHandler.class);
    private List<DisposalDocument> disposalsList;

    private RegisterHandler(){
        super();
    }

    public static RegisterHandler getInstance() {
        return new RegisterHandler();
    }

    @Override
    public boolean reflectDocument() throws Exception{
        confirmPayments(incomingDocList);
        docHandlerService.incomingService().checkOnBaseControl(incomingDocList);
        waiteStatusOfDocument(IncomingDocumentStatus.BK_PASSED);

        String disposalDate = currentDate;
        String disposalNum = "Stress_test-" + disposalDate;
        boolean isDisposalCreate = createDisposal(disposalNum, disposalDate);
        if (isDisposalCreate) {
            reflectDisposal(disposalNum, disposalDate);
            return true;
        } else {
            return false;
        }

    }

    private void reflectDisposal(String disposalNum, String disposalDate) {
        //Get disposal list and find our disposal with disposalNum and disposalDate
        disposalsList = docHandlerService.disposalService().getDisposalsList(currentDate)
                .stream()
                .filter(disposal ->
                        disposal.getNumber().equals(disposalNum)
                                && disposal.getDate().equals(disposalDate)
                ).collect(Collectors.toList());
        //Confirm disposal
        docHandlerService.disposalService().confirmDisposal(disposalsList);

        //For each disposal create PP and confirm PP for this disposal
        confirmPayments(disposalsList);

        //Reflect disposal
        docHandlerService.disposalService().reflectDisposal(this.disposalsList);
    }

    private void confirmPayments(List<? extends IDocuments> documentOrDisposal){
        documentOrDisposal
                .stream()
                .filter(doc -> (doc instanceof DisposalDocument)
                        || (doc.getType().getTypeName().equals(IncomingDocType.V_REGISTERS.name())
                                    && doc.getStatusENG().equals(IncomingDocumentStatus.SAVE.name()))
                )
                .forEach(
                        doc -> {
                            createPaymentFor(doc);
                            docHandlerService.paymentService().confirmPayments(doc.getId());
                        });
    }

    private void createPaymentFor(IDocuments document) {
        String numberPP = "PP-001";
        String datePP = currentDate;
        int amountPP = 100;

        List<Payment> paymentList = docHandlerService.paymentService().getPaymentsOfDocument(document.getId());
        Payment newPayment = docHandlerService.paymentService()
                .createNewPayment(new Payment().withNumber(numberPP).withDate(datePP).withAmount(amountPP));
        paymentList.add(newPayment);

        if (document instanceof IncomingDocument) {
            docHandlerService.paymentService().paymentsLinkToDocument(document.getId(), paymentList);
        } else {
            docHandlerService.paymentService().paymentsLinkToDisposal(document.getId(), paymentList);
        }
    }

    private boolean createDisposal(String disposalNum, String disposalDate) {
        return docHandlerService.incomingService().prepareAndCreateDisposal(disposalNum, disposalDate, incomingDocList);
    }

    @Override
    public void waiteStatusOfDocument(IncomingDocumentStatus status) throws Exception {
        super.waiteStatusOfDocument(status, IncomingDocType.V_REGISTERS);
    }

    @Override
    public boolean rollbackDocument() throws Exception{
        disposalsList = docHandlerService.disposalService().getDisposalsList(currentDate)
                .stream()
                .filter(disp ->
                        disposalsList.stream().map(DisposalDocument::getNumber)
                                .collect(Collectors.toList())
                                .contains(disp.getNumber()))
                .collect(Collectors.toList());

        docHandlerService.disposalService().rollbackDisposal(disposalsList);
        return docHandlerService.incomingService().rollbackDocument(incomingDocList);
    }
}
