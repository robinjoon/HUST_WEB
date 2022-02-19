package controller;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import tools.HttpUtil;
import tools.Tag;
import vo.Comment;
import vo.Member;
import vo.Noti;
import service.BoardService;
import service.CommentService;
import service.MemberService;
import service.NotiService;
import service.PostService;

public class CommentController implements Controller {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response, String action)
			throws ServletException, IOException {
		if(action.contentEquals("write")) { // 댓글작성
			create(request,response);
		}else if(action.contentEquals("edit")){ //댓글 수정
			update(request,response);
		}else if(action.contentEquals("delete")) { // 댓글 삭제
			delete(request,response);
		}else {
			response.sendRedirect("index.jsp");
		}
	}
	
	private void create(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException{
		int pid = Integer.parseInt(request.getParameter("pid"));
		long parent = -1;
		try {
			parent = Long.parseLong(request.getParameter("parent"));
		}catch(NumberFormatException e) {
			parent = -1;
		}
		String content = request.getParameter("content");
		String tags = request.getParameter("tags");
		ArrayList<String> taglist = Tag.extraction(tags,BoardService.getPermissions_by_Pid(pid).get("read")); // 작성된 태그로부터 태그된 회원리스트 생성.
		Comment comment = new Comment();
		comment.setPid(pid);
		comment.setContent(content);
		comment.setParent(parent);
		HttpSession session = request.getSession();
		String id = (String)session.getAttribute("id");
		String board_name = request.getParameter("board_name");
		int permission = (Integer)session.getAttribute("permission");
		String csrf_token_server = (String)session.getAttribute("csrf_token");
		String csrf_token_client = request.getParameter("csrf_token");
		if(id!=null && csrf_token_server!=null && csrf_token_client!=null) { // 로그인여부 확인.
			comment.setWriter(id);
			if(permission>=BoardService.getPermissions_by_Pid(pid).get("comment")) { // 쓰기권한 확인
				if(csrf_token_server.contentEquals(csrf_token_client)) { // csrf 토큰 체크
					if(CommentService.write(comment)) { // 댓글 작성 성공
						if(parent!=-1) {
							Comment parent_comment = CommentService.getComment(parent);
							String parent_writer = parent_comment.getWriter();
							Member parentwriter = MemberService.getMember(parent_writer);
							if(parentwriter.isMycomment_comment_noti_allow()&&!id.contentEquals(parent_writer)) {
								Noti noti = new Noti();
								noti.setSender(id);
								noti.setReceiver(parent_writer);
								noti.setTitle("내가 쓴 댓글에 새 대댓글이 등록되었습니다.");
								noti.setBody(comment.getWriter()+" : "+comment.getContent());
								noti.setUrl("postview.do?pid="+pid+"&board_name="+board_name);
								noti.setDate(new java.sql.Date(new java.util.Date().getTime()));
								noti.setNotice(false);
								NotiService.send_noti(noti);
							}
						}
						String post_writer = PostService.getPost(pid).getWriter();
						Member postwriter = MemberService.getMember(post_writer);
						if(postwriter.isMypost_comment_noti_allow()&&!id.contentEquals(post_writer)) {
							Noti noti = new Noti();
							noti.setSender(id);
							noti.setReceiver(post_writer);
							noti.setTitle("내가 쓴 게시글에 새 댓글이 등록되었습니다.");
							noti.setBody(comment.getWriter()+" : "+comment.getContent());
							noti.setUrl("postview.do?pid="+pid+"&board_name="+board_name);
							noti.setDate(new java.sql.Date(new java.util.Date().getTime()));
							noti.setNotice(false);
							NotiService.send_noti(noti);
						}
						if(!taglist.isEmpty()) {
							for(int i=0;i<taglist.size();i++) {
								Member member = MemberService.getMember(taglist.get(i));
								if(member!=null && member.isCall_noti_allow()) {
									Noti noti = new Noti();
									noti.setSender(id);
									noti.setReceiver(taglist.get(i));
									noti.setTitle(id+"님이 댓글에 태그하셨습니다.");
									noti.setBody(comment.getContent());
									noti.setUrl("postview.do?pid="+pid+"&board_name="+board_name);
									noti.setDate(new java.sql.Date(new java.util.Date().getTime()));
									noti.setNotice(false);
									NotiService.send_noti(noti);
								}
							}
						}
						request.setAttribute("ok_body", "작성 성공");
						request.setAttribute("forward_url", "postview.do?pid="+pid+"&board_name="+board_name);
						HttpUtil.forward(request, response, "/WEB-INF/pages/ok.jsp");
					}else { // 댓글 작성 실패
						request.setAttribute("err_body", "db 에러");
						request.setAttribute("forward_url", "postview.do?pid="+pid+"&board_name="+board_name);
						HttpUtil.forward(request, response, "/WEB-INF/pages/fail.jsp");
					}
				}else {
					request.setAttribute("err_body", "csrf 토큰이 일치하지 않습니다. 알맞은 접근으로 시도하세요.");
					request.setAttribute("forward_url", "index.jsp");
					HttpUtil.forward(request, response, "/WEB-INF/pages/fail.jsp");
				}
			}else{ // 쓰기권한 없음
				request.setAttribute("err_body", "권한없음");
				request.setAttribute("forward_url", "postview.do?pid="+pid+"&board_name="+board_name);
				HttpUtil.forward(request, response, "/WEB-INF/pages/fail.jsp");
			}
		}else { // 로그인 x
			response.sendRedirect("index.jsp");
		}
	}
	private void update(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException{
		long cid = Long.parseLong(request.getParameter("cid"));
		int pid = Integer.parseInt(request.getParameter("pid"));
		long parent = -1;
		try {
			parent = Long.parseLong(request.getParameter("parent"));
		}catch(NumberFormatException e) {
			parent = -1;
		}
		String content = request.getParameter("content");
		Comment comment = new Comment();
		comment.setCid(cid);
		comment.setContent(content);
		comment.setParent(parent);
		HttpSession session = request.getSession();
		String id = (String)session.getAttribute("id");
		String board_name = request.getParameter("board_name");
		String csrf_token_server = (String)session.getAttribute("csrf_token");
		String csrf_token_client = request.getParameter("csrf_token");
		if(id!=null && csrf_token_server!=null && csrf_token_client!=null) {//로그인 여부 확인
			comment.setWriter(id); // 로그인된 사용자를 작성자 취급. sql에서 권한있는 사용자인지 검사.
			if(csrf_token_server.contentEquals(csrf_token_client)) { // csrf 토큰 체크
				if(CommentService.modify(comment)) {//수정성공
					request.setAttribute("forward_url", "postview.do?pid="+pid+"&board_name="+board_name);
					HttpUtil.forward(request, response, "/WEB-INF/pages/ok.jsp");
				}else {//수정실패
					request.setAttribute("err_body", "db에러");
					request.setAttribute("forward_url", "postview.do?pid="+pid+"&board_name="+board_name);
					HttpUtil.forward(request, response, "/WEB-INF/pages/fail.jsp");
				}
			}else {
				request.setAttribute("err_body", "csrf 토큰이 일치하지 않습니다. 알맞은 접근으로 시도하세요.");
				request.setAttribute("forward_url", "index.jsp");
				HttpUtil.forward(request, response, "/WEB-INF/pages/fail.jsp");
			}
		}else { //로그인X
			response.sendRedirect("index.jsp");
		}
	}
	private void delete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException{
		int pid = Integer.parseInt(request.getParameter("pid"));
		long cid = Long.parseLong(request.getParameter("cid"));
		String writer = request.getParameter("writer");
		Comment comment = new Comment();
		comment.setCid(cid);
		comment.setWriter(writer);
		HttpSession session = request.getSession();
		String board_name= request.getParameter("board_name");
		String id = (String)session.getAttribute("id");
		int permission = (int)session.getAttribute("permission");
		int manage_per = BoardService.getPermissions_by_Pid(pid).get("manage");
		String csrf_token_server = (String)session.getAttribute("csrf_token");
		String csrf_token_client = request.getParameter("csrf_token");
		if(id!=null && csrf_token_server!=null && csrf_token_client!=null ) { // 로그인되어있으면
			if(id.equals(writer)||permission==manage_per || permission==6 || (manage_per==7&&permission>=4)) { // 댓글작성자이거나 관리자인 경우
				if(csrf_token_server.contentEquals(csrf_token_client)) { // csrf 토큰 체크
					if(CommentService.delete(comment)) {//삭제성공
						HttpUtil.forward(request, response, "postview.do?pid="+pid+"&board_name="+board_name);
					}else {//삭제실패
						request.setAttribute("err_body", "db에러");
						HttpUtil.forward(request, response, "/WEB-INF/pages/fail.jsp");
					}
				}else {
					request.setAttribute("err_body", "csrf 토큰이 일치하지 않습니다. 알맞은 접근으로 시도하세요.");
					request.setAttribute("forward_url", "index.jsp");
					HttpUtil.forward(request, response, "/WEB-INF/pages/fail.jsp");
				}
			}else {//권한없음
				request.setAttribute("err_body", "권한에러");
				request.setAttribute("forward_url", "postview.do?pid="+pid+"&board_name="+board_name);
				HttpUtil.forward(request, response, "/WEB-INF/pages/fail.jsp");
			}
		}else {//로그인 X
			response.sendRedirect("index.jsp");
		}
	}
}
