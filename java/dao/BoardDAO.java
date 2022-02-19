package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.LinkedList;

import db.DB;
import vo.BoardVO;

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
	
	public boolean create_board(BoardVO board) { // 게시판 생성
		Connection conn = DB.getConnection();
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement("insert into boardlist value(?,?,?,?,?,?)");
			pstmt.setString(1, board.getBoard_name());
			pstmt.setInt(2, board.getWrite_permission());
			pstmt.setInt(3, board.getRead_permission());
			pstmt.setInt(4, board.getManage_permission());
			pstmt.setInt(5, board.getComment_permission());
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
	
	public boolean delete_board(BoardVO board) { // 게시판 삭제, 게시판삭제시 게시글 고려X 수정필요
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
	
	public boolean update_board(BoardVO board, int rp,int wp,int mp,int cp) { // db에는 퍼미션의 타입이 tinyint(4)로 되어있음. 문제소지있으니 통일 필요.
		Connection conn = DB.getConnection();
		PreparedStatement pstmt = null;
		try {
			String sql = "update boardlist set read_permission = ?,write_permission = ?,manage_permission = ?,comment_permission=?, board_description = ? "
					+ "where board_name = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, rp);
			pstmt.setInt(2, wp);
			pstmt.setInt(3, mp);
			pstmt.setInt(4, cp);
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
	public BoardVO getBoard(String board_name){
		Connection conn = DB.getConnection();
		PreparedStatement pstmt = null;
		try {
			String sql = "select * from boardlist where board_name = ?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1,board_name);
			
			ResultSet rs = pstmt.executeQuery();
			BoardVO board = new BoardVO();
			while(rs.next()) {
				board.setBoard_name(rs.getString("board_name"));
				board.setRead_permission(rs.getInt("read_permission"));
				board.setWrite_permission(rs.getInt("write_permission"));
				board.setManage_permission(rs.getInt("manage_permission"));
				board.setComment_permission(rs.getInt("comment_permission"));
				board.setBoard_description(rs.getString("board_description"));
			}
			return board;
		}catch(Exception e) {
			e.printStackTrace();;
			return null;
		}
	}
	public LinkedList<BoardVO> getBoardlist(int per){
		LinkedList<BoardVO> boardlist = new LinkedList<BoardVO>();
		Connection conn = DB.getConnection();
		PreparedStatement pstmt = null;
		try {
			String sql = "select * from boardlist where read_permission <= ?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, per);
			
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				BoardVO board = new BoardVO();
				board.setBoard_name(rs.getString("board_name"));
				board.setRead_permission(rs.getInt("read_permission"));
				board.setWrite_permission(rs.getInt("write_permission"));
				board.setManage_permission(rs.getInt("manage_permission"));
				board.setComment_permission(rs.getInt("comment_permission"));
				board.setBoard_description(rs.getString("board_description"));
				boardlist.add(board);
			}
			return boardlist;
		}catch(Exception e) {
			e.printStackTrace();;
			return null;
		}
	}
	
	public HashMap<String,Integer> getPermissions_by_name(String board_name){ // 없는 게시판이면 8을 반환
		HashMap<String,Integer> per_map = new HashMap<String,Integer>();
		per_map.put("read", 8);
		per_map.put("write", 8);
		per_map.put("manage", 8);
		per_map.put("comment", 8);
		Connection conn = DB.getConnection();
		PreparedStatement pstmt = null;
		try {
			String sql = "select read_permission,write_permission,manage_permission,comment_permission from boardlist where board_name = ?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1,board_name);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				per_map.replace("read", rs.getInt(1));
				per_map.replace("write",rs.getInt(2));
				per_map.replace("manage", rs.getInt(3));
				per_map.replace("comment", rs.getInt(4));
			}
			return per_map;
		}catch(Exception e) {
			System.err.println(e);
			return per_map;
		}
	}
	
	public HashMap<String,Integer> getPermissions_by_Pid(int pid){ // 없는 게시판이면 8을 반환
		HashMap<String,Integer> per_map = new HashMap<String,Integer>();
		per_map.put("read", 8);
		per_map.put("write", 8);
		per_map.put("manage", 8);
		per_map.put("comment", 8);
		Connection conn = DB.getConnection();
		PreparedStatement pstmt = null;
		try {
			String sql = "select read_permission,write_permission,manage_permission,comment_permission from boardlist "
					+ "where board_name = (select board_name from posts where pid=?)";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1,pid);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				per_map.replace("read", rs.getInt(1));
				per_map.replace("write",rs.getInt(2));
				per_map.replace("manage", rs.getInt(3));
				per_map.replace("comment", rs.getInt(4));
			}
			return per_map;
		}catch(Exception e) {
			System.err.println(e);
			return per_map;
		}
	}
}
