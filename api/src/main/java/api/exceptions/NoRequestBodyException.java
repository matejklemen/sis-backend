package api.exceptions;

public class NoRequestBodyException extends RuntimeException {

    public NoRequestBodyException() {
        super("Request body is empty.");
    }
}
