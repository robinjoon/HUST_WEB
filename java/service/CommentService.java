package service;

import java.util.ArrayList;

import dao.CommentDAO;
import vo.CommentVO;

public class CommentService {
	public static boolean write(CommentVO comment) {
		CommentDAO dao = CommentDAO.getInstance();
		return dao.write_comment(comment);
	}
	public static boolean modify(CommentVO comment) {
		CommentDAO dao = CommentDAO.getInstance();
		return dao.update_comment(comment);
	}
	public static boolean delete(CommentVO comment) {
		CommentDAO dao = CommentDAO.getInstance();
		return dao.delete_comment(comment);
	}
	public static ArrayList<ArrayList<CommentVO>> getCommentList_v2(int pid){
		CommentDAO dao = CommentDAO.getInstance();
		return dao.getCommentList_v2(pid);
	}
	public static ArrayList<CommentVO> getMyComments(String id){
		CommentDAO dao = CommentDAO.getInstance();
		return dao.getMyComments(id);
	}
	
	public static CommentVO getComment(long cid){
		CommentDAO dao = CommentDAO.getInstance();
		return dao.getComment(cid);
	}
}
