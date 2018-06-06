package Enums;

public enum DisposalStatus {

    PREPARED("Подготовлен"),
    APPROVED("Утвержден"),
    CONFIRMED("Подтвержден"),
    APPROVED_PROCCESSED("Утвержден. Обработан");

    private String cyrillicName;

    DisposalStatus(String cyrillycName) {
        this.cyrillicName = cyrillycName;
    }

    public String getCyrillicName() {
        return cyrillicName;
    }
}
