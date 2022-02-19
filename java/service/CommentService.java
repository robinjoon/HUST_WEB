package service;

import java.util.ArrayList;

import dao.CommentDAO;
import vo.Comment;

public class CommentService {
	public static boolean write(Comment comment) {
		CommentDAO dao = CommentDAO.getInstance();
		return dao.write_comment(comment);
	}
	public static boolean modify(Comment comment) {
		CommentDAO dao = CommentDAO.getInstance();
		return dao.update_comment(comment);
	}
	public static boolean delete(Comment comment) {
		CommentDAO dao = CommentDAO.getInstance();
		return dao.delete_comment(comment);
	}
	public static ArrayList<ArrayList<Comment>> getCommentList_v2(int pid){
		CommentDAO dao = CommentDAO.getInstance();
		return dao.getCommentList_v2(pid);
	}
	public static ArrayList<Comment> getMyComments(String id){
		CommentDAO dao = CommentDAO.getInstance();
		return dao.getMyComments(id);
	}
	
	public static Comment getComment(long cid){
		CommentDAO dao = CommentDAO.getInstance();
		return dao.getComment(cid);
	}
}
