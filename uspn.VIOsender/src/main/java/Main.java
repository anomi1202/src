import Documents.disposal.DisposalDocument;
import USPN_WEB.DocHandlerServiceBuilder;

import java.util.ArrayList;
import java.util.List;

public class Main {
    private static DocHandlerServiceBuilder docHandlerService;

    public static void main(String[] args) {

        docHandlerService = DocHandlerServiceBuilder.create();
        docHandlerService.doAuthorization();
        List<DisposalDocument> v_disposal = list();
//        docHandlerService.getDisposalList("01.05.2018").forEach((disp -> System.out.println(disp.getId())));
//        Payment newPayment = docHandlerService.createNewPayment(Payment.newPayment().withNumber("123").withDate("15.05.2018").withAmount(123));
        docHandlerService.confirmDisposal(v_disposal)
                .forEach((k, v) -> System.out.println(k.getId() + ": " + v));
        v_disposal = list();

    }

    public static  List<DisposalDocument> list(){
        List<DisposalDocument> v_disposal =  new ArrayList<>(docHandlerService.getDisposalList("01.05.2018"));
        v_disposal.forEach(disp ->
                System.out.println(
                        disp.getStatusRUS() + "(" + disp.getStatusENG()+ ")" + ": " +
                                disp.getId() + ": " +
                                disp.getDate() + ": " +
                                disp.getNumber()

                ));

        return v_disposal;
    }
}
