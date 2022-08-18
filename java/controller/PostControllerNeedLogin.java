package controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import auth.Auth;
import auth.AuthManager;
import vo.Board;
import vo.Comment;
import vo.Permission;
import vo.Post;
import vo.Viewlog;
import service.BoardService;
import service.CommentService;
import service.PostService;
import service.ViewlogService;
import tools.HttpUtil;

public class PostControllerNeedLogin implements Controller,NeedLogin {
	private Board board;
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response, String action)
			throws ServletException, IOException {
		try {
			Auth auth = new Auth(request);
			if(action.contentEquals("getPostlist")) { // 게시글 목록가져오기
				read_many(request,response,auth);
			}else if(action.contentEquals("getPost")) { // 게시글 하나 가져오기
				read(request,response,auth);
			}else if(action.contentEquals("filedownload")){
				download(request,response);
			}else {
				HttpUtil.forward(request, response, "/WEB-INF/pages/fail.jsp");
			}
		}catch(Exception e) {
			e.printStackTrace();
			HttpUtil.forward(request, response, "/WEB-INF/pages/fail.jsp");
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
					Post.blindPostProcessing(auth, post);
					request.setAttribute("post", post);
					ArrayList<ArrayList<Comment>> comments = CommentService.getCommentList_v2(pid);
					comments = blindCommentProcessing(auth, comments);
				}else { 
					request.setAttribute("err_body", "삭제된 게시글입니다.");
					request.setAttribute("forward_url", "postsview.do?board_name="+board_name+"&page=1");
					HttpUtil.forward(request, response, "/WEB-INF/pages/fail.jsp");
				}
			}else { // 게시글이 삭제되지 않은 경우
				Post.checkCsrf(post);
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
	
	private ArrayList<ArrayList<Comment>> blindCommentProcessing(Auth auth,ArrayList<ArrayList<Comment>> comments){
		for(ArrayList<Comment> child_comments : comments) {
			for(Comment comment : child_comments) {
				if(comment.isBlind()) {
					if(AuthManager.canManageBoard(auth, board)) {
						Comment.removeBlindForAdmin(comment);
					}else {
						Comment.blindComment(comment);
					}
				}
			}
		}
		return comments;
	}
	
	private void read_many(HttpServletRequest request, HttpServletResponse response,Auth auth)
			throws ServletException, IOException{
		String boardName = request.getParameter("board_name");
		Board board = BoardService.getBoard(boardName);
		if(!AuthManager.canReadBoard(auth, board)) { // 읽기권한 있는 게시판인지 확인
			response.sendRedirect("index.jsp");
		}
		Map<String, String> boardDescription = makeBoardDescription(request);
		for(String key : boardDescription.keySet()) {
			request.setAttribute(key,boardDescription.get(key));
		}
		ArrayList<Post> posts = PostService.getPostlist(boardName,Integer.parseInt(boardDescription.get("nowPage")));
		request.setAttribute("postlist", posts);
		request.setAttribute("boardDescription", boardDescription);
		HttpUtil.forward(request, response, "WEB-INF/pages/postsview.jsp");
	}
	private Map<String,String> makeBoardDescription(HttpServletRequest request){
		String boardName = request.getParameter("board_name");
		Board board = BoardService.getBoard(boardName);
		Integer page = Integer.parseInt(request.getParameter("page"));
		ArrayList<Post> postlist = PostService.getPostlist(boardName,page);
		int nowPage = page;
		int lastPage = postlist.size()/10;
		if(postlist.size()%10!=0)
			lastPage++;
		int startPage, endPage;
		if(nowPage>lastPage) {
			nowPage =1;
			startPage =1; 
			endPage = lastPage>10 ? 10 :lastPage;
		}else {
			if(nowPage%10!=0) {
				startPage = (int)(Math.floor((double)nowPage /10))*10 + 1;
				endPage = (int)(Math.ceil((double)nowPage /10))*10;
			}else {
				startPage = (int)(Math.floor((double)(nowPage-1) /10))*10 + 1;
				endPage = nowPage;
			}
			if(endPage>lastPage) {
				endPage = lastPage;
			}
		}
		Permission readPermission = board.getReadPermission();
		Permission writePermission = board.getWritePermission();
		Permission commentPermission = board.getCommentPermission();
		Permission managePermission = board.getManagePermission();
		String read = Permission.permissionToString(readPermission);
		String write = Permission.permissionToString(writePermission);
		String comment = Permission.permissionToString(commentPermission);
		String manage = Permission.permissionToManageString(managePermission);
		Map<String,String> boardDescription = new HashMap<>();
		boardDescription.put("read", read);
		boardDescription.put("write", write);
		boardDescription.put("comment", comment);
		boardDescription.put("manage", manage);
		boardDescription.put("startPage", startPage+"");
		boardDescription.put("endPage", endPage+"");
		boardDescription.put("lastPage", lastPage+"");
		boardDescription.put("nowPage", nowPage+"");
		return boardDescription;
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
