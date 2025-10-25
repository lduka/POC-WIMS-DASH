package co.za.pos.wims.enterprise.pocwimsdash.webservice;

import java.util.Map;
import java.util.Optional;

/**
 * Defines the minimal contract for an HTTP request used by the web service layer.
 * Implementations provide request metadata (URL, method, headers, body) and the
 * expected response type for (de)serialization.
 *
 * @param <T> The Java type expected from the HTTP response body after conversion.
 */
public interface WebServiceRequest<T> {
    /** @return Absolute or resolved URL to invoke. */
    String getUrl();
    /** @return HTTP method to use for the request. */
    HttpMethod getMethod();
    /** @return Request headers to send with the call. */
    Map<String, String> getHeaders();
    /** @return Optional request body; empty for methods like GET. */
    Optional<Object> getBody();
    /** @return Class of the expected response payload for conversion. */
    Class<T> getResponseType();
}

