package co.za.pos.wims.enterprise.pocwimsdash.beans;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ContactInfoLine {
    private Long id;
    @JsonAlias({"contactId","id"})
    private Long contactInfoLineId;
    @JsonAlias({"contactName","name"})
    private String contactInfoLineName;
    @JsonAlias({"contactValue","value","contact"})
    private String contactInfoLineValue;
    @JsonAlias({"contactType","type"})
    private String contactInfoLineType;
    // Use simple Integer for status to avoid JSF converter issues in selectOneMenu
    @JsonAlias({"contactStatus","status"})
    private Integer contactInfoLineStatus;
    @JsonAlias({"contactDescription","description"})
    private String contactInfoLineDescription;
    private Long contactInfoLineCreatedBy;
    private Date contactInfoLineCreatedDate;
    private String contactInfoLineUpdatedBy;
    private Date contactInfoLineUpdatedDate;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

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

    public Integer getContactInfoLineStatus() {
        return contactInfoLineStatus;
    }

    public void setContactInfoLineStatus(Integer contactInfoLineStatus) {
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
