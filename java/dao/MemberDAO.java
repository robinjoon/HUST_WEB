package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Random;
import tools.Secure;
import db.DB;
import vo.Member;
import vo.Permission;

public class MemberDAO {
	private static MemberDAO dao;
	
	private MemberDAO() {}
	
	public static MemberDAO getInstance() {
		if(dao==null) {
			dao = new MemberDAO();
		}
		return dao;
	}
	
	public boolean join_member(Member member) { //보안 처리 필요
		Connection conn = DB.getConnection();
		PreparedStatement pstmt = null;
		try {
			String sql = "insert into member(id,pw,name,birthY,admissionY,joinY,"
					+ "school_year,phone,email,scholastic,interest,address,address_now,etc,solved_prob"
					+ ",mypost_comment_noti_allow,mycomment_comment_noti_allow,call_noti_allow)";
			sql = sql +" values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, member.getId());
			pstmt.setString(2, Secure.sha256(Secure.MD5(member.getPw()).toUpperCase()));
			pstmt.setString(3, member.getName());
			pstmt.setInt(4, member.getBirthY());
			pstmt.setInt(5, member.getAdmissionY());
			pstmt.setInt(6,member.getJoinY());
			pstmt.setInt(7, member.getSchool_year());
			pstmt.setString(8, member.getPhone());
			pstmt.setString(9,member.getEmail());
			pstmt.setString(10, member.getScholastic());
			pstmt.setString(11, member.getInterest());
			pstmt.setString(12, member.getAddress());
			pstmt.setString(13, member.getAddress_now());
			pstmt.setString(14, member.getEtc());
			pstmt.setString(15, "");
			pstmt.setBoolean(16, member.isMypost_comment_noti_allow());
			pstmt.setBoolean(17, member.isMycomment_comment_noti_allow());
			pstmt.setBoolean(18, member.isCall_noti_allow());
			
			pstmt.executeUpdate();
			return true;
		}catch(Exception e) {
			System.err.println(e);
			return false;
		}
	}
	public boolean update_member(Member member) { // 비밀번호 변경없이 타 정보 수정만 할 때 처리 필요
		Connection conn = DB.getConnection();
		PreparedStatement pstmt = null;
		try {
			String sql = "update member set pw = ?,name = ?,birthY = ?,admissionY = ?,joinY = ?,";
			sql = sql + "school_year = ?,phone = ?,email = ?,scholastic = ?,interest = ?,address = ?,address_now = ?,etc = ?";
			sql = sql + ",mypost_comment_noti_allow = ?, mycomment_comment_noti_allow = ?,call_noti_allow = ?";
			sql = sql +" where id = ?";
			if(member.getPw().isBlank()||member.getPw().isEmpty()||member.getPw()==null) {
				sql = sql.replace("pw = ?,", "");
			}
			pstmt = conn.prepareStatement(sql);
			if(member.getPw().isBlank()||member.getPw().isEmpty()||member.getPw()==null) {
				pstmt.setString(1, member.getName());
				pstmt.setInt(2, member.getBirthY());
				pstmt.setInt(3, member.getAdmissionY());
				pstmt.setInt(4,member.getJoinY());
				pstmt.setInt(5, member.getSchool_year());
				pstmt.setString(6, member.getPhone());
				pstmt.setString(7,member.getEmail());
				pstmt.setString(8, member.getScholastic());
				pstmt.setString(9, member.getInterest());
				pstmt.setString(10, member.getAddress());
				pstmt.setString(11, member.getAddress_now());
				pstmt.setString(12, member.getEtc());
				pstmt.setBoolean(13, member.isMypost_comment_noti_allow());
				pstmt.setBoolean(14, member.isMycomment_comment_noti_allow());
				pstmt.setBoolean(15, member.isCall_noti_allow());
				pstmt.setString(16, member.getId());
				
			}else {
				pstmt.setString(1, Secure.sha256(Secure.MD5(member.getPw()).toUpperCase()));
				pstmt.setString(2, member.getName());
				pstmt.setInt(3, member.getBirthY());
				pstmt.setInt(4, member.getAdmissionY());
				pstmt.setInt(5,member.getJoinY());
				pstmt.setInt(6, member.getSchool_year());
				pstmt.setString(7, member.getPhone());
				pstmt.setString(8,member.getEmail());
				pstmt.setString(9, member.getScholastic());
				pstmt.setString(10, member.getInterest());
				pstmt.setString(11, member.getAddress());
				pstmt.setString(12, member.getAddress_now());
				pstmt.setString(13, member.getEtc());
				pstmt.setBoolean(14, member.isMypost_comment_noti_allow());
				pstmt.setBoolean(15, member.isMycomment_comment_noti_allow());
				pstmt.setBoolean(16, member.isCall_noti_allow());
				pstmt.setString(17, member.getId());
			}
			pstmt.executeUpdate();
			return true;
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean delete_member(Member member) {
		Connection conn = DB.getConnection();
		PreparedStatement pstmt = null;
		try {
			String sql = "delete from member where id = ? and pw = ?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, member.getId());
			pstmt.setString(2, Secure.sha256(Secure.MD5(member.getPw()).toUpperCase()));
			
			pstmt.executeUpdate();
			return true;
		}catch(Exception e) {
			System.err.println(e);
			return false;
		}
	}
	
	public Member getMember(String id) { //멤버 한명 가져오기.
		Connection conn = DB.getConnection();
		PreparedStatement pstmt = null;
		Member member = new Member();
		try {
			String sql = "select * from member where id = ?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				member.setId(rs.getString("id"));
				member.setName(rs.getString("name"));
				member.setBirthY(rs.getInt("birthY"));
				member.setAdmissionY(rs.getInt("admissionY"));
				member.setJoinY(rs.getInt("joinY"));
				member.setSchool_year(rs.getInt("school_year"));
				member.setPhone(rs.getString("phone"));
				member.setEmail(rs.getString("email"));
				member.setScholastic(rs.getString("scholastic"));
				member.setInterest(rs.getString("interest"));
				member.setAddress(rs.getString("address"));
				member.setAddress_now(rs.getString("address_now"));
				member.setPermission(Permission.intToPermission(rs.getInt("permission")));
				member.setEtc(rs.getString("etc"));
				member.setProb_score(rs.getLong("prob_score"));
				member.setSolved_prob(rs.getString("solved_prob"));
				member.setMypost_comment_noti_allow(rs.getBoolean("mypost_comment_noti_allow"));
				member.setMycomment_comment_noti_allow(rs.getBoolean("mycomment_comment_noti_allow"));
				member.setCall_noti_allow(rs.getBoolean("call_noti_allow"));
			}
			return member;
		}catch(Exception e) {
			System.err.println(e);
			return null;
		}
	}
	
	public ArrayList<Member> getMemberList(String group){ //특정그룹(YB,OB) 멤버 전치 가져오기
		ArrayList<Member> memberlist = new ArrayList<Member>();
		Connection conn = DB.getConnection();
		PreparedStatement pstmt = null;
		
		try {
			String sql = "select * from member where scholastic = ?";
			if(!group.contentEquals("전체")) {
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, group);
			}else{
				sql = sql.replace(" where scholastic = ?", "");
				pstmt = conn.prepareStatement(sql);
			}
			
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				Member member = new Member();
				member.setId(rs.getString("id"));
				member.setName(rs.getString("name"));
				member.setBirthY(rs.getInt("birthY"));
				member.setAdmissionY(rs.getInt("admissionY"));
				member.setJoinY(rs.getInt("joinY"));
				member.setSchool_year(rs.getInt("school_year"));
				member.setPhone(rs.getString("phone"));
				member.setEmail(rs.getString("email"));
				member.setScholastic(rs.getString("scholastic"));
				member.setInterest(rs.getString("interest"));
				member.setAddress(rs.getString("address"));
				member.setAddress_now(rs.getString("address_now"));
				member.setPermission(Permission.intToPermission(rs.getInt("permission")));
				member.setEtc(rs.getString("etc"));
				member.setProb_score(rs.getLong("prob_score"));
				member.setSolved_prob(rs.getString("solved_prob"));
				member.setMypost_comment_noti_allow(rs.getBoolean("mypost_comment_noti_allow"));
				member.setMycomment_comment_noti_allow(rs.getBoolean("mycomment_comment_noti_allow"));
				member.setCall_noti_allow(rs.getBoolean("call_noti_allow"));
				memberlist.add(member);
			}
			return memberlist;
		}catch(Exception e) {
			System.err.println(e);
			return null;
		}
	}
	public ArrayList<Member> getMemberList(int permission){ //특정그룹(YB,OB) 멤버 전치 가져오기
		ArrayList<Member> memberlist = new ArrayList<Member>();
		Connection conn = DB.getConnection();
		PreparedStatement pstmt = null;
		try {
			String sql = "select * from member where permission = ?";
			if(permission!=7) {
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, permission);
			}else {
				sql = sql.replace(" where permission = ?", "");
				pstmt = conn.prepareStatement(sql);
			}
			
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				Member member = new Member();
				member.setId(rs.getString("id"));
				member.setName(rs.getString("name"));
				member.setBirthY(rs.getInt("birthY"));
				member.setAdmissionY(rs.getInt("admissionY"));
				member.setJoinY(rs.getInt("joinY"));
				member.setSchool_year(rs.getInt("school_year"));
				member.setPhone(rs.getString("phone"));
				member.setEmail(rs.getString("email"));
				member.setScholastic(rs.getString("scholastic"));
				member.setInterest(rs.getString("interest"));
				member.setAddress(rs.getString("address"));
				member.setAddress_now(rs.getString("address_now"));
				member.setPermission(Permission.intToPermission(rs.getInt("permission")));
				member.setEtc(rs.getString("etc"));
				member.setProb_score(rs.getLong("prob_score"));
				member.setSolved_prob(rs.getString("solved_prob"));
				member.setMypost_comment_noti_allow(rs.getBoolean("mypost_comment_noti_allow"));
				member.setMycomment_comment_noti_allow(rs.getBoolean("mycomment_comment_noti_allow"));
				member.setCall_noti_allow(rs.getBoolean("call_noti_allow"));
				memberlist.add(member);
			}
			return memberlist;
		}catch(Exception e) {
			System.err.println(e);
			return null;
		}
	}
	public ArrayList<Member> getMemberList(int permission,String group){ //특정그룹(YB,OB) 멤버 전체 가져오기
		ArrayList<Member> memberlist = new ArrayList<Member>();
		Connection conn = DB.getConnection();
		PreparedStatement pstmt = null;
		try {
			String sql = "select * from member where permission = ? and scholastic = ?";
			
			if(permission==7) {
				sql = sql.replace("permission = ?", "permission < ?");
			}
			if(group.contentEquals("전체")) {
				sql = sql.replace("and scholastic = ?", "");
			}
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, permission);
			if(!group.contentEquals("전체"))
				pstmt.setString(2,group);
			
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				Member member = new Member();
				member.setId(rs.getString("id"));
				member.setName(rs.getString("name"));
				member.setBirthY(rs.getInt("birthY"));
				member.setAdmissionY(rs.getInt("admissionY"));
				member.setJoinY(rs.getInt("joinY"));
				member.setSchool_year(rs.getInt("school_year"));
				member.setPhone(rs.getString("phone"));
				member.setEmail(rs.getString("email"));
				member.setScholastic(rs.getString("scholastic"));
				member.setInterest(rs.getString("interest"));
				member.setAddress(rs.getString("address"));
				member.setAddress_now(rs.getString("address_now"));
				member.setPermission(Permission.intToPermission(rs.getInt("permission")));
				member.setEtc(rs.getString("etc"));
				member.setProb_score(rs.getLong("prob_score"));
				member.setSolved_prob(rs.getString("solved_prob"));
				member.setMypost_comment_noti_allow(rs.getBoolean("mypost_comment_noti_allow"));
				member.setMycomment_comment_noti_allow(rs.getBoolean("mycomment_comment_noti_allow"));
				member.setCall_noti_allow(rs.getBoolean("call_noti_allow"));
				memberlist.add(member);
			}
			return memberlist;
		}catch(Exception e) {
			System.err.println(e);
			return null;
		}
	}
	
	public ArrayList<Member> getMemberList(int permission, String input, String notuse){ //태그가능한 멤버들중 사용자의 입력이 포함된 회원목록 가져오기
		ArrayList<Member> memberlist = new ArrayList<Member>();
		Connection conn = DB.getConnection();
		PreparedStatement pstmt = null;
		input = "%"+input+"%";
		System.out.println(input);
		try {
			String sql = "select * from member where permission >= ? and (id like ? or name like ?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, permission);
			pstmt.setString(2,input);
			pstmt.setString(3,input);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				Member member = new Member();
				member.setId(rs.getString("id"));
				member.setName(rs.getString("name"));
				member.setBirthY(rs.getInt("birthY"));
				member.setAdmissionY(rs.getInt("admissionY"));
				member.setJoinY(rs.getInt("joinY"));
				member.setSchool_year(rs.getInt("school_year"));
				member.setPhone(rs.getString("phone"));
				member.setEmail(rs.getString("email"));
				member.setScholastic(rs.getString("scholastic"));
				member.setInterest(rs.getString("interest"));
				member.setAddress(rs.getString("address"));
				member.setAddress_now(rs.getString("address_now"));
				member.setPermission(Permission.intToPermission(rs.getInt("permission")));
				member.setEtc(rs.getString("etc"));
				member.setProb_score(rs.getLong("prob_score"));
				member.setSolved_prob(rs.getString("solved_prob"));
				member.setMypost_comment_noti_allow(rs.getBoolean("mypost_comment_noti_allow"));
				member.setMycomment_comment_noti_allow(rs.getBoolean("mycomment_comment_noti_allow"));
				member.setCall_noti_allow(rs.getBoolean("call_noti_allow"));
				memberlist.add(member);
			}
			return memberlist;
		}catch(Exception e) {
			System.err.println(e);
			return null;
		}
	}	
	public int login_member(Member member) { //return == -1 ==> 로그인 실패. else 사용자 권한 반환 
		Connection conn = DB.getConnection();
		PreparedStatement pstmt = null;
		try {
			String sql = "select permission from member where id = ? and pw = ?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, member.getId());
			pstmt.setString(2, Secure.sha256(Secure.MD5(member.getPw()).toUpperCase()));
			
			ResultSet rs = pstmt.executeQuery();
			int per =-1;
			while(rs.next()) {
				per = rs.getInt("permission");
			}
			return per;
		}catch(Exception e) {
			e.printStackTrace();
			return -1;
		}
	}
	public boolean update_per(Member member,int per) {
		Connection conn = DB.getConnection();
		PreparedStatement pstmt = null;
		try {
			String sql = "update member set permission=? where id=?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, per);
			pstmt.setString(2, member.getId());
			
			pstmt.executeUpdate();
			return true;
		}catch(Exception e) {
			System.err.println(e);
			return false;
		}
	}
	
	private String randString() {
		StringBuffer sb = new StringBuffer();
		Random rand = new Random();
		for(int i=0;i<10;i++) {
			if(rand.nextBoolean()) {
				int a = rand.nextInt(3);
				if(a==1) {
					sb.append((char)(rand.nextInt(26)+65));
				}else if(a==2) {
					sb.append((char)(rand.nextInt(26)+97));
				}else {
					sb.append((char)(rand.nextInt(15)+33));
				}
			}else {
				sb.append(rand.nextInt(10));
			}
		}
		return sb.toString();
	}
	public String resetPw(String id) {
		String pw = randString();
		Connection conn = DB.getConnection();
		PreparedStatement pstmt = null;
		try {
			String sql = "update member set pw = ? where id = ?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, Secure.sha256(Secure.MD5(pw).toUpperCase()));
			pstmt.setString(2, id);
			pstmt.executeUpdate();
		}catch(Exception e) {
			System.err.println(e);
			return "";
		}
		return pw;
	}
	
	public boolean id_duplicate_check(String id) {
		Connection conn = DB.getConnection();
		PreparedStatement pstmt = null;
		try {
			String sql = "select count(*) from member where id = ?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			ResultSet rs = pstmt.executeQuery();
			int count =0;
			while(rs.next()){
				count = rs.getInt(1);
			}
			if(count==0) {
				return true;
			}else {
				return false;
			}
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
}
