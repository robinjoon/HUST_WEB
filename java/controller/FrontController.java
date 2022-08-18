// index.jsp를 제외한 모든 요청은 이 서블릿으로 들어오고, 여기서 알맞은 컨트롤러로 매핑 해 준다.
package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import auth.Auth;
import auth.AuthManager;
import tools.HttpUtil;

public class FrontController extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private String charset = null;
	private Map<String, Controller> uriControllerMap = null;
	private Map<String, String> uriMethodMap = null;
	private List<String> logOutRequired = null;

	@Override
	public void init(ServletConfig config) throws ServletException {
		charset = config.getInitParameter("charset");
		uriControllerMap = new HashMap<String, Controller>();
		uriMethodMap = new HashMap<String, String>();
		initLogOutRequired();
		initUriMaps();
	}

	private void initLogOutRequired() {
		logOutRequired = new ArrayList<String>();
		logOutRequired.add("/login.do");
		logOutRequired.add("/join.do");
	}

	private void initUriMaps() {
		// PostController
		uriControllerMap.put("/postsview.do", new PostController());
		uriMethodMap.put("/postsview.do", "getPostlist");
		uriControllerMap.put("/postview.do", new PostController());
		uriMethodMap.put("/postview.do", "getPost");
		uriControllerMap.put("/writepost.do", new PostController());
		uriMethodMap.put("/writepost.do", "write");
		uriControllerMap.put("/delete_post.do", new PostController());
		uriMethodMap.put("/delete_post.do", "delete_post");
		uriControllerMap.put("/edit_post.do", new PostController());
		uriMethodMap.put("/edit_post.do", "edit_post");
		uriControllerMap.put("/download.do", new PostController());
		uriMethodMap.put("/download.do", "filedownload");

		// MemberController
		uriControllerMap.put("/login.do", new MemberController());
		uriMethodMap.put("/login.do", "login");
		uriControllerMap.put("/join.do", new MemberController());
		uriMethodMap.put("/join.do", "join");
		
		uriControllerMap.put("/change_myinfo.do", new MemberControllerNeedCSRFCheck());
		uriMethodMap.put("/change_myinfo.do", "change_myinfo");
		uriControllerMap.put("/update_member_per.do", new MemberControllerNeedCSRFCheck());
		uriMethodMap.put("/update_member_per.do", "update_member_per");
		uriControllerMap.put("/resetpw.do", new MemberControllerNeedCSRFCheck());
		uriMethodMap.put("/resetpw.do", "reset_member_pw");
		
		uriControllerMap.put("/getmember.do", new MemberControllerNeedLogin());
		uriMethodMap.put("/getmember.do", "getMember");
		uriControllerMap.put("/memberlist.do", new MemberControllerNeedLogin());
		uriMethodMap.put("/memberlist.do", "memberlist");
		uriControllerMap.put("/mypage.do", new MemberControllerNeedLogin());
		uriMethodMap.put("/mypage.do", "myinfo");

		// MappingController
		uriControllerMap.put("/admin.do", new MappingController());
		uriMethodMap.put("/admin.do", "adminpage");

		// BoardController
		uriControllerMap.put("/create_board.do", new BoardController());
		uriMethodMap.put("/create_board.do", "create_board");
		uriControllerMap.put("/delete_board.do", new BoardController());
		uriMethodMap.put("/delete_board.do", "delete_board");
		uriControllerMap.put("/update_board.do", new BoardController());
		uriMethodMap.put("/update_board.do", "update_board");

		// ScheduleController
		uriControllerMap.put("/create_schedule.do", new ScheduleController());
		uriMethodMap.put("/create_schedule.do", "create_schedule");
		uriControllerMap.put("/delete_schedule.do", new ScheduleController());
		uriMethodMap.put("/delete_schedule.do", "delete_schedule");
		uriControllerMap.put("/schedules.do", new ScheduleController());
		uriMethodMap.put("/schedules.do", "getSchedules");

		// CommentController
		uriControllerMap.put("/write_comment.do", new CommentController());
		uriMethodMap.put("/write_comment.do", "write");
		uriControllerMap.put("/edit_comment.do", new CommentController());
		uriMethodMap.put("/edit_comment.do", "edit");
		uriControllerMap.put("/delete_comment.do", new CommentController());
		uriMethodMap.put("/delete_comment.do", "delete");

		// ProbController
		uriControllerMap.put("/problist.do", new ProbController());
		uriMethodMap.put("/problist.do", "getProbList");
		uriControllerMap.put("/scoring.do", new ProbController());
		uriMethodMap.put("/scoring.do", "scoring");
		uriControllerMap.put("/getprob.do", new ProbController());
		uriMethodMap.put("/getprob.do", "getProblem");
		uriControllerMap.put("/writeprob.do", new ProbController());
		uriMethodMap.put("/writeprob.do", "write");
		uriControllerMap.put("/modifyprob.do", new ProbController());
		uriMethodMap.put("/modifyprob.do", "modify");
		uriControllerMap.put("/deleteprob.do", new ProbController());
		uriMethodMap.put("/deleteprob.do", "delete");

		// NotiController
		uriControllerMap.put("/notilist.do", new NotiController());
		uriMethodMap.put("/notilist.do", "get_notilist");
		uriControllerMap.put("/notiview.do", new NotiController());
		uriMethodMap.put("/notiview.do", "get_noti");
		uriControllerMap.put("/readnoti.do", new NotiController());
		uriMethodMap.put("/readnoti.do", "read_noti");
		uriControllerMap.put("/deletenoti.do", new NotiController());
		uriMethodMap.put("/deletenoti.do", "delete_noti");

		// SearchController
		uriControllerMap.put("/search.do", new SearchController());
		uriMethodMap.put("/search.do", "search");
		uriControllerMap.put("/search_from_board.do", new SearchController());
		uriMethodMap.put("/search_from_board.do", "search_from_board");
		/* 컨드롤러 추가시마다 추가 */
	}

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding(charset);
		String url = request.getRequestURI(); // 요청으로부터 URI 추출
		String contextPath = request.getContextPath();
		String path = url.substring(contextPath.length());
		String action = uriMethodMap.get(path); // 각 컨트롤러가 담당하는 여러 역할 중 하나를 지정.
		Controller subController = uriControllerMap.get(path);// 추출한 URI를 키로 가지는 컨트롤러 지정
		Auth auth = new Auth(request);
		if(subController instanceof NeedAdmin  ) {
			if(!AuthManager.isAdmin(auth)) {
				request.setAttribute("err_body", "운영진만 사용할 수 있는 기능입니다.");
				request.setAttribute("forward_url", "index.jsp");
				HttpUtil.forward(request, response, "/WEB-INF/pages/fail.jsp");
				return;
			}
		}
		if(subController instanceof NeedLogin  ) {
			if(!AuthManager.loginCheck(auth)) {
				request.setAttribute("err_body", "로그인이 필요한 기능입니다.");
				request.setAttribute("forward_url", "login.jsp");
				request.getSession().invalidate();
				HttpUtil.forward(request, response, "/WEB-INF/pages/fail.jsp");
				return;
			}
		}
		if(subController instanceof NeedCSRFCheck  ) {
			if(!AuthManager.csrfCheck(auth)) {
				request.setAttribute("err_body", "CSRF 방지 토큰이 일치하지 않습니다.");
				request.setAttribute("forward_url", "index.jsp");
				HttpUtil.forward(request, response, "/WEB-INF/pages/fail.jsp");
				return;
			}
		}
		
		if(logOutRequired.contains(path)) {
			if(AuthManager.loginCheck(auth)) {
				response.sendRedirect("index.jsp");
			}
		}
		
		if (action == null) {
			HttpUtil.forward(request, response, "index.jsp");
		} else {
			subController.execute(request, response, action); // 컨트롤러 실행
		}
	}
}
