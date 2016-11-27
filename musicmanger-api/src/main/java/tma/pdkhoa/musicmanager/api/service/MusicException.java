package tma.pdkhoa.musicmanager.api.service;

public class MusicException extends Exception {
    private String errorcode;
    private String message;
    
    public MusicException(String name, String errorcode, String message) {
        super(message);
        this.errorcode = errorcode;
        this.message = message;
    }
    
    public MusicException(String message, Throwable e) {
        super(e);
        
        this.message = message;
    }

    public String getErrorcode() {
        return errorcode;
    }
    public void setErrorcode(String errorcode) {
        this.errorcode = errorcode;
    }
    @Override
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    
}
