package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import auth.Auth;
import auth.AuthManager;
import service.CommentService;
import service.MemberService;
import service.PostService;
import tools.HttpUtil;
import tools.Secure;
import tools.Sessions;
import vo.Comment;
import vo.Member;
import vo.Permission;
import vo.Post;

import java.net.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MemberControllerNeedCSRFCheck implements Controller,NeedCSRFCheck {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response, String action)
			throws ServletException, IOException {

		Auth auth = new Auth(request);
		try {
		if(action.contentEquals("change_myinfo")) {
			update(request,response,auth);
		}else if(action.contentEquals("update_member_per")) {
			updatePermissionAnddeleteSessionIfSelfUpdate(request,response,auth);
		}else if(action.contentEquals("delete")) {
			delete(request,response,auth);
		}else if(action.contentEquals("reset_member_pw")) {
			resetMemberPassword(request, response, auth);
		}
		}catch(Exception e) {
			response.sendRedirect("index.jsp");
		}
	}
	
	
	private void update(HttpServletRequest request, HttpServletResponse response, Auth auth)
			throws Exception{
		boolean pwCheckEnable = false;
		Member member = new Member(request, Permission.GUEST,pwCheckEnable);
		if(MemberService.update(member)) {
			request.setAttribute("ok_body", "수정 성공하였습니다.");
			request.setAttribute("forward_url", "myinfo.do");
			HttpUtil.forward(request, response, "/WEB-INF/pages/ok.jsp");
		}else {
			request.setAttribute("err_body", "수정 실패하였습니다.");
			request.setAttribute("forward_url", "myinfo.do");
			HttpUtil.forward(request, response, "/WEB-INF/pages/fail.jsp");
		}
	}
	
	private void updatePermissionAnddeleteSessionIfSelfUpdate(HttpServletRequest request,HttpServletResponse response, Auth auth) throws Exception {
		HttpSession session = request.getSession();
		String id = request.getParameter("id"); //등급변경대상 회원의 아이디
		Permission targetOldPermission = MemberService.getMember(id).getPermission(); // 변경대상 회원의 현재 멤버등급.
		Permission targetPermission = Permission.intToPermission(Integer.parseInt(request.getParameter("edit_per"))); //변경하려는 멤버등급
		Permission permission = Permission.intToPermission((int)session.getAttribute("permission")); // 로그인된 사용자의 권한
		Member member = new Member(id);
		String admin_id = (String)session.getAttribute("id"); // 현재 로그인되어있는 아이디
		if(isInvalidPermissions(permission, targetOldPermission, targetPermission)) {
			if(MemberService.update_member_per(member, Permission.permissionToInt(targetPermission))) {//성공
				if(admin_id.contentEquals(id)) { // 자기 자신의 권한을 바꾼 경우. 로그아웃처리
					session.invalidate();
					response.sendRedirect("index.jsp");
				}else { // 권한을 바꾸려는 대상이 자기자신이 아닌 경우
					response.sendRedirect("memberlist.do?group="+URLEncoder.encode("전체", "UTF-8")+"&permission="+Permission.permissionToInt(targetPermission));
				}
			}else {//실패
				request.setAttribute("err_body", "권한이 없습니다.");
				request.setAttribute("forward_url", "memberlist.do?group="+URLEncoder.encode("전체", "UTF-8")+"&permission=7");
				HttpUtil.forward(request, response, "/WEB-INF/pages/fail.jsp");
			}
		}
	}
	private boolean isInvalidPermissions(Permission requesterPermission, Permission targetOldPermission, Permission targetPermission){
		int per = Permission.permissionToInt(targetPermission);
		int old_per = Permission.permissionToInt(targetOldPermission);
		if(requesterPermission == Permission.GENREAL_ADMIN) {
			return true;
		}else if(requesterPermission == Permission.OB_ADMIN) {
			if((per>=2&&per<=5) && (old_per==2 || old_per==3||old_per==5)) {
				return true;
			}else {
				return false;
			}
		}else if(requesterPermission == Permission.YB_ADMIN) {
			if((per>=0&&per!=3&&per<=4) && (old_per==0 || old_per==1||old_per==2||old_per==4)) {
				return true;
			}else {
				return false;
			}
		}else {
			return false;
		}
	}
	
	private void delete(HttpServletRequest request, HttpServletResponse response, Auth auth){
		
	}
	
	
	private void resetMemberPassword(HttpServletRequest request, HttpServletResponse response, Auth auth){
		String id = request.getParameter("id");
		Member member = MemberService.getMember(id);
		Permission memberPermission = member.getPermission();
		if(canResetMemberPassword(auth, memberPermission)) {
			String pw = MemberService.reset_member_pw(id);
			if(pw.contentEquals("")) {
				request.setAttribute("err_body", "알 수 없는 이유로 초기화에 실패하였습니다.");
				request.setAttribute("forward_url", "index.jsp");
				HttpUtil.forward(request, response, "/WEB-INF/pages/fail.jsp");
			}else {
				request.setAttribute("ok_body", id+"님의 비밀번호가 "+pw+"로 초기화되었습니다.");
				request.setAttribute("forward_url", "index.jsp");
				HttpUtil.forward(request, response, "/WEB-INF/pages/ok.jsp");
			}
		}else {
			request.setAttribute("err_body", "이 회원의 비밀번호를 초기화 할 권한이 없습니다");
			request.setAttribute("forward_url", "index.jsp");
			HttpUtil.forward(request, response, "/WEB-INF/pages/fail.jsp");
		}
	}
	private boolean canResetMemberPassword(Auth auth, Permission memberPermission) {
		Permission permission = auth.getPermission();
		if(permission == Permission.GENREAL_ADMIN) {
			return true;
		}else if(permission == Permission.OB_ADMIN) {
			if(memberPermission == Permission.OB || memberPermission == Permission.OB_ADMIN) {
				return true;
			}else {
				return false;
			}
		}else if(permission == Permission.YB_ADMIN) {
			if(memberPermission == Permission.YB_ADMIN || memberPermission.compareTo(Permission.YB)<=0) {
				return true;
			}else {
				return false;
			}
		}else {
			return false;
		}
	}
}
