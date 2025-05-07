package api.bundesliga.service.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(Exception e) {
        super(e);
    }
    public ResourceNotFoundException(String message) {
        super(message);
    }

}
