package co.za.pos.wims.enterprise.pocwimsdash.beans;

import co.za.pos.wims.enterprise.pocwimsdash.webservice.WebServiceCommandDelegate;
import co.za.pos.wims.enterprise.pocwimsdash.webservice.WebServiceOperator;
import co.za.pos.wims.enterprise.pocwimsdash.webservice.config.ApiEndpoint;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.net.http.HttpResponse;
import java.util.Collections;
import java.util.List;

@Named
@ViewScoped
@com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)
public class CashierBean implements Serializable {
    // Cashier fields (used for create and also as DTO for GETs)
    private Long id; // present in backend responses
    private String name;
    private String employeeId;
    private String username;
    private String pin;
    private String status = "ACTIVE";
    private String notes;

    // UI/Query state
    @JsonIgnore
    private String searchId;                 // input for find-by-id
    @JsonIgnore
    private CashierBean foundCashier;        // result of find-by-id
    @JsonIgnore
    private List<CashierBean> cashierList;   // result of get-all

    @PostConstruct
    public void init() {
        // Initialize defaults if necessary
    }

    // CREATE cashier via POST
    public void save() {
        try {
            WebServiceOperator<CashierBean> op = new WebServiceOperator<>(CashierBean.class, ApiEndpoint.CREATE_CASHIER)
                    .withBody(this);

            HttpResponse<String> resp = WebServiceCommandDelegate.execute(op);

            int code = resp != null ? resp.statusCode() : 0;
            if (code >= 200 && code < 300) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Cashier saved", "Cashier has been created successfully."));
            } else {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Save failed",
                                "Service returned status " + code));
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Save error", e.getMessage()));
        }
    }

    // FIND cashier by ID using GET /findCashierByID/{id}
    public void findById() {
        if (searchId == null || searchId.isBlank()) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN, "Validation", "Please enter an ID"));
            return;
        }
        try {
            WebServiceOperator<CashierBean> op = new WebServiceOperator<>(CashierBean.class, ApiEndpoint.FIND_CASHIER_BY_ID)
                    .expectOne();
            String url = ApiEndpoint.FIND_CASHIER_BY_ID.url() + "/" + searchId.trim();
            this.foundCashier = op.DO_GET_ONE(url);
            if (this.foundCashier == null) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Not found", "No cashier found for ID " + searchId));
            }
        } catch (Exception e) {
            this.foundCashier = null;
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Find error", e.getMessage()));
        }
    }

    // LOAD all cashiers using GET /getAllCashiers
    public void loadAll() {
        try {
            WebServiceOperator<CashierBean> op = new WebServiceOperator<>(CashierBean.class, ApiEndpoint.GET_ALL_CASHIERS);
            this.cashierList = op.DO_GET();
        } catch (Exception e) {
            this.cashierList = Collections.emptyList();
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Load error", e.getMessage()));
        }
    }

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmployeeId() { return employeeId; }
    public void setEmployeeId(String employeeId) { this.employeeId = employeeId; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPin() { return pin; }
    public void setPin(String pin) { this.pin = pin; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public String getSearchId() { return searchId; }
    public void setSearchId(String searchId) { this.searchId = searchId; }
    public CashierBean getFoundCashier() { return foundCashier; }
    public void setFoundCashier(CashierBean foundCashier) { this.foundCashier = foundCashier; }
    public List<CashierBean> getCashierList() { return cashierList; }
    public void setCashierList(List<CashierBean> cashierList) { this.cashierList = cashierList; }
}
