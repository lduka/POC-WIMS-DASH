package co.za.pos.wims.enterprise.pocwimsdash.beans;

import co.za.pos.wims.enterprise.pocwimsdash.webservice.WebServiceCommandDelegate;
import co.za.pos.wims.enterprise.pocwimsdash.webservice.WebServiceOperator;
import co.za.pos.wims.enterprise.pocwimsdash.webservice.config.ApiEndpoint;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

import java.io.Serializable;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

@Named
@ViewScoped
public class ProductBean implements Serializable {

    // Form fields
    private String productName;
    private String displayImageName;
    private String barcode;
    private Double price;

    // Dropdown selections (bind to IDs to avoid converters)
    private Long categoryId;
    private Status status;
    private Long supplierId;
    private Long packageTypeId;

    // Dropdown data
    private List<ProductCategory> categories;
    private List<Supplier> suppliers;
    private List<PackageType> packageTypes;

    // List data for Stock List page
    private List<Product> products = new ArrayList<>();

    public Status[] getStatuses() {
        return Status.values();
    }

    @PostConstruct
    public void init() {
        // Initialize collections
        categories = new ArrayList<>();
        suppliers = new ArrayList<>();
        packageTypes = new ArrayList<>();

        // Placeholder categories until category service exists
        ProductCategory c1 = new ProductCategory();
        c1.setId(1L); c1.setCategoryName("General");
        ProductCategory c2 = new ProductCategory();
        c2.setId(2L); c2.setCategoryName("Beverages");
        categories.add(c1);
        categories.add(c2);

        // Load thin supplier list (id, name) from backend
        try {
            WebServiceOperator<Supplier> op = new WebServiceOperator<>(Supplier.class, ApiEndpoint.GET_ALL_SUPPLIERS);
            List<Supplier> remote = op.DO_GET();
            if (remote != null) {
                // ensure only id and name used by UI
                suppliers.clear();
                for (Supplier s : remote) {
                    Supplier thin = new Supplier();
                    thin.setId(s.getId());
                    thin.setSupplierName(s.getSupplierName());
                    suppliers.add(thin);
                }
            }
        } catch (Exception ignored) {
            // keep empty suppliers list if service fails
        }

        // Placeholder package types until service exists
        PackageType p1 = new PackageType(); p1.setId(1L); p1.setUnitsPerPackage(1); p1.setTotalNumberPerPackage(1);
        PackageType p2 = new PackageType(); p2.setId(2L); p2.setUnitsPerPackage(6); p2.setTotalNumberPerPackage(24);
        packageTypes.add(p1); packageTypes.add(p2);

        // Defaults
        status = Status.NEW;
    }

    // Load all products for Stock List page
    public void loadAll() {
        try {
            WebServiceOperator<Product> op = new WebServiceOperator<>(Product.class, ApiEndpoint.GET_ALL_PRODUCTS);
            List<Product> list = op.DO_GET();
            this.products = list != null ? list : new ArrayList<>();
        } catch (Exception e) {
            this.products = new ArrayList<>();
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Load error", e.getMessage()));
        }
    }

    public void save() {
        try {
            // Build Product DTO
            Product product = new Product();
            product.setProductName(productName);
            product.setDisplayImageName(displayImageName);
            product.setBarcode(barcode);
            product.setPrice(price == null ? 0.0 : price);

            if (categoryId != null) {
                ProductCategory pc = new ProductCategory();
                pc.setId(categoryId);
                product.setProductCategory(pc);
            }
            product.setStatus(status);

            if (supplierId != null) {
                Supplier sup = new Supplier();
                sup.setId(supplierId);
                product.setSupplier(sup);
            }
            if (packageTypeId != null) {
                PackageType pt = new PackageType();
                pt.setId(packageTypeId);
                product.setPackageType(pt);
            }

            WebServiceOperator<Product> op = new WebServiceOperator<>(Product.class, ApiEndpoint.CREATE_PRODUCT)
                    .withBody(product);
            HttpResponse<String> resp = WebServiceCommandDelegate.execute(op);

            int code = resp != null ? resp.statusCode() : 0;
            if (code >= 200 && code < 300) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Product saved", "Stock item created successfully."));
                reset();
            } else {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Save failed", "Service returned status " + code));
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Save error", e.getMessage()));
        }
    }

    public void reset() {
        productName = null;
        displayImageName = null;
        barcode = null;
        price = null;
        categoryId = null;
        status = Status.NEW;
        supplierId = null;
        packageTypeId = null;
    }

    // Getters & Setters
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    public String getDisplayImageName() { return displayImageName; }
    public void setDisplayImageName(String displayImageName) { this.displayImageName = displayImageName; }
    public String getBarcode() { return barcode; }
    public void setBarcode(String barcode) { this.barcode = barcode; }
    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }
    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }
    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }
    public Long getSupplierId() { return supplierId; }
    public void setSupplierId(Long supplierId) { this.supplierId = supplierId; }
    public Long getPackageTypeId() { return packageTypeId; }
    public void setPackageTypeId(Long packageTypeId) { this.packageTypeId = packageTypeId; }
    public List<ProductCategory> getCategories() { return categories; }
    public void setCategories(List<ProductCategory> categories) { this.categories = categories; }
    public List<Supplier> getSuppliers() { return suppliers; }
    public void setSuppliers(List<Supplier> suppliers) { this.suppliers = suppliers; }
    public List<PackageType> getPackageTypes() { return packageTypes; }
    public void setPackageTypes(List<PackageType> packageTypes) { this.packageTypes = packageTypes; }
    public List<Product> getProducts() { return products; }
    public void setProducts(List<Product> products) { this.products = products; }
}
