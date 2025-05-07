package api.bundesliga.service.exception;

public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(Exception e) {
        super(e);
    }
    public EntityNotFoundException(String message) {
        super(message);
    }
}