package pojo;

public class ResponseError {

    private int status;
    private String[] messages;

    public ResponseError(int status, String message) {
        this.status = status;
        this.messages = new String[]{message};
    }

    public ResponseError(int status, String[] messages) {
        this.status = status;
        this.messages = messages;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String[] getMessages() {
        return messages;
    }

    public void setMessages(String[] messages) {
        this.messages = messages;
    }

    public static ResponseError error400() {
        return new ResponseError(400, "Poslan je bil neveljaven zahtevek.");
    }

    public static ResponseError errorIdAlreadyExists() {
        return new ResponseError(400, "Id že obstaja.");
    }

    public static ResponseError error404() {
        return new ResponseError(404, "Zahtevek ni vrnil nobenega rezultata.");
    }

    public static ResponseError error500() {
        return new ResponseError(500, "Napaka na strežniku.");
    }

}
