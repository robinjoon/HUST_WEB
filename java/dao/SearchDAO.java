package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import db.DB;
import vo.*;
public class SearchDAO {
	private static SearchDAO dao;
	
	private SearchDAO() {
	}
	
	public static SearchDAO getInstance() {
		if(dao==null) {
			dao = new SearchDAO();
		}
		return dao;
	}
	
	public ArrayList<Post> searchPost(String search_word,String search_target, int permission, String sort){
		ArrayList<Post> search_result = new ArrayList<Post>();
		Connection conn = DB.getConnection();
		PreparedStatement pstmt = null;
		search_word = "%"+search_word+"%";
		try {
			String sql = "select posts.*,boardlist.read_permission from posts "
					+ "join boardlist on posts.board_name = boardlist.board_name "
					+ "where posts."+search_target+" like ? order by sort desc ";
			sql = sql.replace("sort", sort);
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, search_word);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				if(rs.getInt("read_permission")>permission || rs.getBoolean("blind")) {
					continue;
				}else {
					Post post = new Post(rs);
					search_result.add(post);
				}
			}
			return search_result;
		}catch(Exception e) {
			e.printStackTrace();;
			return null;
		}
	}
	public ArrayList<Post> searchPost_from_board(String board_name,String search_word,String search_target, int permission, String sort){
		ArrayList<Post> search_result = new ArrayList<Post>();
		Connection conn = DB.getConnection();
		PreparedStatement pstmt = null;
		search_word = "%"+search_word+"%";
		try {
			String sql = "select posts.*,boardlist.read_permission from posts "
					+ "join boardlist on posts.board_name = boardlist.board_name "
					+ "where posts."+search_target+" like ? and posts.board_name = ? order by sort desc ";
			sql = sql.replace("sort", sort);
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, search_word);
			pstmt.setString(2, board_name);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				if(rs.getInt("read_permission")>permission || rs.getBoolean("blind")) {
					continue;
				}else {
					Post post = new Post(rs);
					search_result.add(post);
				}
			}
			return search_result;
		}catch(Exception e) {
			e.printStackTrace();;
			return null;
		}
	}
}
