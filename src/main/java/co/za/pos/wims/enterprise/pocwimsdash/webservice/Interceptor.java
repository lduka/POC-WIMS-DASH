package co.za.pos.wims.enterprise.pocwimsdash.webservice;

public interface Interceptor {
    <T> WebServiceRequest<T> onRequest(WebServiceRequest<T> request);
    <T> WebServiceResponse onResponse(WebServiceRequest<T> request,
                                      WebServiceResponse response);
}

