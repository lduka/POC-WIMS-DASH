package co.za.pos.wims.enterprise.pocwimsdash.webservice;
import java.util.List;

public class InterceptorChain implements Interceptor {
    private final List<Interceptor> interceptors;

    public InterceptorChain(List<Interceptor> interceptors) {
        this.interceptors = interceptors;
    }

    @Override
    public <T> WebServiceRequest<T> onRequest(WebServiceRequest<T> request) {
        WebServiceRequest<T> current = request;
        for (Interceptor i : interceptors) {
            current = i.onRequest(current);
        }
        return current;
    }

    @Override
    public <T> WebServiceResponse onResponse(WebServiceRequest<T> request,
                                             WebServiceResponse response) {
        WebServiceResponse current = response;
        for (Interceptor i : interceptors) {
            current = i.onResponse(request, current);
        }
        return current;
    }
}
