package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedList;

import db.DB;
import vo.Board;
import vo.Permission;

public class BoardDAO {
	private static BoardDAO dao;
	
	private BoardDAO() {
	}
	
	public static BoardDAO getInstance() {
		if(dao==null) {
			dao = new BoardDAO();
		}
		return dao;
	}
	
	public boolean create_board(Board board) { // 게시판 생성
		Connection conn = DB.getConnection();
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement("insert into boardlist value(?,?,?,?,?,?)");
			pstmt.setString(1, board.getBoard_name());
			pstmt.setInt(2, Permission.permissionToInt(board.getWritePermission()));
			pstmt.setInt(3, Permission.permissionToInt(board.getReadPermission()));
			pstmt.setInt(4, Permission.permissionToInt(board.getManagePermission()));
			pstmt.setInt(5, Permission.permissionToInt(board.getCommentPermission()));
			pstmt.setString(6, board.getBoard_description());
			if(pstmt.executeUpdate()==1) {
				return true;
			}else {
				return false;
			}
		}catch(Exception e) {
			e.printStackTrace();;
			return false;
		}
	}
	
	public boolean delete_board(Board board) { // 게시판 삭제, 게시판삭제시 게시글 고려X 수정필요
		Connection conn = DB.getConnection();
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement("delete from boardlist where board_name = ?");
			pstmt.setString(1, board.getBoard_name());
			if(pstmt.executeUpdate()==1) {
				return true;
			}else
				return false;
		}catch(Exception e) {
			e.printStackTrace();;
			return false;
		}
	}
	
	public boolean update_board(Board board) { // db에는 퍼미션의 타입이 tinyint(4)로 되어있음. 문제소지있으니 통일 필요.
		Connection conn = DB.getConnection();
		PreparedStatement pstmt = null;
		try {
			String sql = "update boardlist set read_permission = ?,write_permission = ?,manage_permission = ?,comment_permission=?, board_description = ? "
					+ "where board_name = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, Permission.permissionToInt(board.getReadPermission()));
			pstmt.setInt(2, Permission.permissionToInt(board.getWritePermission()));
			pstmt.setInt(3, Permission.permissionToInt(board.getManagePermission()));
			pstmt.setInt(4, Permission.permissionToInt(board.getCommentPermission()));
			pstmt.setString(5, board.getBoard_description());
			pstmt.setString(6, board.getBoard_name());
			
			if(pstmt.executeUpdate()==1) {
				return true;
			}else {
				return false;
			}
			
		}catch(Exception e) {
			e.printStackTrace();;
			return false;
		}
	}
	public Board getBoard(String board_name){
		Connection conn = DB.getConnection();
		PreparedStatement pstmt = null;
		try {
			String sql = "select * from boardlist where board_name = ?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1,board_name);
			
			ResultSet rs = pstmt.executeQuery();
			Board board = new Board();
			while(rs.next()) {
				board = new Board(rs);
			}
			return board;
		}catch(Exception e) {
			e.printStackTrace();;
			return new Board();
		}
	}
	public Board getBoard(int postId) {
		Connection conn = DB.getConnection();
		PreparedStatement pstmt = null;
		try {
			String sql = "select * from boardlist where board_name = (select board_name from posts where pid = ?)";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1,postId);
			
			ResultSet rs = pstmt.executeQuery();
			Board board = new Board();
			while(rs.next()) {
				board = new Board(rs);
			}
			return board;
		}catch(Exception e) {
			e.printStackTrace();;
			return new Board();
		}
	}
	public LinkedList<Board> getBoardlist(int per){
		LinkedList<Board> boardlist = new LinkedList<Board>();
		Connection conn = DB.getConnection();
		PreparedStatement pstmt = null;
		try {
			String sql = "select * from boardlist where read_permission <= ?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, per);
			
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				Board board = new Board(rs);
				boardlist.add(board);
			}
			return boardlist;
		}catch(Exception e) {
			e.printStackTrace();;
			return null;
		}
	}
}
