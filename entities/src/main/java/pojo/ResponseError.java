package pojo;

import javax.xml.ws.Response;

public class ResponseError {

    private int status;
    private String message;

    public ResponseError(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static ResponseError error400() {
        return new ResponseError(400, "Poslan je bil neveljaven zahtevek.");
    }

    public static ResponseError error404() {
        return new ResponseError(404, "Zahtevek ni vrnil nobenega rezultata.");
    }

    public static ResponseError error500() {
        return new ResponseError(500, "Napaka na strežniku.");
    }

}
