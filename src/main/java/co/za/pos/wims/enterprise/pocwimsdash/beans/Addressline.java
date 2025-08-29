package co.za.pos.wims.enterprise.pocwimsdash.beans;

import co.za.pos.wims.enterprise.pocwimsdash.beans.util.E_ADDRESS_TYPE;

public class Addressline {

    private Long addressLineId;
    private E_ADDRESS_TYPE addressType;
    private String addressLine1;
    private String addressLine2;
    private String addressLine3;




    public Long getAddressLineId() {
        return addressLineId;
    }

    public void setAddressLineId(Long addressLineId) {
        this.addressLineId = addressLineId;
    }

    public E_ADDRESS_TYPE getAddressType() {
        return addressType;
    }

    public void setAddressType(E_ADDRESS_TYPE addressType) {
        this.addressType = addressType;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public String getAddressLine3() {
        return addressLine3;
    }

    public void setAddressLine3(String addressLine3) {
        this.addressLine3 = addressLine3;
    }

  }
