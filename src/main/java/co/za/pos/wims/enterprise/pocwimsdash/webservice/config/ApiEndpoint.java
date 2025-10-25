package co.za.pos.wims.enterprise.pocwimsdash.webservice.config;


import co.za.pos.wims.enterprise.pocwimsdash.webservice.HttpMethod;

public enum ApiEndpoint {
    GET_ALL_PRODUCTS("get-all-products" , HttpMethod.GET),
    CREATE_PRODUCT("create-product", HttpMethod.POST),
    CREATE_MOC_PRODUCT("create-moc-product", HttpMethod.POST),
    GET_PRODUCT("get-product", HttpMethod.GET),
    GET_PRODUCT_BY_SKU("get-product-by-sku", HttpMethod.GET),
    GET_PRODUCT_BY_BARCODE("get-product-by-barcode", HttpMethod.GET),
    CREATE_SALE("create-sale", HttpMethod.POST),
    CREATE_CASHIER("create-cashier", HttpMethod.POST),
    GET_ALL_CASHIERS("get-all-cashiers", HttpMethod.GET),
    FIND_CASHIER_BY_ID("find-cashier-by-id", HttpMethod.GET),
 FIND_CASHIER_BY_EMPLOYEE_ID("find-cashier-by-id", HttpMethod.GET);

    private final String key;
    private final HttpMethod method;

    public HttpMethod getMethod() {
        return method;
    }



    ApiEndpoint(String key, HttpMethod method) {
        this.key = key;
        this.method = method;
    }

    public String url() {
        return EndPointManager.getEndpoint(key);
    }
}
