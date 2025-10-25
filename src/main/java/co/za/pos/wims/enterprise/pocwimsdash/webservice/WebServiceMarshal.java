package co.za.pos.wims.enterprise.pocwimsdash.webservice;


import co.za.pos.wims.enterprise.pocwimsdash.webservice.config.ApiEndpoint;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import static co.za.pos.wims.enterprise.pocwimsdash.webservice.HttpMethod.DELETE;
import static co.za.pos.wims.enterprise.pocwimsdash.webservice.HttpMethod.GET;
import static co.za.pos.wims.enterprise.pocwimsdash.webservice.HttpMethod.POST;
import static co.za.pos.wims.enterprise.pocwimsdash.webservice.HttpMethod.PUT;

public abstract class WebServiceMarshal<T> {
    private static final ObjectMapper mapper = new ObjectMapper();
    private final Class<T> entityClass;
    private final ApiEndpoint apiEndpoint;
    private final HttpClient client = HttpClient.newHttpClient();

    protected WebServiceMarshal(Class<T> entityClass, ApiEndpoint apiEndpoint) {
        this.entityClass = entityClass;
        this.apiEndpoint = apiEndpoint;
    }

    public List<T> DO_GET() throws IOException, InterruptedException {
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(apiEndpoint.url()))
                .header("Accept", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = client.send(req, HttpResponse.BodyHandlers.ofString());
        int code = response.statusCode();
        if (code < 200 || code >= 300) {
            throw new RuntimeException("HTTP " + code + ": " + response.body());
        }
        JavaType listType = mapper.getTypeFactory().constructCollectionType(List.class, entityClass);
        return mapper.readValue(response.body(), listType);
    }

    public T DO_GET(ApiEndpoint endpoint) throws IOException, InterruptedException {
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(endpoint.url()))
                .header("Accept", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = client.send(req, HttpResponse.BodyHandlers.ofString());
        int code = response.statusCode();
        if (code < 200 || code >= 300) {
            throw new RuntimeException("HTTP " + code + ": " + response.body());
        }
        JavaType objectType = mapper.getTypeFactory().constructType(entityClass);
        return mapper.readValue(response.body(), objectType);
    }

    // New overload to support fully composed URLs (with query parameters)
    public T DO_GET_ONE(String url) throws IOException, InterruptedException {
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Accept", "application/json")
                .GET()
                .build();
        HttpResponse<String> response = client.send(req, HttpResponse.BodyHandlers.ofString());
        int code = response.statusCode();
        if (code < 200 || code >= 300) {
            throw new RuntimeException("HTTP " + code + ": " + response.body());
        }
        JavaType objectType = mapper.getTypeFactory().constructType(entityClass);
        return mapper.readValue(response.body(), objectType);
    }

    public HttpResponse<String> DO_POST(HttpRequest.BodyPublisher body) throws IOException, InterruptedException {
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(apiEndpoint.url()))
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .POST(body)
                .build();
        return client.send(req, HttpResponse.BodyHandlers.ofString());
    }

    // Convenience: serialize a POJO to JSON and POST it
    public HttpResponse<String> DO_POST_JSON(Object bodyObject) throws IOException, InterruptedException {
        String json = bodyObject == null ? "" : mapper.writeValueAsString(bodyObject);
        return DO_POST(HttpRequest.BodyPublishers.ofString(json));
    }

    protected void doPUT(ApiEndpoint endpoint, HttpRequest.BodyPublisher body) throws IOException, InterruptedException {
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(endpoint.url()))
                .header("Accept", "application/json")
                .PUT(body)
                .build();
        client.send(req, HttpResponse.BodyHandlers.discarding());
    }

    protected void doDELETE(ApiEndpoint endpoint) throws IOException, InterruptedException {
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(endpoint.url()))
                .header("Accept", "application/json")
                .DELETE()
                .build();
        client.send(req, HttpResponse.BodyHandlers.discarding());
    }

    public WebServiceMarshal<T> call() throws IOException, InterruptedException {
        switch (apiEndpoint.getMethod()) {
            case GET -> DO_GET();
            case POST -> DO_POST(HttpRequest.BodyPublishers.noBody());
            case PUT -> doPUT(apiEndpoint, HttpRequest.BodyPublishers.noBody());
            case DELETE -> doDELETE(apiEndpoint);
            default -> {
                // For methods not explicitly handled
            }
        }
        return this;
    }
}
