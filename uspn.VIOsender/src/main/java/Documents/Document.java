package Documents;

import Documents.Enums.DocumentType;
import Documents.Enums.IncomingDocType;

import java.util.HashMap;
import java.util.Map;

public class Document {
    private String archiveName;
    private DocumentType type;
    private String number;
    private Integer amount;
    private IncomingDocType incomingDocType;
    private Map<String, String> historyOfHandling;

    public Document(String archiveName, DocumentType type) {
        this.archiveName = archiveName;
        this.type = type;
        this.historyOfHandling = new HashMap<>();
    }

    public String getArchiveName() {
        return archiveName;
    }

    public String getTypeRUR() {
        return type.getCyrillicName();
    }

    public DocumentType getTypeENG() {
        return type;
    }

    public String getNumber() {
        return number;
    }

    public Integer getAmount() {
        return amount;
    }

    public IncomingDocType getIncomingDocType() {
        return incomingDocType;
    }

    public Map<String, String> getHistory(){
        return historyOfHandling;
    }

    public void addHistoryRecord(String status, String dateChange){
        historyOfHandling.put(status, dateChange);
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public void setIncomingDocType(IncomingDocType incomingDocType) {
        this.incomingDocType = incomingDocType;
    }
}
