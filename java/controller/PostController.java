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

import vo.CommentVO;
import vo.MemberVO;
import vo.NotiVO;
import vo.PostVO;
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

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response, String action)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		if(action.contentEquals("write")) {
			create(request,response);
		}else if(action.contentEquals("delete_post")) {
			delete(request,response);
		}else if(action.contentEquals("edit_post")) {
			update(request,response);
		}else if(action.contentEquals("getPostlist")) { // 게시글 목록가져오기
			read_many(request,response);
		}else if(action.contentEquals("getPost")) { // 게시글 하나 가져오기
			read(request,response);
		}else if(action.contentEquals("filedownload")){
			download(request,response);
		}else {
			HttpUtil.forward(request, response, "/WEB-INF/pages/fail.jsp");
		}
	}
	private void create(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException{
		HttpSession session = request.getSession();
		String id = (String)session.getAttribute("id");
		int permission = (int)session.getAttribute("permission");
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
		System.out.println(savePath);
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
			HttpUtil.forward(request, response, "/WEB-INF/pages/fail.jsp");
			return;
		}
		boolean is_notice = Boolean.parseBoolean(multi.getParameter("is_notice"));
		int manage = BoardService.getPermissions_by_name(board_name).get("manage");
		
		PostVO post = new PostVO();
		post.setWriter(id);
		post.setBoard_name(board_name);
		post.setTitle(title);
		post.setContent(Secure.check_script(content));
		post.setContent(post.getContent().replaceAll("<img ", "<img class='w-100' "));
		post.setOrigin_file_name(origin_file_name!=null ? origin_file_name : ""); // 첨부파일이 없을 때 첨부파일의 이름을 공백으로 설정. 안하면 pid 가져오는 쿼리에서 null로인해 정상적인 값을 가져오지 못함. 
		post.setSystem_file_name(system_file_name!=null ? system_file_name : "");
		if(is_notice && (permission!=manage && permission!=6)) // 관리자가 아닌 사람이 공지게시글로 설정한 경우 공지가 아니게 변경.
			is_notice=false;
		if(is_notice && (manage==7 && permission<4)) {
			is_notice=false;
		}
		post.setIs_notice(is_notice);
		String csrf_token_server = (String)session.getAttribute("csrf_token");
		String csrf_token_client = multi.getParameter("csrf_token");
		if (id != null && csrf_token_server!=null && csrf_token_client!=null) { // 로그인여부 확인
			if (id.equals(writer)) {// 작성자와 로그인된 사용자가 같은지 확인
				if(permission>=BoardService.getPermissions_by_name(board_name).get("write")) { // 작성권한 확인
					if(csrf_token_server.contentEquals(csrf_token_client)) { // csrf 체크
						int pid = PostService.writepost_v2(post);
						if (pid!=-1) {//작성성공
							if(is_notice&&(permission==manage||permission==6 || (manage==7&&permission>=4))) { // 공지게시글일 시 관리자가 글 쓰는 건지 확인 후 알림보냄.
								ArrayList<MemberVO> memberlist = MemberService.getMemberList(7, "전체");
								for(int i =0;i<memberlist.size();i++) {
									MemberVO member = memberlist.get(i);
									if(!member.getScholastic().contentEquals("탈퇴") && member.getPermission()>0 && BoardService.getPermissions_by_name(board_name).get("read")>=member.getPermission()) { // 탈퇴회원이나 게스트가 아닌사람에게만 알림 보냄.
										NotiVO noti = new NotiVO();
										noti.setSender(id);
										noti.setReceiver(member.getId());
										noti.setTitle(board_name+"에 새 공지가 등록되었습니다.");
										noti.setBody(post.getContent());
										noti.setUrl("postview.do?pid="+pid+"&board_name="+board_name);
										noti.setDate(new java.sql.Date(new java.util.Date().getTime()));
										noti.setNotice(true);
										NotiService.send_noti(noti);
									}
								}
							}
							request.setAttribute("ok_body", "작성 성공");
							request.setAttribute("forward_url", "postview.do?pid="+pid+"&board_name="+board_name);
							HttpUtil.forward(request, response, "/WEB-INF/pages/ok.jsp");
						} else {//작성실패
							request.setAttribute("err_body", "db에러");
							request.setAttribute("forward_url", "postsview.do?board_name="+board_name+"&page=1");
							HttpUtil.forward(request, response, "/WEB-INF/pages/fail.jsp");
						}
					}else {
						request.setAttribute("err_body", "csrf 토큰이 일치하지 않습니다. 알맞은 접근으로 시도하세요.");
						request.setAttribute("forward_url", "index.jsp");
						HttpUtil.forward(request, response, "/WEB-INF/pages/fail.jsp");
					}
				}else {
					request.setAttribute("err_body", "쓰기권한이 없습니다.");
					request.setAttribute("forward_url", "postsview.do?board_name="+board_name+"&page=1");
					HttpUtil.forward(request, response, "/WEB-INF/pages/fail.jsp");
				}
			}else { // 작성자 이름과 로그인된 사용자가 같지 않으면
				session.invalidate();
				response.sendRedirect("index.jsp");
			}
		}else{//로그인되어있지 않으면
			response.sendRedirect("index.jsp");
		}
	}
	private void read(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException{
		HttpSession session = request.getSession();
		String id = (String)session.getAttribute("id");
		int pid = Integer.parseInt(request.getParameter("pid"));
		String board_name = request.getParameter("board_name");
		Integer permission = (Integer)session.getAttribute("permission");
		int read_per = BoardService.getPermissions_by_Pid(pid).get("read");
		int manage = BoardService.getPermissions_by_Pid(pid).get("manage");
		int comment_per = BoardService.getPermissions_by_Pid(pid).get("comment");
		if(permission==null||read_per>permission) // 읽기권한 확인
			response.sendRedirect("index.jsp");
		PostVO post = PostService.getPost(pid);
		if(post.isBlind()) { // 게시글이 삭제된 경우
			if(!(permission==manage||permission==6 || (manage==7&&permission>=4))) { // 게시글이 삭제되었고 관리자가 아닌경우
				request.setAttribute("err_body", "삭제된 게시글입니다.");
				request.setAttribute("forward_url", "postsview.do?board_name="+board_name+"&page=1");
				HttpUtil.forward(request, response, "/WEB-INF/pages/fail.jsp");
			}else { // 게시글이 삭제되었지만 관리자인 경우
				String content = post.getContent();
				post.setContent("원래 제목 : "+post.getTitle()+"<br>"+post.getContent());
				post.setTitle("이 게시글은 삭제된 게시글입니다.");
				content = Secure.check_script(content);
				post.setContent(content);
				request.setAttribute("post", post);
				ArrayList<ArrayList<CommentVO>> comments = CommentService.getCommentList_v2(pid);
				if(comments!=null) {
					for(int i =0;i<comments.size();i++) {
						ArrayList<CommentVO> child_comments = comments.get(i);
						for(int j=0;j<child_comments.size();j++) {
							CommentVO comment = child_comments.get(j);
							if(comment.isBlind()) {
								if(permission==manage||permission==6 || (manage==7&&permission>=4)) {
									comment.setContent("삭제된 댓글입니다. 내용은 다음과 같습니다.\n"+comment.getContent());
								}else {
									comment.setContent("삭제된 댓글입니다.");
								}
							}
						}
					}
				}
			}
		}else { // 게시글이 삭제되지 않은 경우
			String title = post.getTitle();
			String content = post.getContent();
			title = Secure.check_input(title);
			content = Secure.check_script(content);
			post.setTitle(title);
			post.setContent(content);
			request.setAttribute("post", post);
			ArrayList<ArrayList<CommentVO>> comments = CommentService.getCommentList_v2(pid);
			if(comments!=null) {
				for(int i =0;i<comments.size();i++) {
					ArrayList<CommentVO> child_comments = comments.get(i);
					for(int j=0;j<child_comments.size();j++) {
						CommentVO comment = child_comments.get(j);
						if(comment.isBlind()) {
							if(permission==manage||permission==6 || (manage==7&&permission>=4)) {
								comment.setContent("삭제된 댓글입니다. 내용은 다음과 같습니다.\n"+comment.getContent());
							}else {
								comment.setContent("삭제된 댓글입니다.");
							}
						}
					}
				}
			}
			Viewlog view = new Viewlog();
			view.setId(id); view.setPid(pid);
			boolean viewlog = ViewlogService.viewlogging(view);
			if(viewlog) {
				PostService.increase_views(pid);
			}
			request.setAttribute("comments", comments);
			request.setAttribute("manage_per", manage);
			request.setAttribute("comment_per", comment_per);
			HttpUtil.forward(request, response, "WEB-INF/pages/postview.jsp");
		}
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
		ArrayList<PostVO> postlist = PostService.getPostlist(bname,page);
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
	private void update(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException{
		HttpSession session = request.getSession();
		Integer permission = (Integer)session.getAttribute("permission");
		int pid = Integer.parseInt(request.getParameter("pid"));
		String board_name = request.getParameter("board_name");
		int manage = BoardService.getPermissions_by_name(board_name).get("manage");
		String title = request.getParameter("title");
		String content = request.getParameter("content");
		boolean is_notice = Boolean.parseBoolean(request.getParameter("is_notice"));
		PostVO post = new PostVO();
		post.setPid(pid);post.setBoard_name(board_name);
		post.setTitle(title);post.setContent(content);
		if(is_notice && (permission!=manage && permission!=6)) // 관리자가 아닌 사람이 공지게시글로 설정한 경우 공지가 아니게 변경.
			is_notice=false;
		post.setIs_notice(is_notice);
		String id = (String)session.getAttribute("id");
		String csrf_token_server = (String)session.getAttribute("csrf_token");
		String csrf_token_client = request.getParameter("csrf_token");
		if(id!=null && csrf_token_server!=null && csrf_token_client!=null) { // 로그인 여부 확인
			post.setWriter(id);
			if(permission>=BoardService.getPermissions_by_name(board_name).get("write")) { // 수정시 게시판의 쓰기권한이 있는지 확인
				if(csrf_token_client.contentEquals(csrf_token_server)) { // csrf 방지 코드
					if(PostService.modify_post(post)) {//수정성공
						response.sendRedirect("postview.do?pid="+pid);
					}else {//작성실패
						request.setAttribute("err_body", "db에러");
						request.setAttribute("forward_url", "postview.do?pid="+pid+"&board_name="+board_name);
						HttpUtil.forward(request, response, "/WEB-INF/pages/fail.jsp");
					}	
				}else { // csrf 의심상황.
					request.setAttribute("err_body", "csrf 토큰이 일치하지 않습니다. 알맞은 접근으로 시도하세요.");
					request.setAttribute("forward_url", "index.jsp");
					HttpUtil.forward(request, response, "/WEB-INF/pages/fail.jsp");
				}
			}else { // 수정시 게시판의 쓰기권한이 있는지 확인
				request.setAttribute("err_body", "권한없음");
				request.setAttribute("forward_url", "postview.do?pid="+pid+"&board_name="+board_name);
				HttpUtil.forward(request, response, "/WEB-INF/pages/fail.jsp");
			}
			
		}else {//로그인되어있지 않은 경우
			response.sendRedirect("index.jsp");
		}
	}
	private void delete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException{
		int pid = Integer.parseInt(request.getParameter("pid"));
		String writer = request.getParameter("writer");
		HttpSession session = request.getSession();
		String id = (String)session.getAttribute("id");
		String board_name = request.getParameter("board_name");
		Integer permission = (int)session.getAttribute("permission"); // 사실상 변조 불가
		int manage_per = BoardService.getPermissions_by_Pid(pid).get("manage");// 게시글이 속한 게시판의 관리권한
		PostVO post = new PostVO();
		post.setPid(pid);
		post.setBoard_name(board_name);
		post.setWriter(writer);
		String csrf_token_server = (String)session.getAttribute("csrf_token");
		String csrf_token_client = request.getParameter("csrf_token");
		if(id!=null && csrf_token_server!=null && csrf_token_client!=null) {// 로그인되어있는지 검사
			if(permission==manage_per ||permission==6 || (manage_per==7&&permission>=4) || writer.equals(session.getAttribute("id"))) {//로그인된 사용자가 글 작성자와 같거나 관리자인지 검사
				if(csrf_token_client.contentEquals(csrf_token_server)) { // csrf 방지 코드
					if(PostService.deletepost(post)) { // 삭제 성공
						request.setAttribute("ok_body", "게시글 삭제 성공");
						HttpUtil.forward(request, response, "/WEB-INF/pages/ok.jsp");
					}else { // 삭제 실패
						request.setAttribute("err_body", "db에러");
						request.setAttribute("forward_url", "postview.do?pid="+pid+"&board_name="+board_name);
						HttpUtil.forward(request, response, "/WEB-INF/pages/fail.jsp");
					}
				}else { // csrf 의심상황.
					request.setAttribute("err_body", "csrf 토큰이 일치하지 않습니다. 알맞은 접근으로 시도하세요.");
					request.setAttribute("forward_url", "index.jsp");
					HttpUtil.forward(request, response, "/WEB-INF/pages/fail.jsp");
				}
			}else { // //작성자 이름과 로그인된 사용자가 같지 않고, 관리자가 아니면
				request.setAttribute("err_body", "권한에러");
				request.setAttribute("forward_url", "postview.do?pid="+pid+"&board_name="+board_name);
				HttpUtil.forward(request, response, "/WEB-INF/pages/fail.jsp");
			}
		}else {//로그인되어있지 않으면
			response.sendRedirect("index.jsp");
		}
	}
	private void download(HttpServletRequest request,HttpServletResponse response) throws IOException {
		String savePath = request.getServletContext().getRealPath("data"); // 실제 파일저장경로
		int pid = Integer.parseInt(request.getParameter("pid"));
		PostVO post = PostService.getPost(pid);
		File folder = new File(savePath);
		String up = folder.getParent();
		folder = new File(up);
		up = folder.getParent();
		folder = new File(up);
		up = folder.getParent();
		up = up+File.separator+"data"+File.separator+post.getBoard_name(); // 실제 경로 = webapps의 상위 디렉토리의 data디렉토리 내 게시판이름 폴더 
		savePath = up;
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

}
