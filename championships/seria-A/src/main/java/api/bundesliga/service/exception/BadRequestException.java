package api.bundesliga.service.exception;

public class BadRequestException extends RuntimeException {
  public BadRequestException(Exception e) {
    super(e);
  }
    public BadRequestException(String message) {
        super(message);
    }
}
