package exceptions;

public class InvalidPostException extends RuntimeException {
	public InvalidPostException(String msg) {
		super(msg);
	}
}
