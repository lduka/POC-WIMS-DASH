package co.za.pos.wims.enterprise.pocwimsdash.webservice.impl;

import co.za.pos.wims.enterprise.pocwimsdash.webservice.WebServiceResponse;

import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

public class SimpleResponse implements WebServiceResponse {
    private final HttpResponse<String> resp;

    public SimpleResponse(HttpResponse<String> resp) {
        this.resp = resp;
    }

    @Override
    public int getStatusCode() {
        return resp.statusCode();
    }

    @Override
    public Map<String, String> getHeaders() {
        Map<String, String> map = new HashMap<>();
        resp.headers()
                .map()
                .forEach((k, v) -> map.put(k, String.join(",", v)));
        return map;
    }

    @Override
    public String getBodyString() {
        return resp.body();
    }
}
