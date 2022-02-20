package controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import auth.AuthManager;
import service.BoardService;
import service.MemberService;
import vo.Board;
import vo.Permission;
import tools.HttpUtil;
public class BoardController implements Controller {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response, String action)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		try {
			if(action.contentEquals("create_board")) {
				create(request,response);
			}else if(action.contentEquals("delete_board")) {
				delete(request,response);
			}else if(action.contentEquals("update_board")) {
				update(request,response);
			}else {
				HttpUtil.forward(request, response, "/WEB-INF/pages/fail.jsp");
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private void create(HttpServletRequest request, HttpServletResponse response) 
			throws Exception {
		HttpSession session = request.getSession();
		String id  = (String)session.getAttribute("id");
		String board_name=request.getParameter("board_name");
		Permission rp =	Permission.intToPermission(Integer.parseInt(request.getParameter("rp")));
		Permission wp = Permission.intToPermission(Integer.parseInt(request.getParameter("wp")));
		Permission mp = Permission.intToPermission(Integer.parseInt(request.getParameter("mp")));
		Permission cp = Permission.intToPermission(Integer.parseInt(request.getParameter("cp")));
		String board_description = request.getParameter("board_description");
		Board board = new Board();
		board.setBoard_name(board_name);board.setReadPermission(rp);
		board.setWritePermission(wp);board.setManagePermission(mp);
		board.setCommentPermission(cp);
		board.setBoard_description(board_description);
		if(AuthManager.loginCheck(request)) { // 로그인 여부 확인
			if(AuthManager.csrfCheck(request)) { // csrf 토큰 체크
				if(AuthManager.canMakeBoard(MemberService.getMember(id), board)) { // 게시판 권한설정 확인
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
			}else {
				request.setAttribute("err_body", "csrf 토큰이 일치하지 않습니다. 알맞은 접근으로 시도하세요.");
				request.setAttribute("forward_url", "index.jsp");
				HttpUtil.forward(request, response, "/WEB-INF/pages/fail.jsp");
			}
		}else {// 로그인안되어있으면
			HttpUtil.forward(request, response, "index.jsp");
		}
	}
	private void update(HttpServletRequest request,HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
		String id  = (String)session.getAttribute("id");
		String board_name = request.getParameter("board_name");
		Permission rp =	Permission.intToPermission(Integer.parseInt(request.getParameter("rp")));
		Permission wp = Permission.intToPermission(Integer.parseInt(request.getParameter("wp")));
		Permission mp = Permission.intToPermission(Integer.parseInt(request.getParameter("mp")));
		Permission cp = Permission.intToPermission(Integer.parseInt(request.getParameter("cp")));
		String board_description = request.getParameter("board_description");
		Board board = new Board();
		board.setBoard_name(board_name);board.setReadPermission(rp);
		board.setWritePermission(wp);board.setManagePermission(mp);
		board.setCommentPermission(cp);
		board.setBoard_description(board_description);
		if(AuthManager.loginCheck(request)) { // 로그인 여부 확인
			if(AuthManager.csrfCheck(request)) { // csrf 토큰 체크
				if(AuthManager.canMakeBoard(MemberService.getMember(id), board)) { // 게시판 권한설정 확인
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
			}else {
				request.setAttribute("err_body", "csrf 토큰이 일치하지 않습니다. 알맞은 접근으로 시도하세요.");
				request.setAttribute("forward_url", "index.jsp");
				HttpUtil.forward(request, response, "/WEB-INF/pages/fail.jsp");
			}
		}else {// 로그인안되어있으면
			HttpUtil.forward(request, response, "index.jsp");
		}
	}
	private void delete(HttpServletRequest request,HttpServletResponse response) {
		HttpSession session = request.getSession();
		String id  = (String)session.getAttribute("id");
		String board_name = request.getParameter("board_name");
		Board board = new Board();
		board.setBoard_name(board_name);
		if(AuthManager.loginCheck(request)) { //로그인 여부 확인
			if(AuthManager.canManage(MemberService.getMember(id), board)) {// 권한 확인
				if(AuthManager.csrfCheck(request)) { // csrf 토큰 체크
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
					request.setAttribute("err_body", "csrf 토큰이 일치하지 않습니다. 알맞은 접근으로 시도하세요.");
					request.setAttribute("forward_url", "index.jsp");
					HttpUtil.forward(request, response, "/WEB-INF/pages/fail.jsp");
				}
			}else {
				session.invalidate();
				HttpUtil.forward(request, response, "index.jsp");
			}
		}else {
			HttpUtil.forward(request, response, "index.jsp");
		}
	}
}
