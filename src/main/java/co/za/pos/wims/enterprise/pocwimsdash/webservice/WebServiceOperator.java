package co.za.pos.wims.enterprise.pocwimsdash.webservice;

import co.za.pos.wims.enterprise.pocwimsdash.webservice.config.ApiEndpoint;

import java.io.IOException;
import java.net.URLEncoder;
import java.net.http.HttpRequest;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

public class WebServiceOperator<T> extends WebServiceMarshal<T> {
    private final Map<String, String> parameters = new HashMap<>();
    private final ApiEndpoint endpoint;
    private boolean singleResult = false; // whether the expected result is a single entity
    private Object requestBody; // optional body for POST/PUT

    public WebServiceOperator(Class<T> type, ApiEndpoint endpoint) {
        super(type, endpoint);
        this.endpoint = endpoint;
    }

    public static <R> WebServiceOperator<R> build(Class<R> type, ApiEndpoint apiEndpoint) {
        return new WebServiceOperator<>(type, apiEndpoint);
    }

    public WebServiceOperator<T> adapt(T t) {
        return this;
    }

    public WebServiceOperator<T> withBody(Object body) {
        this.requestBody = body;
        return this;
    }

    public Object getBody() {
        return requestBody;
    }

    public boolean hasBody() { return requestBody != null; }

    public WebServiceOperator<T> setParameters(Map<String, String> parameters) {
        if (parameters != null) {
            this.parameters.clear();
            this.parameters.putAll(parameters);
            performCallMutex();
        }
        return this;
    }

    public WebServiceOperator<T> setParameter(String key, String value) {
        if (key != null && value != null) {
            this.parameters.put(key, value);
        }
        return this;
    }

    // Indicate that the caller expects a single entity result
    public WebServiceOperator<T> expectOne() {
        this.singleResult = true;
        return this;
    }

    public boolean isSingleResult() {
        return singleResult;
    }

    // Expose endpoint to delegates
    public ApiEndpoint getEndpoint() {
        return endpoint;
    }

    // Expose parameters to delegates (read-only copy if needed)
    public Map<String, String> getParameters() {
        return parameters;
    }

    // Helper to compose full URL with query parameters
    public String buildUrlWithParams() {
        String base = endpoint.url();
        if (!parameters.isEmpty()) {
            StringJoiner sj = new StringJoiner("&");
            parameters.forEach((k, v) -> sj.add(URLEncoder.encode(k, StandardCharsets.UTF_8) + "=" + URLEncoder.encode(v, StandardCharsets.UTF_8)));
            base = base + (base.contains("?") ? "&" : "?") + sj.toString();
        }
        return base;
    }

    private void performCallMutex() {
        // placeholder for future synchronization or validation
    }

    public WebServiceOperator<T> execute() {
        try {
            switch (endpoint.getMethod()) {
                case GET -> DO_GET();
                case POST -> {
                    if (hasBody()) {
                        DO_POST_JSON(requestBody);
                    } else {
                        DO_POST(HttpRequest.BodyPublishers.noBody());
                    }
                }
                case PUT -> doPUT(endpoint, HttpRequest.BodyPublishers.noBody());
                case DELETE -> doDELETE(endpoint);
                default -> throw new IllegalStateException("Unsupported HTTP method: " + endpoint.getMethod());
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Failed to execute web service operation", e);
        }
        return this;
    }
}
