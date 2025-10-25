package co.za.pos.wims.enterprise.pocwimsdash.beans;
import co.za.pos.wims.enterprise.pocwimsdash.beans.util.E_SUPPLIER_TYPE;
import co.za.pos.wims.enterprise.pocwimsdash.beans.util.I_STATUS_CONSTANTS;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Named;
import jakarta.enterprise.context.RequestScoped;

import java.util.ArrayList;
import java.util.List;

@Named
@RequestScoped
public class SupplierBean {
    private Integer supplierId;
    private String supplierName;
    private String vatNumber;
    private String businessRegNumber;
    private String supplierDescription;
    private I_STATUS_CONSTANTS iStatusConstants;
    private E_SUPPLIER_TYPE eSupplierType;
    private Addressline supplierAddressLine;
    private List<ContactInfoLine> contactInfoLineList= new ArrayList<>();
    private List<NoteItem>   noteItemList;
    private List<E_SUPPLIER_TYPE> supplierTypes= List.of(E_SUPPLIER_TYPE.values());
    private Integer activeIndex ;
    private ContactInfoLine contactInfoLine = new ContactInfoLine();


    public Integer getActiveIndex()
    {
        if(activeIndex == 1)
        {
            if(contactInfoLineList.isEmpty())
            {
                contactInfoLineList.add(contactInfoLine);
            }
        }
        return activeIndex;
    }

    public void setActiveIndex(Integer activeIndex)
    {
        this.activeIndex = activeIndex;
    }



    public void next()
    {
            activeIndex++;
    }

    public void previous() {
        if (activeIndex > 0) {
            activeIndex--;
        }
    }
    public void addSupplierAdrresses()
    {
        getContactInfoLineList().add(new ContactInfoLine());
    }
    public void removeSupplierAddress(ContactInfoLine contactInfoLine)
    {
        getContactInfoLineList().remove(contactInfoLine);
    }

    @PostConstruct
    public void init() {

        if (contactInfoLineList.isEmpty()) {
            contactInfoLineList.add(new ContactInfoLine());
        }
    }
    // Getters and Setters


    public Integer getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Integer supplierId) {
        this.supplierId = supplierId;
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

    public String getBusinessRegNumber() {
        return businessRegNumber;
    }

    public void setBusinessRegNumber(String businessRegNumber) {
        this.businessRegNumber = businessRegNumber;
    }

    public String getSupplierDescription() {
        return supplierDescription;
    }

    public void setSupplierDescription(String supplierDescription) {
        this.supplierDescription = supplierDescription;
    }

    public I_STATUS_CONSTANTS getiStatusConstants() {
        return iStatusConstants;
    }

    public void setiStatusConstants(I_STATUS_CONSTANTS iStatusConstants) {
        this.iStatusConstants = iStatusConstants;
    }

    public E_SUPPLIER_TYPE getSupplierType() {
        return eSupplierType;
    }

    public void setSupplierType(E_SUPPLIER_TYPE eSupplierType) {
        this.eSupplierType = eSupplierType;
    }

    public Addressline getSupplierAddressLine() {
        return supplierAddressLine;
    }

    public void setSupplierAddressLine(Addressline supplierAddressLine) {
        this.supplierAddressLine = supplierAddressLine;
    }

    public List<ContactInfoLine> getContactInfoLineList() {
        return contactInfoLineList;
    }

    public void setContactInfoLineList(List<ContactInfoLine> contactInfoLineList)
    {
        this.contactInfoLineList = contactInfoLineList;
    }

    public List<NoteItem> getNoteItemList() {
        return noteItemList;
    }

    public void setNoteItemList(List<NoteItem> noteItemList) {
        this.noteItemList = noteItemList;
    }

    public List<E_SUPPLIER_TYPE> getSupplierTypes() {
        return supplierTypes;
    }

    public void setSupplierTypes(List<E_SUPPLIER_TYPE> supplierTypes) {
        this.supplierTypes = supplierTypes;
    }


}
