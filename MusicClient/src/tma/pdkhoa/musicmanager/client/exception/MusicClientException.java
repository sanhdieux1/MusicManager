package tma.pdkhoa.musicmanager.client.exception;

public class MusicClientException extends Exception {
    private String message;
    private String errorcode;
    
    public MusicClientException(String message, String errorcode) {
        super();
        this.message = message;
        this.errorcode = errorcode;
    }

    
    public MusicClientException(String message, Throwable e) {
        super(e);
        this.message = message;
    }


    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getErrorcode() {
        return errorcode;
    }

    public void setErrorcode(String errorcode) {
        this.errorcode = errorcode;
    }

}
