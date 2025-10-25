package co.za.pos.wims.enterprise.pocwimsdash.webservice;

import java.util.Map;

public interface WebServiceResponse {
    int getStatusCode();
    Map<String, String> getHeaders();
    String getBodyString();
}
