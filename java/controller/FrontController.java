// index.jsp를 제외한 모든 요청은 이 서블릿으로 들어오고, 여기서 알맞은 컨트롤러로 매핑 해 준다.
package controller;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import tools.HttpUtil;


public class FrontController extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private String charset =null;
	private HashMap<String,Controller> controllerlist = null;
	private HashMap<String,String> actionlist = null;
	@Override
	public void init(ServletConfig config) throws ServletException {
		charset = config.getInitParameter("charset");
		controllerlist = new HashMap<String,Controller>();
		actionlist = new HashMap<String,String>();
		//PostController
		controllerlist.put("/postsview.do", new PostController());
		actionlist.put("/postsview.do","getPostlist");
		controllerlist.put("/postview.do",new PostController());
		actionlist.put("/postview.do","getPost");
		controllerlist.put("/writepost.do",new PostController());
		actionlist.put("/writepost.do", "write");
		controllerlist.put("/delete_post.do",new PostController());
		actionlist.put("/delete_post.do", "delete_post");
		controllerlist.put("/edit_post.do",new PostController());
		actionlist.put("/edit_post.do", "edit_post");
		controllerlist.put("/download.do",new PostController());
		actionlist.put("/download.do", "filedownload");
		
		//MemberController
		controllerlist.put("/login.do",new MemberController());
		actionlist.put("/login.do","login");
		controllerlist.put("/join.do",new MemberController());
		actionlist.put("/join.do","join");
		controllerlist.put("/mypage.do",new MemberController());
		actionlist.put("/mypage.do", "myinfo");
		controllerlist.put("/memberlist.do",new MemberController());
		actionlist.put("/memberlist.do", "memberlist");
		controllerlist.put("/change_myinfo.do", new MemberController());
		actionlist.put("/change_myinfo.do", "change_myinfo");
		controllerlist.put("/update_member_per.do", new MemberController());
		actionlist.put("/update_member_per.do", "update_member_per");
		controllerlist.put("/getmember.do", new MemberController());
		actionlist.put("/getmember.do", "getMember");
		controllerlist.put("/resetpw.do", new MemberController());
		actionlist.put("/resetpw.do", "reset_member_pw");

		
		//MappingController
		controllerlist.put("/admin.do",new MappingController());
		actionlist.put("/admin.do", "adminpage");
		
		//BoardController
		controllerlist.put("/create_board.do",new BoardController());
		actionlist.put("/create_board.do", "create_board");
		controllerlist.put("/delete_board.do",new BoardController());
		actionlist.put("/delete_board.do", "delete_board");
		controllerlist.put("/update_board.do",new BoardController());
		actionlist.put("/update_board.do", "update_board");
		
		//ScheduleController
		controllerlist.put("/create_schedule.do",new ScheduleController());
		actionlist.put("/create_schedule.do", "create_schedule");
		controllerlist.put("/delete_schedule.do",new ScheduleController());
		actionlist.put("/delete_schedule.do", "delete_schedule");
		controllerlist.put("/schedules.do",new ScheduleController());
		actionlist.put("/schedules.do", "getSchedules");
		

		//CommentController
		controllerlist.put("/write_comment.do", new CommentController());
		actionlist.put("/write_comment.do", "write");
		controllerlist.put("/edit_comment.do", new CommentController());
		actionlist.put("/edit_comment.do", "edit");
		controllerlist.put("/delete_comment.do", new CommentController());
		actionlist.put("/delete_comment.do", "delete");
		
		//ProbController
		controllerlist.put("/problist.do",new ProbController());
		actionlist.put("/problist.do", "getProbList");
		controllerlist.put("/scoring.do", new ProbController());
		actionlist.put("/scoring.do", "scoring");
		controllerlist.put("/getprob.do", new ProbController());
		actionlist.put("/getprob.do", "getProblem");
		controllerlist.put("/writeprob.do", new ProbController());
		actionlist.put("/writeprob.do", "write");
		controllerlist.put("/modifyprob.do", new ProbController());
		actionlist.put("/modifyprob.do", "modify");
		controllerlist.put("/deleteprob.do", new ProbController());
		actionlist.put("/deleteprob.do", "delete");
		
		//NotiController
		controllerlist.put("/notilist.do", new NotiController());
		actionlist.put("/notilist.do","get_notilist");
		controllerlist.put("/notiview.do", new NotiController());
		actionlist.put("/notiview.do","get_noti");
		controllerlist.put("/readnoti.do", new NotiController());
		actionlist.put("/readnoti.do","read_noti");
		controllerlist.put("/deletenoti.do", new NotiController());
		actionlist.put("/deletenoti.do","delete_noti");

		//SearchController
		controllerlist.put("/search.do", new SearchController());
		actionlist.put("/search.do","search");
		controllerlist.put("/search_from_board.do", new SearchController());
		actionlist.put("/search_from_board.do","search_from_board");
		/*컨드롤러 추가시마다 추가*/
	}


	@Override
	protected void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		req.setCharacterEncoding(charset);
		String url = req.getRequestURI(); //요청으로부터 URI 추출
		String contextPath = req.getContextPath();
		String path = url.substring(contextPath.length());
		String action = actionlist.get(path); //각 컨트롤러가 담당하는 여러 역할 중 하나를 지정.
		Controller subController = controllerlist.get(path);// 추출한 URI를 키로 가지는 컨트롤러 지정
		if(controllerlist==null || action==null) {
			HttpUtil.forward(req, res, "index.jsp");
		}else {
			subController.execute(req,res,action); // 컨트롤러 실행
		}
	}

	
}
