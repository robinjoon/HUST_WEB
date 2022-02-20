package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import db.DB;
import vo.Problem;
import vo.Member;
import vo.Permission;

public class ProblemDAO {
	private static ProblemDAO dao;
	
	private ProblemDAO() {}
	
	public static ProblemDAO getInstance() {
		if(dao==null) {
			dao = new ProblemDAO();
		}
		return dao;
	}
	
	public boolean write_problem(Problem prob) {
		Connection conn = DB.getConnection();
		PreparedStatement pstmt = null;
		try {
			String sql = "insert into problem(title,category,content,writer,point,answer) values(?,?,?,?,?,?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, prob.getTitle());
			pstmt.setString(2, prob.getCategory());
			pstmt.setString(3, prob.getContent());
			pstmt.setString(4, prob.getWriter());
			pstmt.setInt(5, prob.getPoint());
			pstmt.setString(6, prob.getAnswer());
			pstmt.executeUpdate();
			return true;
		}catch(Exception e) {
			System.err.println(e);
			return false;
		}
	}
	
	public boolean update_problem(Problem prob) {
		Connection conn = DB.getConnection();
		PreparedStatement pstmt = null;
		try {
			String sql = "update problem set title = ?, category = ?, content = ?, point = ?, answer =? where probid = ? and writer = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, prob.getTitle());
			pstmt.setString(2,prob.getCategory());
			pstmt.setString(3, prob.getContent());
			pstmt.setInt(4, prob.getPoint());
			pstmt.setString(5, prob.getAnswer());
			pstmt.setInt(6,prob.getProbid());
			pstmt.setString(7,prob.getWriter());
			pstmt.executeUpdate();
			return true;
		}catch(Exception e) {
			System.err.println(e);
			return false;
		}
	}
	
	public boolean delete_problem(Problem prob) {
		Connection conn = DB.getConnection();
		PreparedStatement pstmt = null;
		try {
			String sql = "delete from problem where probid = ? and writer = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1,prob.getProbid());
			pstmt.setString(2, prob.getWriter());
			pstmt.executeUpdate();
			return true;
		}catch(Exception e) {
			System.err.println(e);
			return false;
		}
	}
	
	public ArrayList<Problem> getProbList(){
		Connection conn = DB.getConnection();
		PreparedStatement pstmt = null;
		ArrayList<Problem> list = new ArrayList<Problem>();
		try {
			String sql = "select * from problem";
			pstmt = conn.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				Problem prob = new Problem();
				prob.setProbid(rs.getInt("probid"));
				prob.setCategory(rs.getString("category"));
				prob.setTitle(rs.getString("title"));
				prob.setContent(rs.getString("content"));
				prob.setWriter(rs.getString("writer"));
				prob.setPoint(rs.getInt("point"));
				prob.setAnswer(rs.getString("answer"));
				list.add(prob);
			}
			return list;
			
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	public Problem getProb(int probid){
		Connection conn = DB.getConnection();
		PreparedStatement pstmt = null;
		try {
			String sql = "select * from problem where probid = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, probid);
			ResultSet rs = pstmt.executeQuery();
			Problem prob = new Problem();
			while(rs.next()) {
				prob.setProbid(rs.getInt("probid"));
				prob.setCategory(rs.getString("category"));
				prob.setTitle(rs.getString("title"));
				prob.setContent(rs.getString("content"));
				prob.setWriter(rs.getString("writer"));
				prob.setPoint(rs.getInt("point"));
				prob.setAnswer(rs.getString("answer"));
			}
			return prob;
			
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	public boolean scoring(Problem prob,String user_id) {
		Connection conn = DB.getConnection();
		PreparedStatement pstmt = null;
		try {
			String sql = "select answer,point from problem where probid = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, prob.getProbid());
			ResultSet rs = pstmt.executeQuery();
			String right_answer="";
			int point=0;
			while(rs.next()) {
				right_answer = rs.getString("answer");
				point = rs.getInt("point");
			}
			if(right_answer.contentEquals(prob.getAnswer())) {
				PreparedStatement pstmt2 = null;
				sql = "update member set prob_score = prob_score + "+point +", solved_prob = concat(solved_prob,',"+prob.getProbid()+"') where id = ?";
				pstmt2 = conn.prepareStatement(sql);
				pstmt2.setString(1,user_id);
				if(pstmt2.executeUpdate()==1) {
					return true;
				}else {
					return false;
				}
			}else 
				return false;
			
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	public ArrayList<Integer> getSolvedList(String user_id){
		Connection conn = DB.getConnection();
		PreparedStatement pstmt = null;
		ArrayList<Integer> solvedlist = new ArrayList<Integer>();
		try {
			String sql = "select solved_prob from member where id = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, user_id);
			ResultSet rs = pstmt.executeQuery();
			String list = "";
			while(rs.next()) {
				list = rs.getString("solved_prob");
			}
			if(list==null||list.isEmpty()||list.isBlank())return solvedlist;
			String split_list[] = list.split(",");
			split_list[0]="0";
			for(int i=0;i<split_list.length;i++) {
				solvedlist.add(Integer.parseInt(split_list[i]));
			}
			return solvedlist;
			
		}catch(Exception e) {
			e.printStackTrace();
			return solvedlist;
		}
	}
	public ArrayList<Member> getRanking(){
		ArrayList<Member> list = new ArrayList<Member>();
		Connection conn = DB.getConnection();
		PreparedStatement pstmt = null;
		try {
			String sql = "select * from member where permission>=1 and prob_score>0 order by prob_score desc,prob_score_time asc";
			pstmt = conn.prepareStatement(sql);
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
				member.setProb_score_time(rs.getTimestamp("prob_score_time"));
				list.add(member);
			}
			
			return list;
			
		}catch(Exception e) {
			e.printStackTrace();
			return list;
		}
	}
}
