package co.za.pos.wims.enterprise.pocwimsdash.beans;
import co.za.pos.wims.enterprise.pocwimsdash.beans.util.E_SUPPLIER_TYPE;
import co.za.pos.wims.enterprise.pocwimsdash.beans.util.I_STATUS_CONSTANTS;
import co.za.pos.wims.enterprise.pocwimsdash.webservice.WebServiceCommandDelegate;
import co.za.pos.wims.enterprise.pocwimsdash.webservice.WebServiceOperator;
import co.za.pos.wims.enterprise.pocwimsdash.webservice.config.ApiEndpoint;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Named
@ViewScoped
public class SupplierBean implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    private Long supplierId; // also used for view page param
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

    // List/Search state for supplier management
    private List<Supplier> suppliers = new ArrayList<>();
    private String searchQuery;

    // View state
    private Supplier selectedSupplier;

    // Helper: determine if a single contact line is effectively blank
    private boolean isBlank(ContactInfoLine ci) {
        if (ci == null) return true;
        String n = ci.getContactInfoLineName();
        String v = ci.getContactInfoLineValue();
        String t = ci.getContactInfoLineType();
        return (n == null || n.isBlank()) && (v == null || v.isBlank()) && (t == null || t.isBlank());
    }

    // Exposed to EL: true when no meaningful contact info exists on the Add Supplier form
    public boolean isContactInfoEmpty() {
        if (contactInfoLineList == null || contactInfoLineList.isEmpty()) return true;
        for (ContactInfoLine ci : contactInfoLineList) {
            if (!isBlank(ci)) return false;
        }
        return true;
    }

    // Exposed to EL: true when no meaningful contact info exists on the selected supplier (view page)
    public boolean isSelectedSupplierContactsEmpty() {
        if (selectedSupplier == null) return true;
        List<ContactInfoLine> list = selectedSupplier.getContactInfoLineList();
        if (list == null || list.isEmpty()) return true;
        for (ContactInfoLine ci : list) {
            if (!isBlank(ci)) return false;
        }
        return true;
    }

    public Integer getActiveIndex()
    {

        return activeIndex;
    }

    public void setActiveIndex(Integer activeIndex)
    {
        this.activeIndex = activeIndex;
    }

    public void next()
    {
        // simple validation before moving from step 0
        if (activeIndex != null && activeIndex == 0) {
            if (supplierName == null || supplierName.trim().isEmpty()) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_WARN, "Validation", "Supplier name is required"));
                return;
            }
        }
        if (activeIndex == null)
        {
            activeIndex = 0;
        }
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
        if (activeIndex == null) {
            activeIndex = 0;
        }
    }
    // Actions
    public void save() {
        try {
            Supplier dto = new Supplier();
            dto.setSupplierName(supplierName);
            // map additional fields to DTO
            dto.setVatNumber(vatNumber);
            dto.setSupplierDescription(supplierDescription);
            dto.setSupplierType(eSupplierType);

            // attach filtered contact list to DTO (phone/email will be derived from contacts on view)
            List<ContactInfoLine> toPost = new ArrayList<>();
            if (contactInfoLineList != null) {
                for (ContactInfoLine ci : contactInfoLineList) {
                    if (ci == null) { continue; }
                    String value = ci.getContactInfoLineValue();
                    String type = ci.getContactInfoLineType();
                    String name = ci.getContactInfoLineName();
                    Integer status = ci.getContactInfoLineStatus();
                    String desc = ci.getContactInfoLineDescription();
                    boolean hasData = (value != null && !value.isBlank()) || (name != null && !name.isBlank()) || (type != null && !type.isBlank())
                            || (desc != null && !desc.isBlank()) || (status != null);
                    if (hasData) {
                        // Build a thin DTO object explicitly setting fields to ensure they are included
                        ContactInfoLine postLine = new ContactInfoLine();
                        postLine.setContactInfoLineName(name);
                        postLine.setContactInfoLineValue(value);
                        postLine.setContactInfoLineType(type);
                        postLine.setContactInfoLineStatus(status);
                        postLine.setContactInfoLineDescription(desc);
                        toPost.add(postLine);
                    }
                }
            }
            dto.setContactInfoLineList(toPost);

            WebServiceOperator<Supplier> op = new WebServiceOperator<>(Supplier.class, ApiEndpoint.CREATE_SUPPLIER)
                    .withBody(dto);
            java.net.http.HttpResponse<String> resp = WebServiceCommandDelegate.execute(op);
            int code = resp != null ? resp.statusCode() : 0;
            if (code >= 200 && code < 300) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Supplier saved", "Supplier has been created successfully."));
                // refresh list if needed
                loadAll();
                // advance to completion step
                activeIndex = 3;
            } else {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Save failed", "Service returned status " + code));
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Save error", e.getMessage()));
        }
    }

    public void loadAll() {
        try {
            WebServiceOperator<Supplier> op = new WebServiceOperator<>(Supplier.class, ApiEndpoint.GET_ALL_SUPPLIERS);
            this.suppliers = op.DO_GET();
            if (this.suppliers == null) this.suppliers = Collections.emptyList();
        } catch (Exception e) {
            this.suppliers = Collections.emptyList();
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Load error", e.getMessage()));
        }
    }

    // UI action: flag a supplier (UI-only; no backend endpoint specified)
    public void flagSupplier(Long id) {
        this.supplierId = id;
        Supplier flagged = null;
        if (id != null) {
            if (suppliers == null || suppliers.isEmpty()) {
                loadAll();
            }
            if (suppliers != null) {
                for (Supplier s : suppliers) {
                    if (s != null && s.getId() != null && s.getId().equals(id)) {
                        flagged = s;
                        break;
                    }
                }
            }
        }
        String name = flagged != null && flagged.getSupplierName() != null ? flagged.getSupplierName() : (id != null ? ("#" + id) : "(unknown)");
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Flagged", "Supplier " + name + " has been flagged."));
    }

    public void search() {
        // For simple client-side filtering, we just keep searchQuery and rely on p:dataTable globalFilter
        // Alternatively, if backend supports query param, it can be added later.
    }

    // Load a single supplier into view state using supplierId
    public void loadOne() {
        // Try to resolve supplierId from request parameter if not set via viewParam (works inside includes)
        if (supplierId == null) {
            try {
                String param = FacesContext.getCurrentInstance()
                        .getExternalContext()
                        .getRequestParameterMap()
                        .get("supplierId");
                if (param != null && !param.isBlank()) {
                    supplierId = Long.valueOf(param);
                }
            } catch (Exception ignored) {

                // ignore parse errors; will fall through to not found message below
            }
        }
        if (supplierId == null) {
            return;
        }
        if (suppliers == null || suppliers.isEmpty()) {
            loadAll();
        }
        selectedSupplier = null;
        if (suppliers != null) {
            for (Supplier s : suppliers) {
                if (s != null && s.getId() != null && s.getId().equals(supplierId)) {
                    selectedSupplier = s;
                    break;
                }
            }
        }
        if (selectedSupplier == null) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN, "Not found", "Supplier not found for id " + supplierId));
        }
    }

    // Getters and Setters

    public Long getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Long supplierId) {
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

    public List<Supplier> getSuppliers() { return suppliers; }
    public void setSuppliers(List<Supplier> suppliers) { this.suppliers = suppliers; }
    public String getSearchQuery() { return searchQuery; }
    public void setSearchQuery(String searchQuery) { this.searchQuery = searchQuery; }

    public Supplier getSelectedSupplier() { return selectedSupplier; }
    public void setSelectedSupplier(Supplier selectedSupplier) { this.selectedSupplier = selectedSupplier; }

    // Derived contact info from contact lines for display
    public String getPrimaryPhone() {
        List<ContactInfoLine> list = null;
        if (selectedSupplier != null && selectedSupplier.getContactInfoLineList() != null && !selectedSupplier.getContactInfoLineList().isEmpty()) {
            list = selectedSupplier.getContactInfoLineList();
        } else {
            list = contactInfoLineList;
        }
        if (list != null) {
            for (ContactInfoLine ci : list) {
                if (ci == null) continue;
                String type = ci.getContactInfoLineType();
                if (type != null && (type.equalsIgnoreCase("phone") || type.equalsIgnoreCase("cell") || type.equalsIgnoreCase("mobile"))) {
                    String v = ci.getContactInfoLineValue();
                    if (v != null && !v.isBlank()) return v;
                }
            }
        }
        return null;
    }

    public String getPrimaryEmail() {
        List<ContactInfoLine> list = null;
        if (selectedSupplier != null && selectedSupplier.getContactInfoLineList() != null && !selectedSupplier.getContactInfoLineList().isEmpty()) {
            list = selectedSupplier.getContactInfoLineList();
        } else {
            list = contactInfoLineList;
        }
        if (list != null) {
            for (ContactInfoLine ci : list) {
                if (ci == null) continue;
                String type = ci.getContactInfoLineType();
                if (type != null && type.equalsIgnoreCase("email")) {
                    String v = ci.getContactInfoLineValue();
                    if (v != null && !v.isBlank()) return v;
                }
            }
        }
        return null;
    }
}
