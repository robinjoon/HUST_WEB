package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import db.DB;
import vo.Noti;

public class NotiDAO {
	
	private static NotiDAO dao;
	
	private NotiDAO() {}
	
	public static NotiDAO getInstance() {
		if(dao==null) {
			dao = new NotiDAO();
		}
		return dao;
	}
	
	public boolean send_noti(Noti noti) {
		Connection conn = DB.getConnection();
		PreparedStatement pstmt = null;
		
		try {
			String sql = "insert into noti(sender,receiver,title,body,url,date,notice) values(?,?,?,?,?,?,?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, noti.getSender());
			pstmt.setString(2, noti.getReceiver());
			pstmt.setString(3, noti.getTitle());
			pstmt.setString(4, noti.getBody());
			pstmt.setString(5, noti.getUrl());
			pstmt.setDate(6, noti.getDate());
			pstmt.setBoolean(7, noti.isNotice());
			if(pstmt.executeUpdate()==1) {
				return true;
			}else {
				return false;
			}
		}catch(Exception e) {
			System.err.println(e);
			return false;
		}
	}
	
	public boolean read_noti(Noti noti) {
		Connection conn = DB.getConnection();
		PreparedStatement pstmt = null;
		
		try {
			String sql = "UPDATE `hust`.`noti` SET `read`='1' WHERE  `nid`=?"; 
			// update noti set read=1 where nid=? 로 하니까 작동이 안됨.이유는 모르겠음. helidsql 이라는 프로그램을 이용해 작동하는 쿼리를 알아내 집어넣음.
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, noti.getNid());
			if(pstmt.executeUpdate()==1) {
				return true;
			}else {
				return false;
			}
		}catch(Exception e) {
			System.err.println(e);
			return false;
		}
	}
	
	public boolean delete_noti(Noti noti) {
		Connection conn = DB.getConnection();
		PreparedStatement pstmt = null;
		
		try {
			String sql = "DELETE FROM `hust`.`noti` WHERE  `nid`=?;";
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, noti.getNid());

			if(pstmt.executeUpdate()==1) {
				return true;
			}else {
				return false;
			}
		}catch(Exception e) {
			System.err.println(e);
			return false;
		}
	}
	
	public Noti getNoti(Long nid) {
		Connection conn = DB.getConnection();
		PreparedStatement pstmt = null;
		Noti noti = new Noti();
		try {
			String sql = "select * from noti where nid = ?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, nid);
			
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				noti.setNid(nid);
				noti.setSender(rs.getString("sender"));
				noti.setReceiver(rs.getString("receiver"));
				noti.setTitle(rs.getString("title"));
				noti.setBody(rs.getString("body"));
				noti.setUrl(rs.getString("url"));
				noti.setDate(rs.getDate("date"));
				noti.setRead(rs.getBoolean("read"));
				noti.setNotice(rs.getBoolean("notice"));
			}
			return noti;
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public ArrayList<Noti> getNotiList(String receiver) {
		Connection conn = DB.getConnection();
		PreparedStatement pstmt = null;
		ArrayList<Noti> list = new ArrayList<Noti>();
		try {
			String sql = "select * from noti where receiver = ?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1,receiver);
			
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				Noti noti = new Noti();
				noti.setNid(rs.getLong("nid"));
				noti.setSender(rs.getString("sender"));
				noti.setReceiver(rs.getString("receiver"));
				noti.setTitle(rs.getString("title"));
				noti.setBody(rs.getString("body"));
				noti.setUrl(rs.getString("url"));
				noti.setDate(rs.getDate("date"));
				noti.setRead(rs.getBoolean("read"));
				noti.setNotice(rs.getBoolean("notice"));
				list.add(noti);
			}
			return list;
		}catch(Exception e) {
			e.printStackTrace();
			return list;
		}
	}
}
