package PlansExecutionDocuments;

import Documents.forJson.disposal.DisposalDocument;
import Documents.forJson.payment.Payment;

import java.util.List;
import java.util.stream.Collectors;

public class RegisterHandler extends AbstractHandlerService {
    private List<DisposalDocument> disposalsList;

    private RegisterHandler(){
        super();
    }

    public static RegisterHandler getInstance() {
        return new RegisterHandler();
    }

    @Override
    public void reflectDocument() {
        docHandlerService.incomingService().checkOnBaseControl(incomingDocList);

        String disposalDate = currentDate;
        String disposalNum = "Stress_test-" + disposalDate;
        boolean isDisposalCreate = createDisposal(disposalNum, disposalDate);
        if (isDisposalCreate) {
            reflectDisposal(disposalNum, disposalDate);
        }
    }

    private void reflectDisposal(String disposalNum, String disposalDate) {
        //Get disposal list and find our disposal with disposalNum and disposalDate
        disposalsList = docHandlerService.disposalService().getDisposalsList(disposalDate)
                .stream()
                .filter(disposal -> disposal.getNumber().equals(disposalNum) && disposal.getDate().equals(disposalDate))
                .collect(Collectors.toList());
        //Confirm disposal
        docHandlerService.disposalService().confirmDisposal(disposalsList);

        //For each disposal create PP and confirm PP for this disposal
        disposalsList.forEach(
                disp -> {
                    createPaymentForDisposal(disp);
                    docHandlerService.paymentService().confirmPayments(disp.getId());
                }
        );

        //Reflect disposal
        docHandlerService.disposalService().reflectDisposal(disposalsList);
    }

    private void createPaymentForDisposal(DisposalDocument disposal) {
        String numberPP = "PP-001";
        String datePP = currentDate;
        int amountPP = 100;

        List<Payment> paymentList = docHandlerService.paymentService().getPaymentsOfDocument(disposal.getId());
        Payment newPayment = docHandlerService.paymentService()
                .createNewPayment(new Payment().withNumber(numberPP).withDate(datePP).withAmount(amountPP));
        paymentList.add(newPayment);
        docHandlerService.paymentService().paymentsLinkToDisposal(disposal.getId(), paymentList);
    }

    private boolean createDisposal(String disposalNum, String disposalDate) {
        return docHandlerService.incomingService().prepareAndCreateDisposal(disposalNum, disposalDate, incomingDocList);
    }

    @Override
    public void rollbackDocument() {
        docHandlerService.disposalService().rollbackDisposal(disposalsList);
        docHandlerService.incomingService().rollbackDocument(incomingDocList);
    }
}
