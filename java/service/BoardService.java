package service;


import java.util.HashMap;

import dao.BoardDAO;
import vo.Board;

public class BoardService {
	public static boolean createboard(Board board) {
		BoardDAO dao = BoardDAO.getInstance();
		return dao.create_board(board);
	}
	public static boolean deleteBoard(Board board) {
		BoardDAO dao = BoardDAO.getInstance();
		return dao.delete_board(board);
	}
	public static boolean updateboard(Board board) {
		BoardDAO dao = BoardDAO.getInstance();
		return dao.update_board(board);
	}
	public static HashMap<String,Integer> getPermissions_by_Pid(int pid){
		BoardDAO dao = BoardDAO.getInstance();
		return dao.getPermissions_by_Pid(pid);
	}
	public static HashMap<String,Integer> getPermissions_by_name(String board_name){
		BoardDAO dao = BoardDAO.getInstance();
		return dao.getPermissions_by_name(board_name);
	}
	public static Board getBoard(String board_name){
		BoardDAO dao = BoardDAO.getInstance();
		return dao.getBoard(board_name);
	}
}
