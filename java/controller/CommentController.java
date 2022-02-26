package controller;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import auth.Auth;
import auth.AuthManager;
import exceptions.CreateCommentException;
import exceptions.DeleteCommentFailException;
import exceptions.UpdateCommentFailException;
import tools.HttpUtil;
import tools.Tag;
import vo.Board;
import vo.Comment;
import vo.Member;
import vo.Noti;
import vo.Permission;
import service.BoardService;
import service.CommentService;
import service.MemberService;
import service.NotiService;
import service.PostService;

public class CommentController implements Controller {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response, String action)
			throws ServletException, IOException {
		Auth auth = new Auth(request);
		if(AuthManager.loginCheck(auth)) {
			if(AuthManager.csrfCheck(auth)) {
				if(action.contentEquals("write")) { // 댓글작성
					create(request,response,auth);
				}else if(action.contentEquals("edit")){ //댓글 수정
					update(request,response,auth);
				}else if(action.contentEquals("delete")) { // 댓글 삭제
					delete(request,response,auth);
				}else {
					response.sendRedirect("index.jsp");
				}
			}else {
				response.sendRedirect("index.jsp");
			}
		}else {
			response.sendRedirect("index.jsp");
		}

	}
	
	private void create(HttpServletRequest request, HttpServletResponse response,Auth auth)
			throws ServletException, IOException{
		String tags = request.getParameter("tags");
		int pid = Integer.parseInt(request.getParameter("pid"));
		Board board = BoardService.getBoard(pid);
		ArrayList<String> taglist = Tag.extraction(tags,Permission.permissionToInt(board.getReadPermission())); // 작성된 태그로부터 태그된 회원리스트 생성.
		Comment comment = new Comment(request);
		long parent =comment.getParent();
		HttpSession session = request.getSession();
		String id = (String)session.getAttribute("id");
		if(AuthManager.canCommentBoard(auth, board)) { // 쓰기권한 확인
			try {
				CommentService.write(comment);
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
						noti.setUrl("postview.do?pid="+pid);
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
					noti.setUrl("postview.do?pid="+pid);
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
							noti.setUrl("postview.do?pid="+pid);
							noti.setDate(new java.sql.Date(new java.util.Date().getTime()));
							noti.setNotice(false);
							NotiService.send_noti(noti);
						}
					}
				}
				request.setAttribute("ok_body", "작성 성공");
				request.setAttribute("forward_url", "postview.do?pid="+pid);
				HttpUtil.forward(request, response, "/WEB-INF/pages/ok.jsp");
			}catch(CreateCommentException e) {
				request.setAttribute("err_body", "db 에러");
				request.setAttribute("forward_url", "postview.do?pid="+pid);
				HttpUtil.forward(request, response, "/WEB-INF/pages/fail.jsp");
			}
		}else{ // 쓰기권한 없음
			request.setAttribute("err_body", "권한없음");
			request.setAttribute("forward_url", "postview.do?pid="+pid);
			HttpUtil.forward(request, response, "/WEB-INF/pages/fail.jsp");
		}
	}
	private void update(HttpServletRequest request, HttpServletResponse response, Auth auth)
			throws ServletException, IOException{
		Comment comment = new Comment(request);
		Board board = BoardService.getBoard(comment.getPid());
		if(AuthManager.canCommentBoard(auth, board)) {
			try {
				CommentService.modify(comment);
				request.setAttribute("forward_url", "postview.do?pid="+comment.getPid());
				HttpUtil.forward(request, response, "/WEB-INF/pages/ok.jsp");
			}catch(UpdateCommentFailException e) {
				request.setAttribute("err_body", "db에러");
				request.setAttribute("forward_url", "postview.do?pid="+comment.getPid());
				HttpUtil.forward(request, response, "/WEB-INF/pages/fail.jsp");
			}
		}else {
			request.setAttribute("err_body", "권한없음");
			request.setAttribute("forward_url", "postview.do?pid="+comment.getPid());
			HttpUtil.forward(request, response, "/WEB-INF/pages/fail.jsp");
		}
	}
	private void delete(HttpServletRequest request, HttpServletResponse response, Auth auth)
			throws ServletException, IOException{
		Comment comment = new Comment(request);
		if(AuthManager.isWriter(auth, comment)) { // 댓글작성자이거나 관리자인 경우
			try{
				CommentService.delete(comment);
				HttpUtil.forward(request, response, "postview.do?pid="+comment.getPid());
			}catch (DeleteCommentFailException e) {//삭제실패
				request.setAttribute("err_body", "db에러");
				HttpUtil.forward(request, response, "/WEB-INF/pages/fail.jsp");
			}
		}else {//권한없음
			request.setAttribute("err_body", "권한에러");
			request.setAttribute("forward_url", "postview.do?pid="+comment.getPid());
			HttpUtil.forward(request, response, "/WEB-INF/pages/fail.jsp");
		}
	}
}
