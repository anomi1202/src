package uspn.test.appmanager.data;

/**
 * Created by Andrey.Filyuta on 06.11.2015.
 */
public enum NotificationMessages {
    MESSAGES_DOC_GENERATED("Документ сформирован."),
    MESSAGES_DOC_CHECK_START("Документ(ы) поставлен(ы) в очередь на проверку БК"),
    MESSAGES_DOC_CHECK("Документ №%s дата %s был проверен."),
    MESSAGES_DOC_CHECK_WITH_ERROR("Документ №%s дата %s был проверен. Имеются ошибки."),
    REFLECTION_SH_START("Документ №%s дата %s поставлен в очередь на отражение в СЧ"),
    CONFIRM_REFLECTION_SPU_START("Документ №%s дата %s отправлен в сервис СПУ на обработку."),
    CONFIRM_REFLECTION_SPU_END("Документ №%s дата %s отражен."),
    MESSAGES_CHECK_DOC_REVIEW("Документ(ы) поставлен(ы) в очередь на отзыв"),
    MESSAGES_DOC_REVIEW("Документ №%s дата %s отозван."),
    MESSAGES_DOC_DELETE("Документ №%s дата %s удален."),
    MESSAGES_ERR("В ходе формирования документа возникла ошибка. Детальнее смотрите в кабинете специалиста."),
    MESSAGES_SET_UP_A_PAYMENT_ORDER("Создано платежное поручение №%s дата %s"),
    MESSAGES_DOCUMENT_VERIFIED("Документ был подтвержден"),
    MESSAGES_ORDER_PREPARED("Распоряжение №%s дата %s подготовлено"),
    MESSAGES_CONFIRM_ORDER("Распоряжение №%s дата %s подтверждено."),
    MESSAGES_WITHDRAWAL_ORDER("Распоряжение №%s дата %s отозвано."),
    MESSAGES_ORDER_SPU_END("Распоряжение №%s дата %s отражено"),
    MESSAGES_ORDER_NOT_DELETED("Нельзя удалить распоряжение, в которое включены документы"),
    MESSAGES_ORDER_DELETED("Распоряжение №%s дата %s удалено."),
    MESSAGES_RESPONSE_REQUEST("Документ сформирован. Тип: \"%s\""),
    MESSAGES_NOT_DELETED_DOC("Документ №%s дата %s отозвать нельзя, т.к. документ включен в распоряжение №%s дата %s со статусом 'Утвержден. Обработан'"),
    MESSAGES_CREATE_SK_PFR("Документ сформирован. Тип: \"СК-ПФР\", Дата: \"%s\", Номер: \"%s\".");

    private String message;

    NotificationMessages(String message){
        this.message = message;
    }

    public String getMessage(){
        return message;
    }
}
