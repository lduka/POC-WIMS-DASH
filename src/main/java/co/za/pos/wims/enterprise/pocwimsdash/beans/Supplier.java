package co.za.pos.wims.enterprise.pocwimsdash.beans;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import co.za.pos.wims.enterprise.pocwimsdash.beans.util.E_SUPPLIER_TYPE;
import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Supplier {
    private Long id;

    private String supplierName;
    // New metadata fields
    private String vatNumber;
    private String supplierDescription;
    private E_SUPPLIER_TYPE supplierType;

    @JsonAlias({"contacts","contactInfo"})
    private List<ContactInfoLine> contactInfoLineList = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getVatNumber() {
        return vatNumber;
    }

    public void setVatNumber(String vatNumber) {
        this.vatNumber = vatNumber;
    }

    public String getSupplierDescription() {
        return supplierDescription;
    }

    public void setSupplierDescription(String supplierDescription) {
        this.supplierDescription = supplierDescription;
    }

    public E_SUPPLIER_TYPE getSupplierType() {
        return supplierType;
    }

    public void setSupplierType(E_SUPPLIER_TYPE supplierType) {
        this.supplierType = supplierType;
    }

    public List<ContactInfoLine> getContactInfoLineList() {
        return contactInfoLineList;
    }

    public void setContactInfoLineList(List<ContactInfoLine> contactInfoLineList) {
        this.contactInfoLineList = contactInfoLineList;
    }
}
