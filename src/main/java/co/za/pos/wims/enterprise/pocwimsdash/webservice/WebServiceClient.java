package co.za.pos.wims.enterprise.pocwimsdash.webservice;



import co.za.pos.wims.enterprise.pocwimsdash.webservice.error.WebServiceException;

import java.util.concurrent.CompletableFuture;

public interface WebServiceClient {
    <T> T execute(WebServiceRequest<T> request) throws WebServiceException;
    <T> CompletableFuture<T> executeAsync(WebServiceRequest<T> request);
}
