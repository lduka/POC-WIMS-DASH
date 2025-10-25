package co.za.pos.wims.enterprise.pocwimsdash.webservice.error;

public class WebServiceException extends Exception {
    public WebServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}

