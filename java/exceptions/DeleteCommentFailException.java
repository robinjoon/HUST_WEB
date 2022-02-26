package exceptions;

public class DeleteCommentFailException extends RuntimeException {
	public DeleteCommentFailException(String msg) {
		super(msg);
	}
}
