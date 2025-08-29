package co.za.pos.wims.enterprise.pocwimsdash.beans.util;

public enum E_ADDRESS_TYPE {

    BUSINESS("Business"),
    HOME("Home"),
    WORK("Work"),
    OTHER("Other");

    private String value;

    private E_ADDRESS_TYPE(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
