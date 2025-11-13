package co.za.pos.wims.enterprise.pocwimsdash.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.ArrayList;
import java.util.List;

/**
 * DTO for creating a sale via the web service. Matches the JSON previously built in SalesDAO.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SaleCreateRequest
{
    private String cashier;
    private double total;
    private String createdAt;
    // New: type of transaction as integer code (e.g., 1=CASH_SALE, 2=RETURN, etc.)
    private Integer saleType;
    private List<SaleItemLine> saleItemLines = new ArrayList<>();

    public String getCashier() { return cashier; }
    public void setCashier(String cashier) { this.cashier = cashier; }

    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    public Integer getSaleType() { return saleType; }
    public void setSaleType(Integer saleType) { this.saleType = saleType; }

    public List<SaleItemLine> getLines() { return saleItemLines; }
    public void setLines(List<SaleItemLine> saleItemLines) { this.saleItemLines = saleItemLines; }

    public void addLine(SaleItemLine l) { if (l != null) this.saleItemLines.add(l); }
}
