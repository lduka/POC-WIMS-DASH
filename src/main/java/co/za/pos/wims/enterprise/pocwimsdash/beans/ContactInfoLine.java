package co.za.pos.wims.enterprise.pocwimsdash.beans;

import co.za.pos.wims.enterprise.pocwimsdash.beans.util.I_STATUS_CONSTANTS;

import java.util.Date;

public class ContactInfoLine {

    private Long contactInfoLineId;
    private String contactInfoLineName;
    private String contactInfoLineValue;
    private String contactInfoLineType;
    private I_STATUS_CONSTANTS contactInfoLineStatus;
    private String contactInfoLineDescription;
    private Long contactInfoLineCreatedBy;
    private Date contactInfoLineCreatedDate;
    private String contactInfoLineUpdatedBy;
    private Date contactInfoLineUpdatedDate;


    public Long getContactInfoLineId() {
        return contactInfoLineId;
    }

    public void setContactInfoLineId(Long contactInfoLineId) {
        this.contactInfoLineId = contactInfoLineId;
    }

    public String getContactInfoLineName() {
        return contactInfoLineName;
    }

    public void setContactInfoLineName(String contactInfoLineName) {
        this.contactInfoLineName = contactInfoLineName;
    }

    public String getContactInfoLineValue() {
        return contactInfoLineValue;
    }

    public void setContactInfoLineValue(String contactInfoLineValue) {
        this.contactInfoLineValue = contactInfoLineValue;
    }

    public String getContactInfoLineType() {
        return contactInfoLineType;
    }

    public void setContactInfoLineType(String contactInfoLineType) {
        this.contactInfoLineType = contactInfoLineType;
    }

    public I_STATUS_CONSTANTS getContactInfoLineStatus() {
        return contactInfoLineStatus;
    }

    public void setContactInfoLineStatus(I_STATUS_CONSTANTS contactInfoLineStatus) {
        this.contactInfoLineStatus = contactInfoLineStatus;
    }

    public String getContactInfoLineDescription() {
        return contactInfoLineDescription;
    }

    public void setContactInfoLineDescription(String contactInfoLineDescription) {
        this.contactInfoLineDescription = contactInfoLineDescription;
    }

    public Long getContactInfoLineCreatedBy() {
        return contactInfoLineCreatedBy;
    }

    public void setContactInfoLineCreatedBy(Long contactInfoLineCreatedBy) {
        this.contactInfoLineCreatedBy = contactInfoLineCreatedBy;
    }

    public Date getContactInfoLineCreatedDate() {
        return contactInfoLineCreatedDate;
    }

    public void setContactInfoLineCreatedDate(Date contactInfoLineCreatedDate) {
        this.contactInfoLineCreatedDate = contactInfoLineCreatedDate;
    }

    public String getContactInfoLineUpdatedBy() {
        return contactInfoLineUpdatedBy;
    }

    public void setContactInfoLineUpdatedBy(String contactInfoLineUpdatedBy) {
        this.contactInfoLineUpdatedBy = contactInfoLineUpdatedBy;
    }

    public Date getContactInfoLineUpdatedDate() {
        return contactInfoLineUpdatedDate;
    }

    public void setContactInfoLineUpdatedDate(Date contactInfoLineUpdatedDate) {
        this.contactInfoLineUpdatedDate = contactInfoLineUpdatedDate;
    }
}
