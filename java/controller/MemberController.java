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

public class MemberController implements Controller {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response, String action)
			throws ServletException, IOException {
			try {
				Auth auth = new Auth(request);
				if(AuthManager.loginCheck(auth)) {
					if(AuthManager.csrfCheck(auth)) {
						if(action.contentEquals("change_myinfo")) {
							update(request,response,auth);
						}else if(action.contentEquals("update_member_per")) {
							updatePermissionAnddeleteSessionIfSelfUpdate(request,response,auth);
						}else if(action.contentEquals("delete")) {
							delete(request,response,auth);
						}else if(action.contentEquals("reset_member_pw")) {
							resetMemberPassword(request, response, auth);
						}
					}else{
						if(action.contentEquals("getMember")) {
							read(request,response,auth);
						}else if(action.contentEquals("myinfo")) {
							readMe(request,response,auth);
						}else if(action.contentEquals("memberlist")) {
							readMany(request,response,auth);
						} 
					}
				}else {
					if(action.contentEquals("join")) {
						create(request,response,auth);
					}else if(action.contentEquals("login")) {
						login(request,response,auth);
					}
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
			request.setAttribute("err_body", "???????????? ???????????? ??????,?????????????????????,????????? ????????? ??? ????????????.");
			request.setAttribute("forward_url", "join.jsp");
			HttpUtil.forward(request, response, "/WEB-INF/pages/fail.jsp");
			return;
		}
		if(MemberService.join(member)) { // ???????????? ??????
			request.setAttribute("ok_body", "????????? ???????????????!");
			HttpUtil.forward(request, response, "/WEB-INF/pages/ok.jsp");
		}else {
			request.setAttribute("err_body", "db ??????");
			HttpUtil.forward(request, response, "/WEB-INF/pages/fail.jsp");
		}
	}
	private boolean idCheck(String id) {
		return !id.matches("[0-9|a-z|A-Z|???-???|???-???|???-???]*");
	}
	private boolean nameCheck(String name) {
		return !name.matches("[0-9|a-z|A-Z|???-???|???-???|???-???]*");
	}
	
	private void read(HttpServletRequest request, HttpServletResponse response, Auth auth)
			throws ServletException, IOException{
		String memberid = request.getParameter("member"); // ????????? ?????? ????????? ?????????.
		if(AuthManager.canReadMemberList(auth)) {
			Member member = MemberService.getMember(memberid);
			ArrayList<Member> list = new ArrayList<Member>();
			list.add(member);
			request.setAttribute("memberlist", list);
			HttpUtil.forward(request, response, "/WEB-INF/pages/memberlist.jsp");
		}else {
			response.sendRedirect("index.jsp");
		}
	}
	
	private void readMe(HttpServletRequest request,HttpServletResponse response, Auth auth) {
		HttpSession session = request.getSession();
		String id = (String)session.getAttribute("id");
		if(AuthManager.loginCheck(auth)) { // ????????? ??????
			Member member = MemberService.getMember(id);
			ArrayList<Post> list = PostService.getMyPosts(id);
			ArrayList<Comment> clist = CommentService.getMyComments(id);
			if(member==null || isInvalidList(list) || isInvalidList(clist)) { // ???????????? ???????????? ??????
				request.setAttribute("err_body", "?????? ?????? ????????? ??????????????? ??????????????? ?????????????????????.");
				request.setAttribute("forward_url", "index.jsp");
				HttpUtil.forward(request, response, "/WEB-INF/pages/fail.jsp");
			}else {
				request.setAttribute("list", list);
				request.setAttribute("clist", clist);
				request.setAttribute("member", member);
				HttpUtil.forward(request, response, "/WEB-INF/pages/myinfo.jsp");
			}
		}else {
			request.setAttribute("err_body", "???????????? ???????????????.");
			request.setAttribute("forward_url", "login.jsp");
			HttpUtil.forward(request, response, "/WEB-INF/pages/fail.jsp");
		}
	}
	private boolean isInvalidList(@SuppressWarnings("rawtypes") List list) {
		if(list==null) {
			return false;
		}else {
			return true;
		}
	}
	
	private void readMany(HttpServletRequest request,HttpServletResponse response, Auth auth) throws IOException {
		ArrayList<Member> list = null;
		if(AuthManager.canReadMemberList(auth)) { // ?????????X
			response.sendRedirect("index.jsp");
			return;
		}
		String group = request.getParameter("group");
		Integer permission = -1;
		if(request.getParameter("permission")!=null) // ????????????.
			permission = Integer.parseInt(request.getParameter("permission")); // ????????? ?????????????????? ????????? ??????
		if(group==null && permission==-1) { // ????????? ??????.
			System.out.println("???????????????");
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
	
	private void update(HttpServletRequest request, HttpServletResponse response, Auth auth)
			throws Exception{
		boolean pwCheckEnable = false;
		Member member = new Member(request, Permission.GUEST,pwCheckEnable);
		if(MemberService.update(member)) {
			request.setAttribute("ok_body", "?????? ?????????????????????.");
			request.setAttribute("forward_url", "myinfo.do");
			HttpUtil.forward(request, response, "/WEB-INF/pages/ok.jsp");
		}else {
			request.setAttribute("err_body", "?????? ?????????????????????.");
			request.setAttribute("forward_url", "myinfo.do");
			HttpUtil.forward(request, response, "/WEB-INF/pages/fail.jsp");
		}
	}
	
	private void updatePermissionAnddeleteSessionIfSelfUpdate(HttpServletRequest request,HttpServletResponse response, Auth auth) throws Exception {
		HttpSession session = request.getSession();
		String id = request.getParameter("id"); //?????????????????? ????????? ?????????
		Permission targetOldPermission = MemberService.getMember(id).getPermission(); // ???????????? ????????? ?????? ????????????.
		Permission targetPermission = Permission.intToPermission(Integer.parseInt(request.getParameter("edit_per"))); //??????????????? ????????????
		Permission permission = Permission.intToPermission((int)session.getAttribute("permission")); // ???????????? ???????????? ??????
		Member member = new Member(id);
		String admin_id = (String)session.getAttribute("id"); // ?????? ????????????????????? ?????????
		if(isInvalidPermissions(permission, targetOldPermission, targetPermission)) {
			if(MemberService.update_member_per(member, Permission.permissionToInt(targetPermission))) {//??????
				if(admin_id.contentEquals(id)) { // ?????? ????????? ????????? ?????? ??????. ??????????????????
					session.invalidate();
					response.sendRedirect("index.jsp");
				}else { // ????????? ???????????? ????????? ??????????????? ?????? ??????
					response.sendRedirect("memberlist.do?group="+URLEncoder.encode("??????", "UTF-8")+"&permission="+Permission.permissionToInt(targetPermission));
				}
			}else {//??????
				request.setAttribute("err_body", "????????? ????????????.");
				request.setAttribute("forward_url", "memberlist.do?group="+URLEncoder.encode("??????", "UTF-8")+"&permission=7");
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
			request.setAttribute("err_body", "???????????? ??????????????? ?????????????????????.");
			request.setAttribute("forward_url", "login.jsp");
			HttpUtil.forward(request, response, "/WEB-INF/pages/fail.jsp");
		}
	}
	
	private void resetMemberPassword(HttpServletRequest request, HttpServletResponse response, Auth auth){
		String id = request.getParameter("id");
		Member member = MemberService.getMember(id);
		Permission memberPermission = member.getPermission();
		if(canResetMemberPassword(auth, memberPermission)) {
			String pw = MemberService.reset_member_pw(id);
			if(pw.contentEquals("")) {
				request.setAttribute("err_body", "??? ??? ?????? ????????? ???????????? ?????????????????????.");
				request.setAttribute("forward_url", "index.jsp");
				HttpUtil.forward(request, response, "/WEB-INF/pages/fail.jsp");
			}else {
				request.setAttribute("ok_body", id+"?????? ??????????????? "+pw+"??? ????????????????????????.");
				request.setAttribute("forward_url", "index.jsp");
				HttpUtil.forward(request, response, "/WEB-INF/pages/ok.jsp");
			}
		}else {
			request.setAttribute("err_body", "??? ????????? ??????????????? ????????? ??? ????????? ????????????");
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
