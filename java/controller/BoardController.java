package controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import auth.Auth;
import auth.AuthManager;
import service.BoardService;
import vo.Board;
import tools.HttpUtil;
public class BoardController implements Controller {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response, String action)
			throws ServletException, IOException {
		try {
			Auth auth = new Auth(request);
			
			if(!AuthManager.loginCheck(auth)) {
				HttpUtil.forward(request, response, "index.jsp");
			}else if(!AuthManager.csrfCheck(auth)) {
				request.setAttribute("err_body", "csrf 토큰이 일치하지 않습니다. 알맞은 접근으로 시도하세요.");
				request.setAttribute("forward_url", "index.jsp");
				HttpUtil.forward(request, response, "/WEB-INF/pages/fail.jsp");
			}
			
			if(action.contentEquals("create_board")) {
				create(request,response,auth);
			}else if(action.contentEquals("delete_board")) {
				delete(request,response,auth);
			}else if(action.contentEquals("update_board")) {
				update(request,response,auth);
			}else {
				HttpUtil.forward(request, response, "/WEB-INF/pages/fail.jsp");
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private void create(HttpServletRequest request, HttpServletResponse response, Auth auth) 
			throws Exception {
		Board board = new Board(request);
		if(AuthManager.canMakeBoard(auth, board)) { // 게시판 권한설정 확인
			if(BoardService.createboard(board)) {
				request.setAttribute("ok_body", "게시판 생성 성공");
				request.setAttribute("forward_url", "admin.do");
				HttpUtil.forward(request, response, "/WEB-INF/pages/ok.jsp");
			}else {
				request.setAttribute("err_body", "db 에러");
				request.setAttribute("forward_url", "admin.do");
				HttpUtil.forward(request, response, "/WEB-INF/pages/fail.jsp");
			}
		}else {
			response.sendRedirect("index.jsp");
		}
	}
	private void update(HttpServletRequest request,HttpServletResponse response, Auth auth) throws Exception {
		Board board = new Board(request);
		if(AuthManager.canMakeBoard(auth, board)) { // 게시판 권한설정 확인
			if(BoardService.updateboard(board)) {
				request.setAttribute("ok_body", "게시판 생성 성공");
				request.setAttribute("forward_url", "admin.do");
				HttpUtil.forward(request, response, "/WEB-INF/pages/ok.jsp");
			}else {
				request.setAttribute("err_body", "db 에러");
				request.setAttribute("forward_url", "admin.do");
				HttpUtil.forward(request, response, "/WEB-INF/pages/fail.jsp");
			}
		}else {
			response.sendRedirect("index.jsp");
		}
	}
	private void delete(HttpServletRequest request,HttpServletResponse response, Auth auth) throws Exception {
		Board board = new Board(request.getParameter("board_name"));
		if(AuthManager.canManageBoard(auth, board)) {// 권한 확인
			if(BoardService.deleteBoard(board)) { // 삭제 성공
				request.setAttribute("ok_body", "게시판 삭제 성공");
				request.setAttribute("forward_url", "admin.do");
				HttpUtil.forward(request, response, "/WEB-INF/pages/ok.jsp");
			}else { //삭제 실패
				request.setAttribute("err_body", "db 에러");
				request.setAttribute("forward_url", "admin.do");
				HttpUtil.forward(request, response, "/WEB-INF/pages/fail.jsp");
			}
		}else {
			HttpUtil.forward(request, response, "index.jsp");
		}
	}
}
