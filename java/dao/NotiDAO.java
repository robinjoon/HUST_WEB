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
		try {
			String sql = "select * from noti where nid = ?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, nid);
			Noti noti = new Noti();
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				noti = new Noti(rs);
			}
			return noti;
		}catch(Exception e) {
			e.printStackTrace();
			return new Noti();
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
				Noti noti = new Noti(rs);
				list.add(noti);
			}
			return list;
		}catch(Exception e) {
			e.printStackTrace();
			return list;
		}
	}
}
