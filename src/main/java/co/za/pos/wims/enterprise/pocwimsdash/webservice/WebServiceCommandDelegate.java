package co.za.pos.wims.enterprise.pocwimsdash.webservice;

import java.io.IOException;

import static co.za.pos.wims.enterprise.pocwimsdash.webservice.HttpMethod.DELETE;
import static co.za.pos.wims.enterprise.pocwimsdash.webservice.HttpMethod.GET;
import static co.za.pos.wims.enterprise.pocwimsdash.webservice.HttpMethod.POST;
import static co.za.pos.wims.enterprise.pocwimsdash.webservice.HttpMethod.PUT;

/**
 * Backward-compatible alias intentionally matching the misspelled name in the request: ComandDelagate.
 * Delegates to CommandDelegate.
 */
public final class WebServiceCommandDelegate {
    private WebServiceCommandDelegate() {}

    // Generic execute for all call types. The caller's assignment target determines R.
    @SuppressWarnings("unchecked")
    public static <R> R execute(WebServiceOperator<?> operator) {
        if (operator == null) return null;
        try {
            switch (operator.getEndpoint().getMethod()) {
                case GET -> {
                    if (operator.isSingleResult()) {
                        String url = operator.buildUrlWithParams();
                        return (R) operator.DO_GET_ONE(url);
                    } else {
                        return (R) operator.DO_GET();
                    }
                }
                case POST -> {
                    if (operator instanceof WebServiceOperator<?> op && op.hasBody()) {
                        return (R) op.DO_POST_JSON(op.getBody());
                    }
                    return (R) operator.DO_POST(java.net.http.HttpRequest.BodyPublishers.noBody());
                }
                case PUT -> {
                    // Use operator's internal execution for PUT
                    operator.execute();
                    return null; // no response expected
                }
                case DELETE -> {
                    // Use operator's internal execution for DELETE
                    operator.execute();
                    return null; // no response expected
                }
                default -> {
                    return null;
                }
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Failed to execute web service command", e);
        }
    }

    // New helper to POST with a JSON body
    public static java.net.http.HttpResponse<String> postJson(WebServiceOperator<?> operator, String jsonBody) {
        if (operator == null) throw new IllegalArgumentException("operator is null");
        try {
            return operator.DO_POST(java.net.http.HttpRequest.BodyPublishers.ofString(jsonBody == null ? "" : jsonBody));
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Failed to execute POST request", e);
        }
    }
}
