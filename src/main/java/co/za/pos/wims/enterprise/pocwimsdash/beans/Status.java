package co.za.pos.wims.enterprise.pocwimsdash.beans;


public enum Status {
    IN_STOCK("In Stock"),
    OUT_OF_STOCK("Out of stock"),
    ORDERED("Ordered"),
    NOT_ACTIVE("Not Active"),
    NOT_SELLING("Not Selling"),
    NOT_PURCHASING("Not Purchasing"),
    NEW("New");

    private String desc;

    Status(String description)
    {
        this.desc=description;
    }

    public String getDesc() {
        return desc;
    }

}
