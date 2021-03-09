package com.sample.leantech.transfer.model.db;

public enum NameTypeField {

    BUSSINESS("Бизнес"),
    SUBSYSTEM("Подсистема"),
    CUSTOMER("Заказчик");

    private final String nameType;

    NameTypeField(String nameType) {
        this.nameType = nameType;
    }

    public String getNameType(){
        return nameType;
    }

}
