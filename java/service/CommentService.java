package service;

import java.util.ArrayList;

import dao.CommentDAO;
import vo.Comment;

public class CommentService {
	public static void write(Comment comment) {
		CommentDAO dao = CommentDAO.getInstance();
		dao.write_comment(comment);
	}
	public static void modify(Comment comment) {
		CommentDAO dao = CommentDAO.getInstance();
		dao.update_comment(comment);
	}
	public static void delete(Comment comment) {
		CommentDAO dao = CommentDAO.getInstance();
		dao.delete_comment(comment);
	}
	public static ArrayList<ArrayList<Comment>> getCommentList_v2(int pid) throws Exception{
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
