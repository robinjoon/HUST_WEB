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

public class MemberControllerNeedLogin implements Controller,NeedLogin {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response, String action)
			throws ServletException, IOException {
		Auth auth = new Auth(request);
		if(action.contentEquals("getMember")) {
			read(request,response,auth);
		}else if(action.contentEquals("myinfo")) {
			readMe(request,response,auth);
		}else if(action.contentEquals("memberlist")) {
			readMany(request,response,auth);
		} 
	}
	
	private void read(HttpServletRequest request, HttpServletResponse response, Auth auth)
			throws ServletException, IOException{
		String memberid = request.getParameter("member"); // 보려고 하는 멤버의 아이디.
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
		Member member = MemberService.getMember(id);
		ArrayList<Post> list = PostService.getMyPosts(id);
		ArrayList<Comment> clist = CommentService.getMyComments(id);
		if(member==null || isInvalidList(list) || isInvalidList(clist)) { // 멤버정보 가져오기 실패
			request.setAttribute("err_body", "알수 없는 오류로 멤버정보를 가져오는데 실패하였습니다.");
			request.setAttribute("forward_url", "index.jsp");
			HttpUtil.forward(request, response, "/WEB-INF/pages/fail.jsp");
		}else {
			request.setAttribute("list", list);
			request.setAttribute("clist", clist);
			request.setAttribute("member", member);
			HttpUtil.forward(request, response, "/WEB-INF/pages/myinfo.jsp");
		}
	}
	private boolean isInvalidList(@SuppressWarnings("rawtypes") List list) {
		if(list!=null) {
			return false;
		}else {
			return true;
		}
	}
	
	private void readMany(HttpServletRequest request,HttpServletResponse response, Auth auth) throws IOException {
		ArrayList<Member> list = null;
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
}
