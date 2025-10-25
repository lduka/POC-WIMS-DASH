package co.za.pos.wims.enterprise.pocwimsdash.webservice.impl;



import co.za.pos.wims.enterprise.pocwimsdash.webservice.Converter;
import co.za.pos.wims.enterprise.pocwimsdash.webservice.Interceptor;
import co.za.pos.wims.enterprise.pocwimsdash.webservice.WebServiceClient;
import co.za.pos.wims.enterprise.pocwimsdash.webservice.WebServiceRequest;
import co.za.pos.wims.enterprise.pocwimsdash.webservice.WebServiceResponse;
import co.za.pos.wims.enterprise.pocwimsdash.webservice.error.ConversionException;
import co.za.pos.wims.enterprise.pocwimsdash.webservice.error.HttpException;
import co.za.pos.wims.enterprise.pocwimsdash.webservice.error.WebServiceException;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;


public class DefaultWebServiceClient implements WebServiceClient {
    private final HttpClient client;
    private final Converter converter;
    private final Interceptor interceptor;

    public DefaultWebServiceClient(Converter converter, Interceptor interceptor) {
        this.client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
        this.converter = converter;
        this.interceptor = interceptor;
    }

    @Override
    public <T> T execute(WebServiceRequest<T> rawRequest) throws WebServiceException {
        WebServiceRequest<T> req = interceptor.onRequest(rawRequest);

        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create(req.getUrl()))
                .method(req.getMethod().name(),
                        req.getBody()
                                .map(body -> {
                                    try {
                                        return HttpRequest.BodyPublishers
                                                .ofString(converter.serialize(body));
                                    } catch (ConversionException e) {
                                        throw new RuntimeException("Failed to serialize request body", e);
                                    }
                                })
                                .orElse(HttpRequest.BodyPublishers.noBody()));

        req.getHeaders().forEach(builder::header);

        try {
            HttpResponse<String> httpResp = client.send(builder.build(),
                    HttpResponse.BodyHandlers.ofString());

            WebServiceResponse wrapped = new SimpleResponse(httpResp);
            wrapped = interceptor.onResponse(req, wrapped);

            int code = wrapped.getStatusCode();
            if (code < 200 || code >= 300) {
                throw new HttpException(code, wrapped.getBodyString());
            }

            return converter.deserialize(wrapped.getBodyString(),
                    req.getResponseType());
        } catch (WebServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new WebServiceException("Execution error", e);
        }
    }

    @Override
    public <T> CompletableFuture<T> executeAsync(WebServiceRequest<T> rawRequest) {
        WebServiceRequest<T> req = interceptor.onRequest(rawRequest);

        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create(req.getUrl()))
                .method(req.getMethod().name(),
                        req.getBody()
                                .map(body -> HttpRequest.BodyPublishers.ofString(
                                        toUnchecked((CheckedSupplier<String>) () -> converter.serialize(body))))
                                .orElse(HttpRequest.BodyPublishers.noBody()));

        req.getHeaders().forEach(builder::header);

        return client.sendAsync(builder.build(),
                        HttpResponse.BodyHandlers.ofString())
                .thenApply((Function<? super HttpResponse<String>, ? extends T>) httpResp -> {
                    WebServiceResponse wrapped = interceptor
                            .onResponse(req, new SimpleResponse(httpResp));

                    if (wrapped.getStatusCode() < 200 ||
                            wrapped.getStatusCode() >= 300) {
                        throw new RuntimeException(new HttpException(
                                wrapped.getStatusCode(), wrapped.getBodyString()));
                    }
                    try {
                        return converter.deserialize(wrapped.getBodyString(),
                                req.getResponseType());
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    private static <T> T toUnchecked(CheckedSupplier<T> supplier) {
        try {
            return supplier.get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @FunctionalInterface
    private interface CheckedSupplier<T> {
        T get() throws Exception;
    }
}
