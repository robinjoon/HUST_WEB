package exceptions;

public class LoginFailException extends RuntimeException {
	public LoginFailException(String msg) {
		super(msg);
	}
}
