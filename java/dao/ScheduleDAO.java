package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import db.DB;
import vo.Schedule;

public class ScheduleDAO {
	private static ScheduleDAO dao;
	private ScheduleDAO() {}
	public static ScheduleDAO getInstance() {
		if(dao==null) {
			dao = new ScheduleDAO();
		}
		return dao;
	}
	public boolean create(Schedule schedule) {
		Connection conn = DB.getConnection();
		PreparedStatement pstmt = null;
		try {
			String sql = "insert into schedule(s_date,title,place,content,is_ob) value(?,?,?,?,?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setDate(1, schedule.getS_date());
			pstmt.setString(2, schedule.getTitle());
			pstmt.setString(3,schedule.getPlace());
			pstmt.setString(4, schedule.getContent());
			pstmt.setBoolean(5, schedule.is_ob());
			pstmt.executeUpdate();
			return true;
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean update(Schedule schedule) {
		if(delete(schedule)) {
			return create(schedule);
		}else {
			return false;
		}
	}
	
	public boolean delete(Schedule schedule) {
		Connection conn = DB.getConnection();
		PreparedStatement pstmt = null;
		try {
			String sql = "delete from schedule where sid = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, schedule.getSid());
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
	
	public ArrayList<Schedule> getScheduleList(String what, int year, int month){
		Connection conn = DB.getConnection();
		PreparedStatement pstmt = null;
		ArrayList<Schedule> list = new ArrayList<Schedule> ();
		try {
			String sql = "select * from schedule where year(s_date)=? and month(s_date)=? and is_ob = ?";
			if(what.contentEquals("all")) {
				sql = sql.replace("and is_ob = ?", "");
			}
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, year);
			pstmt.setInt(2, month);
			System.out.println(what);
			if(what.contentEquals("ob")) {
				pstmt.setBoolean(3, true);
			}else if(what.contentEquals("yb")) {
				pstmt.setBoolean(3, false);
			}
			
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				Schedule sc = new Schedule();
				sc.setSid(rs.getInt("sid"));
				sc.setS_date(rs.getDate("s_date"));
				sc.setTitle(rs.getString("title"));
				sc.setPlace(rs.getString("place"));
				sc.setContent(rs.getString("content"));
				sc.set_ob(rs.getBoolean("is_ob"));
				list.add(sc);
			}
			return list;
			
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public Schedule getSchedule(Date date) {
		Connection conn = DB.getConnection();
		PreparedStatement pstmt = null;
		try {
			String sql = "select * from schedule where s_date = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setDate(1, date);
			ResultSet rs = pstmt.executeQuery();
			Schedule sc = new Schedule();
			while(rs.next()) {
				sc.setSid(rs.getInt("sid"));
				sc.setS_date(rs.getDate("s_date"));
				sc.setTitle(rs.getString("title"));
				sc.setPlace(rs.getString("place"));
				sc.setContent(rs.getString("content"));
				sc.set_ob(rs.getBoolean("is_ob"));
			}
			return sc;
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public Schedule getSchedule2(int sid) {
		Connection conn = DB.getConnection();
		PreparedStatement pstmt = null;
		try {
			String sql = "select * from schedule where sid = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, sid);
			ResultSet rs = pstmt.executeQuery();
			Schedule sc = new Schedule();
			while(rs.next()) {
				sc.setSid(rs.getInt("sid"));
				sc.setS_date(rs.getDate("s_date"));
				sc.setTitle(rs.getString("title"));
				sc.setPlace(rs.getString("place"));
				sc.setContent(rs.getString("content"));
				sc.set_ob(rs.getBoolean("is_ob"));
			}
			return sc;
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public Schedule nextSchedule(Date date) {
		Connection conn = DB.getConnection();
		PreparedStatement pstmt = null;
		try {
			String sql = "select * from schedule where s_date > ? limit 1";
			pstmt = conn.prepareStatement(sql);
			pstmt.setDate(1, date);
			ResultSet rs = pstmt.executeQuery();
			Schedule sc = new Schedule();
			while(rs.next()) {
				sc.setSid(rs.getInt("sid"));
				sc.setS_date(rs.getDate("s_date"));
				sc.setTitle(rs.getString("title"));
				sc.setPlace(rs.getString("place"));
				sc.setContent(rs.getString("content"));
				sc.set_ob(rs.getBoolean("is_ob"));
			}
			return sc;
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public Schedule preSchedule(Date date) {
		Connection conn = DB.getConnection();
		PreparedStatement pstmt = null;
		try {
			String sql = "select * from schedule where s_date < ? limit 1";
			pstmt = conn.prepareStatement(sql);
			pstmt.setDate(1, date);
			ResultSet rs = pstmt.executeQuery();
			Schedule sc = new Schedule();
			while(rs.next()) {
				sc.setSid(rs.getInt("sid"));
				sc.setS_date(rs.getDate("s_date"));
				sc.setTitle(rs.getString("title"));
				sc.setPlace(rs.getString("place"));
				sc.setContent(rs.getString("content"));
				sc.set_ob(rs.getBoolean("is_ob"));
			}
			return sc;
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public boolean is_ob(Schedule schedule) {
		Connection conn = DB.getConnection();
		PreparedStatement pstmt = null;
		try {
			boolean ret = false;
			String sql = "select is_ob from schedule where s_date = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setDate(1, schedule.getS_date());
			
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				ret = rs.getBoolean("is_ob");
			}
			return ret;
			
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
}
