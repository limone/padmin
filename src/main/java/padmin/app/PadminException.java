package padmin.app;

public class PadminException extends Exception {

  public PadminException() {
    super();
  }

  public PadminException(String message) {
    super(message);
  }

  public PadminException(Throwable cause) {
    super(cause);
  }

  public PadminException(String message, Throwable cause) {
    super(message, cause);
  }

  public PadminException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}