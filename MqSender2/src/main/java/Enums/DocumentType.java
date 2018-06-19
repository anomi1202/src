package Enums;

public enum DocumentType {
    V_UNPF("УНПФ", "UNPF", "9080f0e00dd191e1b000000000ad1111"),
    V_SPN_DOG_NVS("РНПФ-У", "RNPF-U", "1011f1e11dd111e1b000000000ad1112"),
    V_SPN_ZAJAV("РНПФ", "RNPF", "5551f1e55dd555e5b000000000ad5551"),
    V_SPN_ANLIS("РНПФ-А", "RNPF-A", ""),
    V_SPN_R_SUD("РНПФ-С", "RNPF-S", "6661f6e66dd666e6b000000000ad6661"),
    V_MSK_OTKAZ("РНПФ-М", "RNPF-M", ""),
    V_MSK_DEATH("РНПФ-УМ", "RNPF-UM", ""),
    V_UVED_COURT("УОПС-С", "UOPS-S", "5000f5e75dd878e2b000000000ad1111"),
    V_UVED_OZV_SPN("ЗСПННПФ", "ZSPNNPF", "1881f8e11dd121e1b002200000ad1221"),
    V_ZAPROS("ЗННЧ", "ZNNCH", "6589f1e11dd111e1b000000000ad1111"),
    V_SVEDENIYA_PEREDANI("ПСПН-НПФ", "PSPN-NPF", ""),
    V_UVED_O_NAZNACHENII("РСПННПФ", "RSPNNPF", "7771f1e77dd777e1b000000000ad7771"),
    V_UVED_SPN_NEVSTUP_NPF("ИПСПН-НПФ", "IPSPN-NPF", "4441f4e44dd444e4b000000000ad4441"),
    V_SVEDENIYA_SVERKA_ILS("АСЛС", "ASLS", ""),
    V_UVED_VIPLATI("СФВ", "SVF", "1999f9e99dd999e1b000000000ad1111"),
    V_UVED_PP("ВСПН-П", "VSPN-P", "1311f1e11dd111e3b100010001ad1010"),
    V_UVED_OTKAZ_MSK("УОВМ", "UOVM", "1054f2e36dd112e1b000000000ad8187"),
    V_UVED_END_RORG("УЗР", "UZR", ""),
    V_SPN_ANLIS_NTCHP("РНПФ-НП", "RNPF-PP", ""),
    V_SPN_ANLIS_SPV("РНПФ-СПВ", "RNPF-SPV", ""),
    V_UVED_INV_EST_P("ИНСПН-В", "INSPN-V", "1033f3e33dd333e3b000000000ad3331"),
    V_UVED_INV_NOT_EST_P("ИНСПН", "INSPN", "2022f2e22dd222e2b000000000ad2222"),
    V_SPN_ANLIS_EV("РНПФ-ЕВ", "RNPF-EV", ""),
    V_SPN_NEPR("РНПФ-НЕПР", "RNPF-NEPR", ""),
    V_UVED_DEATH("СФС-НПФ", "SFS-NPF", "1088f8e88dd888e8b000000000ad1111");


    private final String cyrillicName;
    private final String messageID;
    private final String shortName;

    DocumentType(String cyrillicName, String shortName, String messageID) {
        this.cyrillicName = cyrillicName;
        this.shortName = shortName;
        this.messageID = messageID;
    }

    public String getShortName() {
        return shortName;
    }

    public String getMessageID() {
        return !messageID.equals("") ? messageID : DocumentType.V_SPN_R_SUD.getMessageID();
    }

    public String getCyrillicName() {
        return cyrillicName;
    }
}
