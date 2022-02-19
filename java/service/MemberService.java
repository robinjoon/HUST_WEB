package service;

import java.util.ArrayList;

import dao.MemberDAO;
import vo.MemberVO;

public class MemberService {
	public static boolean delete_member(MemberVO member) {
		MemberDAO dao = MemberDAO.getInstance();
		return dao.delete_member(member);
	}
	public static boolean join(MemberVO member) {
		MemberDAO dao = MemberDAO.getInstance();
		return dao.join_member(member);
	}
	public static int login(MemberVO member) {//return == -1 ==> 로그인 실패. else 사용자 권한 반환 
		MemberDAO dao = MemberDAO.getInstance();
		return dao.login_member(member);
	}
	public static boolean update(MemberVO member) {
		MemberDAO dao = MemberDAO.getInstance();
		return dao.update_member(member);
	}
	public static MemberVO getMember(String id) {
		MemberDAO dao = MemberDAO.getInstance();
		return dao.getMember(id);
	}
	public static ArrayList<MemberVO> getMemberList(String group){
		MemberDAO dao = MemberDAO.getInstance();
		return dao.getMemberList(group);
	}
	public static ArrayList<MemberVO> getMemberList(int permission){
		MemberDAO dao = MemberDAO.getInstance();
		return dao.getMemberList(permission);
	}
	public static ArrayList<MemberVO> getMemberList(int permission,String group){
		MemberDAO dao = MemberDAO.getInstance();
		return dao.getMemberList(permission,group);
	}
	public static ArrayList<MemberVO> getMemberList(int permission, String input, String notuse){
		MemberDAO dao = MemberDAO.getInstance();
		return dao.getMemberList(permission,input,notuse);
	}
	public static boolean update_member_per(MemberVO member,int per) {
		MemberDAO dao = MemberDAO.getInstance();
		return dao.update_per(member, per);
	}
	
	public static String reset_member_pw(String id) {
		MemberDAO dao = MemberDAO.getInstance();
		return dao.resetPw(id);
	}
	
	public static boolean id_duplicate_check(String id) {
		MemberDAO dao = MemberDAO.getInstance();
		return dao.id_duplicate_check(id);
	}
}
