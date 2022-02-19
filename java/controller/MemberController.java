package controller;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import service.CommentService;
import service.MemberService;
import service.PostService;
import tools.HttpUtil;
import tools.Secure;
import tools.Sessions;
import vo.Comment;
import vo.Member;
import vo.Post;

import java.net.*;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MemberController implements Controller {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response, String action)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
			if(action.contentEquals("join")) {
				create(request,response);
			}else if(action.contentEquals("getMember")) {
				read(request,response);
			}else if(action.contentEquals("myinfo")) {
				read_me(request,response);
			}else if(action.contentEquals("memberlist")) {
				read_many(request,response);
			}else if(action.contentEquals("change_myinfo")) {
				update(request,response);
			}else if(action.contentEquals("update_member_per")) {
				update_permission(request,response);
			}else if(action.contentEquals("delete")) {
				delete(request,response);
			}else if(action.contentEquals("login")) {
				login(request,response);
			}else if(action.contentEquals("reset_member_pw")) {
				reset_member_pw(request,response);
			}
	}
	private void create(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException{
		String id,pw,pw_check,name,phone,email,scholastic,interest,address,address_now,etc;
		Integer birthY,admissionY,joinY,school_year;
		boolean mypost_comment_noti_allow = Boolean.parseBoolean(request.getParameter("mypost_comment_noti_allow"));
		boolean mycomment_comment_noti_allow = Boolean.parseBoolean(request.getParameter("mycomment_comment_noti_allow"));
		boolean call_noti_allow = Boolean.parseBoolean(request.getParameter("call_noti_allow"));
		
		id = request.getParameter("id");
		pw = request.getParameter("pw");
		pw_check = request.getParameter("pw_check");
		if(!pw.equals(pw_check)) {
			HttpUtil.forward(request, response, "join.jsp");
			return;
		}
		name = request.getParameter("name"); 
		phone = request.getParameter("phone");
		email = request.getParameter("email"); scholastic = request.getParameter("scholastic");
		interest = request.getParameter("interest");
		address = request.getParameter("address");address_now = request.getParameter("address_now");
		etc = request.getParameter("etc");
		try {
			birthY = Integer.parseInt(request.getParameter("birthY"));
		}catch(NumberFormatException e) {
			birthY=0;
		}
		try {
			admissionY = Integer.parseInt(request.getParameter("admissionY"));
		}catch(NumberFormatException e) {
			admissionY=0;
		}
		try {
			joinY = Integer.parseInt(request.getParameter("joinY"));
		}catch(NumberFormatException e) {
			joinY=0;
		}
		try {
			school_year = Integer.parseInt(request.getParameter("school_year"));
		}catch(NumberFormatException e) {
			school_year=0;
		}
		if(!id.matches("[0-9|a-z|A-Z|ㄱ-ㅎ|ㅏ-ㅣ|가-힝]*")||!name.matches("[0-9|a-z|A-Z|ㄱ-ㅎ|ㅏ-ㅣ|가-힝]*")){
			request.setAttribute("err_body", "아이디와 이름에는 숫자,알파벳대소문자,한글만 사용할 수 있습니다.");
			request.setAttribute("forward_url", "join.jsp");
			HttpUtil.forward(request, response, "/WEB-INF/pages/fail.jsp");
			return;
		}
		Member member = new Member(id,pw,name,birthY,admissionY,joinY,phone,email,scholastic,school_year,interest,address,address_now,0,etc,0);
		member.setMypost_comment_noti_allow(mypost_comment_noti_allow);
		member.setMycomment_comment_noti_allow(mycomment_comment_noti_allow);
		member.setCall_noti_allow(call_noti_allow);
		if(MemberService.join(member)) { // 회원가입 성공
			request.setAttribute("ok_body", "가입을 환영합니다!");
			HttpUtil.forward(request, response, "/WEB-INF/pages/ok.jsp");
		}else {
			request.setAttribute("err_body", "db 에러");
			HttpUtil.forward(request, response, "/WEB-INF/pages/fail.jsp");
		}
	}
	private void read(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException{
		String memberid = request.getParameter("member");
		HttpSession session = request.getSession();
		Integer permission = (int)session.getAttribute("permission");
		if(permission>=1) {
			Member member = MemberService.getMember(memberid);
			ArrayList<Member> list = new ArrayList<Member>();
			list.add(member);
			request.setAttribute("memberlist", list);
			HttpUtil.forward(request, response, "/WEB-INF/pages/memberlist.jsp");
		}else {
			response.sendRedirect("index.jsp");
		}
	}
	private void read_me(HttpServletRequest request,HttpServletResponse response) {
		HttpSession session = request.getSession();
		String id = (String)session.getAttribute("id");
		if(id!=null) { // 로그인 확인
			Member member = MemberService.getMember(id);
			ArrayList<Post> list = PostService.getMyPosts(id);
			ArrayList<Comment> clist = CommentService.getMyComments(id);
			if(member==null || list==null || clist==null) { // 멤버정보 가져오기 실패
				request.setAttribute("err_body", "알수 없는 오류로 멤버정보를 가져오는데 실패하였습니다.");
				request.setAttribute("forward_url", "index.jsp");
				HttpUtil.forward(request, response, "/WEB-INF/pages/fail.jsp");
			}else {
				request.setAttribute("list", list);
				request.setAttribute("clist", clist);
				request.setAttribute("member", member);
				HttpUtil.forward(request, response, "/WEB-INF/pages/myinfo.jsp");
			}
		}else {
			request.setAttribute("err_body", "로그인이 필요합니다.");
			request.setAttribute("forward_url", "login.jsp");
			HttpUtil.forward(request, response, "/WEB-INF/pages/fail.jsp");
		}
	}
	private void read_many(HttpServletRequest request,HttpServletResponse response) throws IOException {
		ArrayList<Member> list = null;
		HttpSession session = request.getSession();
		Integer per = (Integer)session.getAttribute("permission"); // 사용자의 권한
		if(per==null||per<1) { // 로그인X
			response.sendRedirect("index.jsp");
			return;
		}
		String group = request.getParameter("group");
		Integer permission = -1;
		if(request.getParameter("permission")!=null) // 에러방지.
			permission = Integer.parseInt(request.getParameter("permission")); // 가져올 멤버리스트의 멤버의 권한
		if(group==null && permission==-1) { // 요청값 없음.
			System.out.println("요청값없음");
			response.sendRedirect("index.jsp");
			return;
		}else if(group==null) {
			list = MemberService.getMemberList(permission);
			request.setAttribute("memberlist", list);
			HttpUtil.forward(request, response, "/WEB-INF/pages/memberlist.jsp");
		}else if(permission==-1) {
			list = MemberService.getMemberList(group);
			request.setAttribute("memberlist", list);
			HttpUtil.forward(request, response, "/WEB-INF/pages/memberlist.jsp");
		}else {
			list = MemberService.getMemberList(permission, group);
			request.setAttribute("memberlist", list);
			HttpUtil.forward(request, response, "/WEB-INF/pages/memberlist.jsp");
		}
	}
	private void update(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException{
		String id,pw,name,phone,email,scholastic,interest,address,address_now,etc;
		Integer birthY,admissionY,joinY,school_year;
		HttpSession session = request.getSession();
		id = (String)session.getAttribute("id"); // 사용자의 입력과 관계없이 로그인된 사용자의 정보변경을 시도.
		pw = request.getParameter("pw");
		name = request.getParameter("name"); 
		phone = request.getParameter("phone");
		email = request.getParameter("email"); scholastic = request.getParameter("scholastic");
		interest = request.getParameter("interest");
		address = request.getParameter("address");address_now = request.getParameter("address_now");
		etc = request.getParameter("etc");
		boolean mypost_comment_noti_allow = Boolean.parseBoolean(request.getParameter("mypost_comment_noti_allow"));
		boolean mycomment_comment_noti_allow = Boolean.parseBoolean(request.getParameter("mycomment_comment_noti_allow"));
		boolean call_noti_allow = Boolean.parseBoolean(request.getParameter("call_noti_allow"));
		try {
			birthY = Integer.parseInt(request.getParameter("birthY"));
		}catch(NumberFormatException e) {
			birthY=0;
		}
		try {
			admissionY = Integer.parseInt(request.getParameter("admissionY"));
		}catch(NumberFormatException e) {
			admissionY=0;
		}
		try {
			joinY = Integer.parseInt(request.getParameter("joinY"));
		}catch(NumberFormatException e) {
			joinY=0;
		}
		try {
			school_year = Integer.parseInt(request.getParameter("school_year"));
		}catch(NumberFormatException e) {
			school_year=0;
		}
		Member member = new Member(id,pw,name,birthY,admissionY,joinY,phone,email,scholastic,school_year,interest,address,address_now,0,etc,0);
		member.setMypost_comment_noti_allow(mypost_comment_noti_allow);
		member.setMycomment_comment_noti_allow(mycomment_comment_noti_allow);
		member.setCall_noti_allow(call_noti_allow);
		String csrf_token_server = (String)session.getAttribute("csrf_token");
		String csrf_token_client = request.getParameter("csrf_token");
		if(id!=null && csrf_token_server!=null && csrf_token_client!=null) { // 로그인 확인
			if(csrf_token_server.contentEquals(csrf_token_client)) { // csrf 토큰 체크
				if(MemberService.update(member)) {
					request.setAttribute("ok_body", "수정 성공하였습니다.");
					request.setAttribute("forward_url", "myinfo.do");
					HttpUtil.forward(request, response, "/WEB-INF/pages/ok.jsp");
				}else {
					request.setAttribute("err_body", "수정 실패하였습니다.");
					request.setAttribute("forward_url", "myinfo.do");
					HttpUtil.forward(request, response, "/WEB-INF/pages/fail.jsp");
				}
			}else {
				request.setAttribute("err_body", "csrf 토큰이 일치하지 않습니다. 알맞은 접근으로 시도하세요.");
				request.setAttribute("forward_url", "index.jsp");
				HttpUtil.forward(request, response, "/WEB-INF/pages/fail.jsp");
			}
		}else {
			response.sendRedirect("index.jsp");
		}
	}
	private void update_permission(HttpServletRequest request,HttpServletResponse response) throws IOException {
		HttpSession session = request.getSession();
		String id = request.getParameter("id"); //등급변경대상 회원의 아이디
		int old_per = MemberService.getMember(id).getPermission(); // 변경대상 회원의 현재 멤버등급.
		int per = Integer.parseInt(request.getParameter("edit_per")); //변경하려는 멤버등급
		int permission = (int)session.getAttribute("permission"); // 로그인된 사용자의 권한
		Member member = new Member();
		member.setId(id);
		String csrf_token_server = (String)session.getAttribute("csrf_token");
		String csrf_token_client = request.getParameter("csrf_token");
		if(session.getAttribute("id")!=null&& csrf_token_server!=null && csrf_token_client!=null) {// 로그인여부 확인
			String admin_id = (String)session.getAttribute("id"); // 현재 로그인되어있는 아이디
			if(csrf_token_server.contentEquals(csrf_token_client)) { // csrf 토큰 체크
				if(permission==6) {//관리자
					if(MemberService.update_member_per(member, per)) {//성공
						if(admin_id.contentEquals(id)) { // 자기 자신의 권한을 바꾼 경우. 로그아웃처리
							session.invalidate();
							response.sendRedirect("index.jsp");
						}else { // 권한을 바꾸려는 대상이 자기자신이 아닌 경우
							response.sendRedirect("memberlist.do?group="+URLEncoder.encode("전체", "UTF-8")+"&permission="+per);
						}
					}else {//실패
						request.setAttribute("err_body", "권한이 없습니다.");
						request.setAttribute("forward_url", "memberlist.do?group="+URLEncoder.encode("전체", "UTF-8")+"&permission=7");
						HttpUtil.forward(request, response, "/WEB-INF/pages/fail.jsp");
					}
				}else if(permission==5 && (per>=2&&per<=5) && (old_per==2 || old_per==3||old_per==5)) {//OB운영진
					if(MemberService.update_member_per(member, per)) {//성공
						if(admin_id.contentEquals(id)) { // 자기 자신의 권한을 바꾼 경우. 로그아웃처리
							session.invalidate();
							response.sendRedirect("index.jsp");
						}else { // 권한을 바꾸려는 대상이 자기자신이 아닌 경우
							response.sendRedirect("memberlist.do?group="+URLEncoder.encode("전체", "UTF-8")+"&permission="+per);
						}
					}else {//실패
						request.setAttribute("err_body", "권한이 없습니다.");
						request.setAttribute("forward_url", "memberlist.do?group="+URLEncoder.encode("전체", "UTF-8")+"&permission=7");
						HttpUtil.forward(request, response, "/WEB-INF/pages/fail.jsp");
					}
				}else if(permission==4 && (per>=0&&per!=3&&per<=4) && (old_per==0 || old_per==1||old_per==2||old_per==4)) {//YB운영진
					if(MemberService.update_member_per(member, per)) {//성공
						if(admin_id.contentEquals(id)) { // 자기 자신의 권한을 바꾼 경우. 로그아웃처리
							session.invalidate();
							response.sendRedirect("index.jsp");
						}else { // 권한을 바꾸려는 대상이 자기자신이 아닌 경우
							response.sendRedirect("memberlist.do?group="+URLEncoder.encode("전체", "UTF-8")+"&permission="+per);
						}
					}else {//실패
						request.setAttribute("err_body", "권한이 없습니다.");
						request.setAttribute("forward_url", "memberlist.do?group="+URLEncoder.encode("전체", "UTF-8")+"&permission=7");
						HttpUtil.forward(request, response, "/WEB-INF/pages/fail.jsp");
					}
				}else {//권한없음
					request.setAttribute("err_body", "권한이 없습니다.");
					request.setAttribute("forward_url", "memberlist.do?group="+URLEncoder.encode("전체", "UTF-8")+"&permission=7");
					HttpUtil.forward(request, response, "/WEB-INF/pages/fail.jsp");
				}
			}else {
				request.setAttribute("err_body", "csrf 토큰이 일치하지 않습니다. 알맞은 접근으로 시도하세요.");
				request.setAttribute("forward_url", "index.jsp");
				HttpUtil.forward(request, response, "/WEB-INF/pages/fail.jsp");
			}
		}
	}
	private void delete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException{
		String id = request.getParameter("id");
		String pw = request.getParameter("pw");
		Member member = new Member();
		member.setId(id);
		member.setPw(pw);
		HttpSession session = request.getSession();
		if(session.getAttribute("id")!=null) { //로그인여부 확인
			if(id.equals(session.getAttribute("id")) || (int)session.getAttribute("permission")==6) { // 삭제하려는 계정이 본인이거나 관리자인 경우
				if(MemberService.delete_member(member)) {
					request.setAttribute("ok_body", "탈퇴 완료되었습니다...ㅠㅠ");
					HttpUtil.forward(request, response, "/WEB-INF/pages/ok.jsp");
				}else {
					request.setAttribute("err_body", "탈퇴 실패하였습니다.");
					HttpUtil.forward(request, response, "/WEB-INF/pages/fail.jsp");
				}
			}else { // 삭제권한이 없는 경우
				session.invalidate();
				response.sendRedirect("index.jsp");
			}
		}else {//로그인 X
			response.sendRedirect("index.jsp");
		}
	}
	private void login(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException{
		String id = request.getParameter("id");
		String pw = request.getParameter("pw");
		Member member = new Member();
		member.setId(id);
		member.setPw(pw);
		HttpSession session = request.getSession();
		if(session.getAttribute(id)!=null) {//로그인여부 확인
			request.setAttribute("err_body", "이미 로그인되어있습니다.");
			request.setAttribute("forward_url", "index.jsp");
			HttpUtil.forward(request, response, "/WEB-INF/pages/fail.jsp");
			return;
		}else { //로그인되어있지 않으면
			int per = MemberService.login(member);
			if(per!=-1) { // per == -1 ==> 로그인 실패. else 사용자 권한 
				session.setAttribute("id", id);
				session.setAttribute("permission",per);
				String csrf_token ="";
				DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
				LocalDateTime now = LocalDateTime.now();
				csrf_token = id+dtf.format(now);
				try {
					csrf_token = Secure.sha256(csrf_token);
					session.setAttribute("csrf_token", csrf_token);
				} catch (NoSuchAlgorithmException e) {
					e.printStackTrace();
				}
				Sessions.addSession(session);
				response.sendRedirect("index.jsp");
			}else {
				request.setAttribute("err_body", "아이디나 비밀번호가 잘못되었습니다.");
				request.setAttribute("forward_url", "login.jsp");
				HttpUtil.forward(request, response, "/WEB-INF/pages/fail.jsp");
			}
		}
	}
	private void reset_member_pw(HttpServletRequest request, HttpServletResponse response) throws IOException {
		HttpSession session = request.getSession();
		String id = (String)session.getAttribute("id");
		int permission = (int)session.getAttribute("permission");
		String uid = request.getParameter("id");
		Member member = MemberService.getMember(uid);
		int per = member.getPermission();
		String csrf_token_server = (String)session.getAttribute("csrf_token");
		String csrf_token_client = request.getParameter("csrf_token");
		if(id!=null && csrf_token_server!=null && csrf_token_client!=null) {
			if(csrf_token_server.contentEquals(csrf_token_client)) { // csrf 토큰 체크
				if(permission==4 && (per<=2 || per ==4)) {
					String pw = MemberService.reset_member_pw(uid);
					if(pw.contentEquals("")) {
						request.setAttribute("err_body", "알 수 없는 이유로 초기화에 실패하였습니다.");
						request.setAttribute("forward_url", "index.jsp");
						HttpUtil.forward(request, response, "/WEB-INF/pages/fail.jsp");
					}else {
						request.setAttribute("ok_body", uid+"님의 비밀번호가 "+pw+"로 초기화되었습니다.");
						request.setAttribute("forward_url", "index.jsp");
						HttpUtil.forward(request, response, "/WEB-INF/pages/ok.jsp");
					}
				}else if(permission==5 && (per==3 || per==5)) {
					String pw = MemberService.reset_member_pw(uid);
					if(pw.contentEquals("")) {
						request.setAttribute("err_body", "알 수 없는 이유로 초기화에 실패하였습니다.");
						request.setAttribute("forward_url", "index.jsp");
						HttpUtil.forward(request, response, "/WEB-INF/pages/fail.jsp");
					}else {
						request.setAttribute("ok_body", uid+"님의 비밀번호가 "+pw+"로 초기화되었습니다.");
						request.setAttribute("forward_url", "index.jsp");
						HttpUtil.forward(request, response, "/WEB-INF/pages/ok.jsp");
					}
				}else if(permission==6) {
					String pw = MemberService.reset_member_pw(uid);
					if(pw.contentEquals("")) {
						request.setAttribute("err_body", "알 수 없는 이유로 초기화에 실패하였습니다.");
						request.setAttribute("forward_url", "index.jsp");
						HttpUtil.forward(request, response, "/WEB-INF/pages/fail.jsp");
					}else {
						request.setAttribute("ok_body", uid+"님의 비밀번호가 "+pw+"로 초기화되었습니다.");
						request.setAttribute("forward_url", "index.jsp");
						HttpUtil.forward(request, response, "/WEB-INF/pages/ok.jsp");
					}
				}else {
					request.setAttribute("err_body", "이 회원의 비밀번호를 초기화 할 권한이 없습니다");
					request.setAttribute("forward_url", "index.jsp");
					HttpUtil.forward(request, response, "/WEB-INF/pages/fail.jsp");
				}
			}else {
				request.setAttribute("err_body", "csrf 토큰이 일치하지 않습니다. 알맞은 접근으로 시도하세요.");
				request.setAttribute("forward_url", "index.jsp");
				HttpUtil.forward(request, response, "/WEB-INF/pages/fail.jsp");
			}
		}else {
			response.sendRedirect("index.jsp");
		}
	}
}
