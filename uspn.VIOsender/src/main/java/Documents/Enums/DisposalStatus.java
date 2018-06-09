package Documents.Enums;

public enum DisposalStatus {

    PREPARED("Подготовлен"),
    APPROVED("Утвержден"),
    CONFIRMED("Подтвержден"),
    APPROVED_PROCCESSED("Утвержден. Обработан");

    private final String cyrillicName;

    DisposalStatus(String cyrillycName) {
        this.cyrillicName = cyrillycName;
    }

    public String getCyrillicName() {
        return cyrillicName;
    }
}
