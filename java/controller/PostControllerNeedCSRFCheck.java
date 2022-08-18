package controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

import auth.Auth;
import auth.AuthManager;
import vo.Board;
import vo.Comment;
import vo.Member;
import vo.Noti;
import vo.Permission;
import vo.Post;
import vo.Viewlog;
import service.BoardService;
import service.CommentService;
import service.MemberService;
import service.NotiService;
import service.PostService;
import service.ViewlogService;
import tools.HttpUtil;

public class PostControllerNeedCSRFCheck implements Controller {
	private Board board;
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response, String action)
			throws ServletException, IOException {
		try {
			Auth auth = new Auth(request);
			if(action.contentEquals("write")) {
				create(request,response,auth);
			}else if(action.contentEquals("delete_post")) {
				delete(request,response,auth);
			}else if(action.contentEquals("edit_post")) {
				update(request,response,auth);
			}else {
				HttpUtil.forward(request, response, "/WEB-INF/pages/fail.jsp");
			}
		}catch(Exception e) {
			e.printStackTrace();
			HttpUtil.forward(request, response, "/WEB-INF/pages/fail.jsp");
		}
	}
	private void create(HttpServletRequest request, HttpServletResponse response, Auth auth)
			throws Exception{
		Post post = uploadFileAndGetPostFromRequest(request);
		String board_name = post.getBoard_name();
		board = BoardService.getBoard(board_name);
		if(!AuthManager.canManageBoard(auth, board)) {
			Post.deleteNotice(post);
		}
		boolean is_notice = post.is_notice();
		if(AuthManager.canWriteBoard(auth, board)) {
			int pid = PostService.writepost_v2(post);
			Post.addPid(post, pid);
			if(is_notice && AuthManager.canManageBoard(auth, board)) {
				senNotification(request, post);
			}
			request.setAttribute("ok_body", "작성 성공");
			request.setAttribute("forward_url", "postview.do?pid="+pid+"&board_name="+board_name);
			HttpUtil.forward(request, response, "/WEB-INF/pages/ok.jsp");
		}else {
			request.setAttribute("err_body", "쓰기권한이 없습니다.");
			request.setAttribute("forward_url", "postsview.do?board_name="+board_name+"&page=1");
			HttpUtil.forward(request, response, "/WEB-INF/pages/fail.jsp");
		}
	}
	private Post uploadFileAndGetPostFromRequest(HttpServletRequest request) throws Exception {
		HttpSession session = request.getSession();
		String writer = (String)session.getAttribute("id");
		String savePath = getSavePathWhenCreate(request);
		int sizeLimit = 1024*1024*40; // 파일 최대 크기. 현재 40M
		MultipartRequest multi = new MultipartRequest(request, savePath,sizeLimit,"UTF-8",
				new DefaultFileRenamePolicy()); // 파일업로드 실행.
		Post post = new Post(multi, writer);
		return post;
	}
	private String getSavePathWhenCreate(HttpServletRequest request) {
		String savePath = request.getServletContext().getRealPath("data"); 
		File folder = new File(savePath);
		String up = folder.getParent();
		folder = new File(up);
		up = folder.getParent();
		folder = new File(up);
		up = folder.getParent();
		System.out.println(up);
		Cookie[] cookies = request.getCookies();
		for(int i=0;i<cookies.length;i++) {
			Cookie c = cookies[i];
			if(c.getName().contentEquals("board_name")) {
				up = up + File.separator+"data"+File.separator+c.getValue();
			}
		}
		folder = new File(up); // webapps 상위에 data아래에 게시판 이름으로 폴더있음.
		if(!folder.exists()) {
			try{
			    folder.mkdir(); //폴더 생성합니다.
			    System.out.println("폴더가 생성되었습니다.");
		        } 
		        catch(Exception e){
			    e.getStackTrace();
			}        
		}
		savePath = up;
		return savePath;
	}
	private void senNotification(HttpServletRequest request, Post post) {
		ArrayList<Member> memberlist = MemberService.getMemberList(7, "전체");
		for(Member member : memberlist) {
			if(!member.getScholastic().contentEquals("탈퇴") && AuthManager.canReadBoard(new Auth(member.getId(),member.getPermission()), board)) { // 탈퇴회원이나 게스트가 아닌사람에게만 알림 보냄.
				String receiver = member.getId();
				Noti noti = Noti.getNoticeNoti(post, receiver);
				NotiService.send_noti(noti);
			}
		}
	}
	
	
	private void update(HttpServletRequest request, HttpServletResponse response, Auth auth)
			throws Exception{
		Post post = new Post(request);
		int pid = post.getPid();
		String boardName = post.getBoard_name();
		if(AuthManager.canWriteBoard(auth, board)) { // 수정시 게시판의 쓰기권한이 있는지 확인
			if(PostService.modify_post(post)) {//수정성공
				response.sendRedirect("postview.do?pid="+pid);
			}else {//작성실패
				request.setAttribute("err_body", "db에러");
				request.setAttribute("forward_url", "postview.do?pid="+pid+"&board_name="+boardName);
				HttpUtil.forward(request, response, "/WEB-INF/pages/fail.jsp");
			}	
		}else {
			request.setAttribute("err_body", "권한없음");
			request.setAttribute("forward_url", "postview.do?pid="+pid+"&board_name="+boardName);
			HttpUtil.forward(request, response, "/WEB-INF/pages/fail.jsp");
		}
	}
	
	private void delete(HttpServletRequest request, HttpServletResponse response, Auth auth)
			throws ServletException, IOException{
		Post post = Post.getPostFromRequestWhenDelete(request);
		int pid = post.getPid();
		String board_name = post.getBoard_name();
		board = BoardService.getBoard(board_name);
		if(AuthManager.canManageBoard(auth, board)) {//로그인된 사용자가 글 작성자와 같거나 관리자인지 검사
			if(PostService.deletepost(post)) { // 삭제 성공
				request.setAttribute("ok_body", "게시글 삭제 성공");
				HttpUtil.forward(request, response, "/WEB-INF/pages/ok.jsp");
			}else { // 삭제 실패
				request.setAttribute("err_body", "db에러");
				request.setAttribute("forward_url", "postview.do?pid="+pid+"&board_name="+board_name);
				HttpUtil.forward(request, response, "/WEB-INF/pages/fail.jsp");
			}
		}else { // //작성자 이름과 로그인된 사용자가 같지 않고, 관리자가 아니면
			request.setAttribute("err_body", "권한에러");
			request.setAttribute("forward_url", "postview.do?pid="+pid+"&board_name="+board_name);
			HttpUtil.forward(request, response, "/WEB-INF/pages/fail.jsp");
		}
	}
}
