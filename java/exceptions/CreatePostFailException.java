package exceptions;

public class CreatePostFailException extends RuntimeException {
	public CreatePostFailException(String msg) {
		super(msg);
	}
}
