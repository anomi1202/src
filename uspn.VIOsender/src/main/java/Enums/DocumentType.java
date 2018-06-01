package Enums;

import java.util.Arrays;

public enum DocumentType {
    V_UNPF("УНПФ", 0),
    V_SPN_DOG_NVS("РНПФ-У", 1),
    V_SPN_ZAJAV("РНПФ", 2),
    V_SPN_ANLIS("РНПФ-А", 3),
    V_SPN_R_SUD("РНПФ-С", 4),
    V_MSK_OTKAZ("РНПФ-М", 5),
    V_MSK_DEATH("РНПФ-УМ", 6),
    V_UVED_COURT("УОПС-С", 7),
    V_UVED_OZV_SPN("ЗСПННПФ", 8),
    V_ZAPROS("ЗННЧ", 15),
    V_SVEDENIYA_PEREDANI("ПСПН-НПФ", 26),
    V_UVED_O_NAZNACHENII("РСПННПФ", 27),
    V_UVED_SPN_NEVSTUP_NPF("ИПСПН-НПФ", 28),
    V_SVEDENIYA_SVERKA_ILS("АСЛС", 29),
    V_UVED_VIPLATI("СФВ", 30),
    V_UVED_PP("ВСПН-П", 31),
    V_UVED_OTKAZ_MSK("УОВМ", 32),
    V_UVED_END_RORG("УЗР", 34),
    V_SPN_ANLIS_NTCHP("РНПФ-НП", 35),
    V_SPN_ANLIS_SPV("РНПФ-СПВ", 36),
    V_UVED_INV_EST_P("ИНСПН-В", 39),
    V_UVED_INV_NOT_EST_P("ИНСПН", 40),
    V_SPN_ANLIS_EV("РНПФ-ЕВ", 41),
    V_SPN_NEPR("РНПФ-НЕПР", 43),
    V_UVED_DEATH("СФС-НПФ", 85);


    private final String cyrillicName;
    private final int numDocType;

    DocumentType(String cyrillicName, int numDocType) {
        this.cyrillicName = cyrillicName;
        this.numDocType = numDocType;
    }

    public static String getCyrillicName(int numDocType){
        return Arrays.stream(values()).filter(v -> v.numDocType == numDocType).findFirst().get().cyrillicName;
    }

    public static DocumentType getName(int numDocType){
        return Arrays.stream(values()).filter(v -> v.numDocType == numDocType).findFirst().get();
    }
}
