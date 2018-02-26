package uspn.test.data;

public enum DocTypes {

    V_SPN_DOG_NVS("type_1"),
    V_SPN_ZAJAV("type_2"),
    V_SPN_ANLIS("type_3"),
    V_SPN_R_SUD("type_4"),
    V_MSK_OTKAZ("type_5"),
    V_UVED_COURT("type_7"),
    V_UVED_OZV_SPN("type_8"),
    V_ZAPROS("type_15"),
    V_UVED_O_NAZNACHENII("type_27"),
    V_UVED_SPN_NEVSTUP_NPF("type_28"),
    V_SVEDENIYA_SVERKA_ILS("type_29"),
    V_UVED_VIPLATI("type_30"),
    V_UVED_PP("type_31"),
    V_UVED_OTKAZ_MSK("type_32"),
    V_UVED_INV_EST_P("type_39"),
    V_UVED_INV_NOT_EST_P("type_40"),
    V_UVED_DEATH("type_85");

    private String type;

    DocTypes(String type){
        this.type = type;
    }

    public String getType() { return type;}

    public static DocTypes valueEnOf(String type) {
        for (DocTypes nameEn : values()) {
            if (nameEn.getType().equals(type)) {
                return nameEn;
            }
        }
        return null;
    }

}
