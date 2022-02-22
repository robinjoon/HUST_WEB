package controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

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
import tools.Secure;

public class PostController implements Controller {
	private Board board;
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response, String action)
			throws ServletException, IOException {
		try {
			Auth auth = new Auth(request);
			if(AuthManager.loginCheck(auth)) {
				if(AuthManager.csrfCheck(auth)) {
					if(action.contentEquals("write")) {
						create(request,response,auth);
					}else if(action.contentEquals("delete_post")) {
						delete(request,response,auth);
					}else if(action.contentEquals("edit_post")) {
						update(request,response,auth);
					}else {
						HttpUtil.forward(request, response, "/WEB-INF/pages/fail.jsp");
					}
				}else {
					if(action.contentEquals("getPostlist")) { // 게시글 목록가져오기
						read_many(request,response);
					}else if(action.contentEquals("getPost")) { // 게시글 하나 가져오기
						read(request,response,auth);
					}else if(action.contentEquals("filedownload")){
						download(request,response);
					}else {
						HttpUtil.forward(request, response, "/WEB-INF/pages/fail.jsp");
					}
				}
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
			post.set_notice(false);
		}
		boolean is_notice = post.is_notice();
		if(AuthManager.canWriteBoard(auth, board)) {
			int pid = PostService.writepost_v2(post);
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
		String id = (String)session.getAttribute("id");
		String savePath = getSavePathWhenCreate(request);
		int sizeLimit = 1024*1024*40; // 파일 최대 크기. 현재 40M
		MultipartRequest multi = new MultipartRequest(request, savePath,sizeLimit,"UTF-8",
				new DefaultFileRenamePolicy()); // 파일업로드 실행.
		String writer = multi.getParameter("writer");
		String board_name = multi.getParameter("board_name");
		String title = multi.getParameter("title");
		String content = multi.getParameter("content");
		String origin_file_name = multi.getOriginalFileName("file");
		String system_file_name = multi.getFilesystemName("file");
		if(writer.isBlank()||board_name.isBlank()||title.isBlank()||content.isBlank()) {
			System.out.println(content);
			throw new Exception("no required data");
		}
		boolean is_notice = Boolean.parseBoolean(multi.getParameter("is_notice"));		
		Post post = new Post();
		post.setWriter(id);
		post.setBoard_name(board_name);
		post.setTitle(title);
		post.setContent(Secure.check_script(content));
		post.setContent(post.getContent().replaceAll("<img ", "<img class='w-100' "));
		post.setOrigin_file_name(origin_file_name!=null ? origin_file_name : ""); // 첨부파일이 없을 때 첨부파일의 이름을 공백으로 설정. 안하면 pid 가져오는 쿼리에서 null로인해 정상적인 값을 가져오지 못함. 
		post.setSystem_file_name(system_file_name!=null ? system_file_name : "");
		post.set_notice(is_notice);
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
		int pid = post.getPid();
		String boardName = post.getBoard_name();
		ArrayList<Member> memberlist = MemberService.getMemberList(7, "전체");
		for(Member member : memberlist) {
			if(!member.getScholastic().contentEquals("탈퇴") && AuthManager.canReadBoard(new Auth(member.getId(),member.getPermission()), board)) { // 탈퇴회원이나 게스트가 아닌사람에게만 알림 보냄.
				Noti noti = new Noti();
				String id = (String)request.getSession().getAttribute("id");
				noti.setSender(id);
				noti.setReceiver(member.getId());
				noti.setTitle(boardName+"에 새 공지가 등록되었습니다.");
				noti.setBody(post.getContent());
				noti.setUrl("postview.do?pid="+pid+"&board_name="+boardName);
				noti.setDate(new java.sql.Date(new java.util.Date().getTime()));
				noti.setNotice(true);
				NotiService.send_noti(noti);
			}
		}
	}
	private void read(HttpServletRequest request, HttpServletResponse response, Auth auth)
			throws Exception{
		int pid = Integer.parseInt(request.getParameter("pid"));
		String board_name = request.getParameter("board_name");
		Post post = PostService.getPost(pid);
		board = BoardService.getBoard(post.getBoard_name());
		if(!AuthManager.canReadBoard(auth, board)) { // 읽기권한 확인
			response.sendRedirect("index.jsp");
		}else {
			if(post.isBlind()) { // 게시글이 삭제된 경우
				if(AuthManager.canManageBoard(auth, board)) {
					post = blindPostProcessing(auth, post);
					request.setAttribute("post", post);
					ArrayList<ArrayList<Comment>> comments = CommentService.getCommentList_v2(pid);
					comments = blindCommentProcessing(auth, comments);
				}else { 
					request.setAttribute("err_body", "삭제된 게시글입니다.");
					request.setAttribute("forward_url", "postsview.do?board_name="+board_name+"&page=1");
					HttpUtil.forward(request, response, "/WEB-INF/pages/fail.jsp");
				}
			}else { // 게시글이 삭제되지 않은 경우
				post = checkCsrf(post);
				request.setAttribute("post", post);
				ArrayList<ArrayList<Comment>> comments = CommentService.getCommentList_v2(pid);
				comments = blindCommentProcessing(auth, comments);
				Viewlog viewlog = new Viewlog(request, post); 
				if(ViewlogService.viewlogging(viewlog)) {
					PostService.increase_views(pid);
				}
				request.setAttribute("comments", comments);
				request.setAttribute("manage_per", Permission.permissionToInt(board.getManagePermission()));
				request.setAttribute("comment_per", Permission.permissionToInt(board.getCommentPermission()));
				HttpUtil.forward(request, response, "WEB-INF/pages/postview.jsp");
			}
		}
	}
	private Post checkCsrf(Post post) {
		String title = post.getTitle();
		String content = post.getContent();
		title = Secure.check_input(title);
		content = Secure.check_script(content);
		post.setTitle(title);
		post.setContent(content);
		return post;
	}
	private Post blindPostProcessing(Auth auth,Post post){
		String content = post.getContent();
		post.setContent("원래 제목 : "+post.getTitle()+"<br>"+post.getContent());
		post.setTitle("이 게시글은 삭제된 게시글입니다.");
		content = Secure.check_script(content);
		post.setContent(content);
		return post;
	}
	private ArrayList<ArrayList<Comment>> blindCommentProcessing(Auth auth,ArrayList<ArrayList<Comment>> comments){
		for(ArrayList<Comment> child_comments : comments) {
			for(Comment comment : child_comments) {
				if(comment.isBlind()) {
					if(AuthManager.canManageBoard(auth, board)) {
						comment.setContent("삭제된 댓글입니다. 내용은 다음과 같습니다.\n"+comment.getContent());
					}else {
						comment.setContent("삭제된 댓글입니다.");
					}
				}
			}
		}
		return comments;
	}
	
	private void read_many(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException{
		String bname = request.getParameter("board_name");
		HttpSession session = request.getSession();
		int per = (Integer)session.getAttribute("permission");
		if(per<BoardService.getPermissions_by_name(bname).get("read")) { // 읽기권한 있는 게시판인지 확인
			response.sendRedirect("index.jsp");
		}
		Integer page = Integer.parseInt(request.getParameter("page"));
		ArrayList<Post> postlist = PostService.getPostlist(bname,page);
		int now = page;
		int total = postlist.size()/10;
		if(postlist.size()%10!=0)
			total++;
		int start, end;
		if(now>total) {
			now =1;
			start =1; 
			end = total>10 ? 10 :total;
		}else {
			if(now%10!=0) {
				start = (int)(Math.floor((double)now /10))*10 + 1;
				end = (int)(Math.ceil((double)now /10))*10;
			}else {
				start = (int)(Math.floor((double)(now-1) /10))*10 + 1;
				end = now;
			}
			if(end>total) {
				end = total;
			}
		}
		int manage_per = BoardService.getPermissions_by_name(bname).get("manage");
		String manage="";
		switch(manage_per) {
		case 4:
			manage="YB 운영자";
			break;
		case 5:
			manage="OB 운영자";
			break;
		case 6:
			manage="관리자";
			break;
		case 7:
			manage="YB/OB 공동관리";
			break;
		}
		int wp = BoardService.getPermissions_by_name(bname).get("write");
		int rp = BoardService.getPermissions_by_name(bname).get("read");
		int cp = BoardService.getPermissions_by_name(bname).get("comment");
		String read="",write="",comment="";
		switch(rp){
		case 1: 
			read = "신입회원이상";
			break;
		case 2: 
			read = "정회원이상";
			break;
		case 3: 
			read = "OB회원이상";
			break;
		case 4: 
			read = "YB운영진이상";
			break;
		case 5: 
			read = "OB운영진이상";
			break;
		case 6: 
			read = "관리자이상";
			break;
		}
		switch(wp){
		case 1: 
			write = "신입회원이상";
			break;
		case 2: 
			write = "정회원이상";
			break;
		case 3: 
			write = "OB회원이상";
			break;
		case 4: 
			write = "YB운영진이상";
			break;
		case 5: 
			write = "OB운영진이상";
			break;
		case 6: 
			write = "관리자이상";
			break;
		}
		switch(cp){
		case 1: 
			comment = "신입회원이상";
			break;
		case 2: 
			comment = "정회원이상";
			break;
		case 3: 
			comment = "OB회원이상";
			break;
		case 4: 
			comment = "YB운영진이상";
			break;
		case 5: 
			comment = "OB운영진이상";
			break;
		case 6: 
			comment = "관리자이상";
			break;
		}
		request.setAttribute("postlist", postlist);
		request.setAttribute("board_name",bname);
		request.setAttribute("total", total);
		request.setAttribute("now",now);
		request.setAttribute("start", start);
		request.setAttribute("end",end);
		request.setAttribute("manage", manage);
		request.setAttribute("read", read);
		request.setAttribute("write", write);
		request.setAttribute("comment", comment);
		HttpUtil.forward(request, response, "WEB-INF/pages/postsview.jsp");
	}
	
	private void update(HttpServletRequest request, HttpServletResponse response, Auth auth)
			throws Exception{
		Post post = getPostFromRequestWhenUpdate(request);
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
	private Post getPostFromRequestWhenUpdate(HttpServletRequest request) throws Exception {
		Auth auth = new Auth(request);
		int pid = Integer.parseInt(request.getParameter("pid"));
		String board_name = request.getParameter("board_name");
		String title = request.getParameter("title");
		String content = request.getParameter("content");
		boolean is_notice = Boolean.parseBoolean(request.getParameter("is_notice"));
		Post post = new Post();
		post.setPid(pid);post.setBoard_name(board_name);
		post.setTitle(title);post.setContent(content);
		board = BoardService.getBoard(board_name);
		if(is_notice && AuthManager.canManageBoard(auth, board)) // 관리자가 아닌 사람이 공지게시글로 설정한 경우 공지가 아니게 변경.
			is_notice=false;
		post.set_notice(is_notice);
		return post;
	}
	
	private void delete(HttpServletRequest request, HttpServletResponse response, Auth auth)
			throws ServletException, IOException{
		Post post = getPostFromRequestWhenDelete(request);
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
	private Post getPostFromRequestWhenDelete(HttpServletRequest request) {
		int pid = Integer.parseInt(request.getParameter("pid"));
		String writer = request.getParameter("writer");
		String board_name = request.getParameter("board_name");
		Post post = new Post();
		post.setPid(pid);
		post.setBoard_name(board_name);
		post.setWriter(writer);
		return post;
	}
	
	private void download(HttpServletRequest request,HttpServletResponse response) throws IOException {
		String savePath = getSavePathWhenDownload(request);
		int pid = Integer.parseInt(request.getParameter("pid"));
		Post post = PostService.getPost(pid);
		File file = new File(savePath+File.separator+post.getSystem_file_name());
		if(!file.exists()) {
			request.setAttribute("err_body","파일이 없습니다");
			request.setAttribute("forward_url", "postsview.do?board_name="+post.getBoard_name()+"&page=1");
			HttpUtil.forward(request, response, "WEB-INF/pages/fail.jsp");
			return;
		}
		// ③ MIMETYPE 설정하기
		String mimeType = request.getServletContext().getMimeType(file.toString());
		if(mimeType == null)
		{
			response.setContentType("application/octet-stream");
		}
		
		// ④ 다운로드용 파일명을 설정
		String downName = post.getOrigin_file_name();
		// ⑤ 무조건 다운로드하도록 설정
		response.setHeader("Content-Disposition","attachment;filename=\"" + new String(downName.getBytes("UTF-8"), "ISO-8859-1") + "\";");
		
		// ⑥ 요청된 파일을 읽어서 클라이언트쪽으로 저장한다.
		FileInputStream fileInputStream = new FileInputStream(file);
		ServletOutputStream servletOutputStream = response.getOutputStream();
		
		byte b [] = new byte[1024];
		int data = 0;
		
		while((data=(fileInputStream.read(b, 0, b.length))) != -1)
		{
			servletOutputStream.write(b, 0, data);
		}
		
		servletOutputStream.flush();
		servletOutputStream.close();
		fileInputStream.close();
	}
	private String getSavePathWhenDownload(HttpServletRequest request) {
		String savePath = request.getServletContext().getRealPath("data"); // 실제 파일저장경로
		int pid = Integer.parseInt(request.getParameter("pid"));
		Post post = PostService.getPost(pid);
		File folder = new File(savePath);
		String up = folder.getParent();
		folder = new File(up);
		up = folder.getParent();
		folder = new File(up);
		up = folder.getParent();
		up = up+File.separator+"data"+File.separator+post.getBoard_name(); // 실제 경로 = webapps의 상위 디렉토리의 data디렉토리 내 게시판이름 폴더 
		savePath = up;
		return savePath;
	}
}
