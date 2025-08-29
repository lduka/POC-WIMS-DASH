package co.za.pos.wims.enterprise.pocwimsdash.beans.util;

import java.util.ArrayList;
import java.util.List;

public enum E_SUPPLIER_TYPE {
    PERISHABLE("Perishables"),
     WHOLE_SALE("Wholesale"),
     RESELLER("Reseller"),
    NON_PERISHABLE("Non-Perishables");

    private String supplierType;

    E_SUPPLIER_TYPE(String supplierType) {
        this.supplierType = supplierType;
    }

    public static List<E_SUPPLIER_TYPE> getSupplierTypes() {
       for (E_SUPPLIER_TYPE supplierType : E_SUPPLIER_TYPE.values()) {
           List<String> supplierTypes = new ArrayList<>();
           supplierTypes.add(supplierType.toString());
       }
        return null;
    }

    public String getSupplierType() {
        return supplierType;
    }

    @Override
    public String toString() {
        return supplierType;
    }

    public static E_SUPPLIER_TYPE getSupplierType(String supplierType) {
        for (E_SUPPLIER_TYPE supplierType1 : E_SUPPLIER_TYPE.values()) {
            if (supplierType1.getSupplierType().equalsIgnoreCase(supplierType)) {
                return supplierType1;
            }
        }
        return null;
    }

    public void setSupplierType(String supplierType) {
        this.supplierType = supplierType;
    }
}
