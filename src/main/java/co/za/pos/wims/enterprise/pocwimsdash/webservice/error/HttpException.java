package co.za.pos.wims.enterprise.pocwimsdash.webservice.error;

public class HttpException extends WebServiceException {
    private final int statusCode;

    public HttpException(int statusCode, String message) {
        super("HTTP " + statusCode + ": " + message, null);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
