package Documents.Enums;

public enum IncomingDocumentStatus {
    SAVE("Сохранен"),
    CONFIRMED("Подтвержден"),
    BK_IN_PROCESSING("В процессе БК"),
    BK_REJECTED("Отклонен на БК"),
    BK_PASSED("БК пройден"),
    MARKED_FOR_REFLECTION("Включен в распоряжение"),
    READY_FOR_REFLECTION("Готов к отражению"),
    REFLECTION_PREPARED("Подготовлен к отражению"),
    REFLECTING("Отражение в СЧ"),
    REFLECTED("Отражен в СЧ"),
    REMOVE_IN_PROCESSING("В процессе отзыва"),
    REMOVED("Отозван")
    ;

    private final String cyrillicName;

    IncomingDocumentStatus(String cyrillicName){
        this.cyrillicName = cyrillicName;
    }

    public String getCyrillicName() {
        return cyrillicName;
    }
}
