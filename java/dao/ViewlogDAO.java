package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;

import db.DB;
import vo.Viewlog;

public class ViewlogDAO {

	private static ViewlogDAO dao;
	
	private ViewlogDAO() {}
	
	public static ViewlogDAO getinstance() {
		if(dao==null) {
			dao = new ViewlogDAO();
		}
		return dao;
	}
	
	public boolean logging(Viewlog view) {
		Connection conn = DB.getConnection();
		PreparedStatement pstmt = null;
		try {
			String sql = "insert into viewlogs(id,pid) values(?,?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, view.getId());
			pstmt.setInt(2, view.getPid());
			if(pstmt.executeUpdate()==1) {
				return true;
			}else {
				return false;
			}
		}catch(Exception e) {
			return false;
		}
	}
	
	public boolean deletelogs() {
		Connection conn = DB.getConnection();
		PreparedStatement pstmt = null;
		try {
			String sql = "delete from viewlogs where timestampdiff(second,date,current_timestamp()) > 86400";
			pstmt = conn.prepareStatement(sql);
			if(pstmt.executeUpdate()==1) {
				return true;
			}else {
				return false;
			}
		}catch(Exception e) {
			return false;
		}
	}
}
