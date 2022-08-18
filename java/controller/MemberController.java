package controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import auth.Auth;
import service.MemberService;
import tools.HttpUtil;
import tools.Secure;
import tools.Sessions;
import vo.Member;
import vo.Permission;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MemberController implements Controller {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response, String action)
			throws ServletException, IOException {
		try {
			Auth auth = new Auth(request);
			if(action.contentEquals("join")) {
				create(request,response,auth);
			}else if(action.contentEquals("login")) {
				login(request,response,auth);
			}
		} catch (Exception e) {
			response.sendRedirect("index.jsp");
			e.printStackTrace();
		} 
	}
	private void create(HttpServletRequest request, HttpServletResponse response, Auth auth)
			throws Exception{
		boolean pwCheckEnable = true;
		Member member = new Member(request, Permission.GUEST, pwCheckEnable);
		if(idCheck(member.getId()) || nameCheck(member.getName())){
			request.setAttribute("err_body", "아이디와 이름에는 숫자,알파벳대소문자,한글만 사용할 수 있습니다.");
			request.setAttribute("forward_url", "join.jsp");
			HttpUtil.forward(request, response, "/WEB-INF/pages/fail.jsp");
			return;
		}
		if(MemberService.join(member)) { // 회원가입 성공
			request.setAttribute("ok_body", "가입을 환영합니다!");
			HttpUtil.forward(request, response, "/WEB-INF/pages/ok.jsp");
		}else {
			request.setAttribute("err_body", "db 에러");
			HttpUtil.forward(request, response, "/WEB-INF/pages/fail.jsp");
		}
	}
	private boolean idCheck(String id) {
		return !id.matches("[0-9|a-z|A-Z|ㄱ-ㅎ|ㅏ-ㅣ|가-힝]*");
	}
	private boolean nameCheck(String name) {
		return !name.matches("[0-9|a-z|A-Z|ㄱ-ㅎ|ㅏ-ㅣ|가-힝]*");
	}
	
	
	private void login(HttpServletRequest request, HttpServletResponse response, Auth auth){
		String id = request.getParameter("id");
		String pw = request.getParameter("pw");
		Member member = new Member(id, pw);
		HttpSession session = request.getSession();
		try {
			Permission permission = MemberService.login(member);
			session.setAttribute("id", id);
			session.setAttribute("permission",permission);
			String csrf_token ="";
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
			LocalDateTime now = LocalDateTime.now();
			csrf_token = id+dtf.format(now);
			csrf_token = Secure.sha256(csrf_token);
			session.setAttribute("csrf_token", csrf_token);
			Sessions.addSession(session);
			response.sendRedirect("index.jsp");
		}catch(Exception e) {
			session.invalidate();
			request.setAttribute("err_body", "아이디나 비밀번호가 잘못되었습니다.");
			request.setAttribute("forward_url", "login.jsp");
			HttpUtil.forward(request, response, "/WEB-INF/pages/fail.jsp");
		}
	}
}
