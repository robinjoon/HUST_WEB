package controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import service.BoardService;
import vo.BoardVO;
import tools.HttpUtil;
public class BoardController implements Controller {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response, String action)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		if(action.contentEquals("create_board")) {
			create(request,response);
		}else if(action.contentEquals("delete_board")) {
			delete(request,response);
		}else if(action.contentEquals("update_board")) {
			update(request,response);
		}else {
			HttpUtil.forward(request, response, "/WEB-INF/pages/fail.jsp");
		}
	}
	
	private void create(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		Integer permission = (int)session.getAttribute("permission");
		String id  = (String)session.getAttribute("id");
		String board_name=request.getParameter("board_name");
		int rp = Integer.parseInt(request.getParameter("rp"));
		int wp = Integer.parseInt(request.getParameter("wp"));
		int mp = Integer.parseInt(request.getParameter("mp"));
		int cp = Integer.parseInt(request.getParameter("cp"));
		String board_description = request.getParameter("board_description");
		BoardVO board = new BoardVO();
		board.setBoard_name(board_name);board.setRead_permission(rp);
		board.setWrite_permission(wp);board.setManage_permission(mp);
		board.setComment_permission(cp);
		board.setBoard_description(board_description);
		String csrf_token_server = (String)session.getAttribute("csrf_token");
		String csrf_token_client = request.getParameter("csrf_token");
		if(id!=null && csrf_token_server!=null && csrf_token_client!=null) { // 로그인 여부 확인
			if(csrf_token_server.contentEquals(csrf_token_client)) { // csrf 토큰 체크
				if(permission==4) { // YB운영자인 경우
					if(rp>4 || wp>4|| mp!=4 || cp>4) { //게시판 권한이 유효한지 확인
						request.setAttribute("err_body", "할 수 없는 권한설정입니다.");
						request.setAttribute("forward_url", "admin.do");
						HttpUtil.forward(request, response, "/WEB-INF/pages/fail.jsp");
					}else{
						if(BoardService.createboard(board)) {
							request.setAttribute("ok_body", "게시판 생성 성공");
							request.setAttribute("forward_url", "admin.do");
							HttpUtil.forward(request, response, "/WEB-INF/pages/ok.jsp");
						}else {
							request.setAttribute("err_body", "db 에러");
							request.setAttribute("forward_url", "admin.do");
							HttpUtil.forward(request, response, "/WEB-INF/pages/fail.jsp");
						}
					}
				}else if(permission==5) { // OB운영자인 경우 
					if(rp>5 || wp>5|| mp!=5 || cp>5) { // 게시판권한이 유효한지 확인
						request.setAttribute("err_body", "할 수 없는 권한설정입니다.");
						request.setAttribute("forward_url", "admin.do");
						HttpUtil.forward(request, response, "/WEB-INF/pages/fail.jsp");
					}else{
						if(BoardService.createboard(board)) {
							request.setAttribute("ok_body", "게시판 생성 성공");
							request.setAttribute("forward_url", "admin.do");
							HttpUtil.forward(request, response, "/WEB-INF/pages/ok.jsp");
						}else {
							request.setAttribute("err_body", "db 에러");
							request.setAttribute("forward_url", "admin.do");
							HttpUtil.forward(request, response, "/WEB-INF/pages/fail.jsp");
						}
					}
				}else if(permission==6) { // 관리자인 경우
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
	private void update(HttpServletRequest request,HttpServletResponse response) throws IOException {
		HttpSession session = request.getSession();
		Integer permission = (int)session.getAttribute("permission");
		String id  = (String)session.getAttribute("id");
		String board_name = request.getParameter("board_name");
		int rp = Integer.parseInt(request.getParameter("rp"));
		int wp = Integer.parseInt(request.getParameter("wp"));
		int mp = Integer.parseInt(request.getParameter("mp"));
		int cp = Integer.parseInt(request.getParameter("cp"));
		String board_description = request.getParameter("board_description");
		BoardVO board = new BoardVO();
		board.setBoard_name(board_name);
		board.setBoard_description(board_description);
		String csrf_token_server = (String)session.getAttribute("csrf_token");
		String csrf_token_client = request.getParameter("csrf_token");
		if(id!=null && csrf_token_server!=null && csrf_token_client!=null) { // 로그인여부 확인
			if(csrf_token_server.contentEquals(csrf_token_client)) { // csrf 토큰 체크
				if(permission==4) { // YB운영자인 경우
					if(rp>4 || wp>4|| mp!=4 || cp>4) { //게시판 권한이 유효한지 확인
						request.setAttribute("err_body", "할 수 없는 권한설정입니다.");
						request.setAttribute("forward_url", "admin.do");
						HttpUtil.forward(request, response, "/WEB-INF/pages/fail.jsp");
					}else{
						if(BoardService.updateboard(board, rp, wp, mp,cp)) {
							request.setAttribute("ok_body", "게시판 생성 성공");
							request.setAttribute("forward_url", "admin.do");
							HttpUtil.forward(request, response, "/WEB-INF/pages/ok.jsp");
						}else {
							request.setAttribute("err_body", "db 에러");
							request.setAttribute("forward_url", "admin.do");
							HttpUtil.forward(request, response, "/WEB-INF/pages/fail.jsp");
						}
					}
				}else if(permission==5) { // OB운영자인 경우 
					if(rp>5 || wp>5|| mp!=5 || cp>5) { // 게시판권한이 유효한지 확인
						request.setAttribute("err_body", "할 수 없는 권한설정입니다.");
						request.setAttribute("forward_url", "admin.do");
						HttpUtil.forward(request, response, "/WEB-INF/pages/fail.jsp");
					}else{
						if(BoardService.updateboard(board, rp, wp, mp,cp)) {
							request.setAttribute("ok_body", "게시판 생성 성공");
							request.setAttribute("forward_url", "admin.do");
							HttpUtil.forward(request, response, "/WEB-INF/pages/ok.jsp");
						}else {
							request.setAttribute("err_body", "db 에러");
							request.setAttribute("forward_url", "admin.do");
							HttpUtil.forward(request, response, "/WEB-INF/pages/fail.jsp");
						}
					}
				}else if(permission==6) { // 관리자인 경우
					if(BoardService.updateboard(board, rp, wp, mp,cp)) {
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
		Integer permission = (int)session.getAttribute("permission");
		String id  = (String)session.getAttribute("id");
		String board_name = request.getParameter("board_name");
		BoardVO board = new BoardVO();
		board.setBoard_name(board_name);
		String csrf_token_server = (String)session.getAttribute("csrf_token");
		String csrf_token_client = request.getParameter("csrf_token");
		if(id!=null && csrf_token_server!=null && csrf_token_client!=null) { //로그인 여부 확인
			if(permission==BoardService.getPermissions_by_name(board_name).get("manage") ||permission==6) {// 권한 확인
				if(csrf_token_server.contentEquals(csrf_token_client)) { // csrf 토큰 체크
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
