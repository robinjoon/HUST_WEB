package controller;

import java.io.IOException;
import java.sql.Date;
import java.util.Calendar;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import auth.Auth;
import auth.AuthManager;
import service.ScheduleService;
import tools.HttpUtil;
import vo.Schedule;

public class ScheduleController implements Controller {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response, String action)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
			Auth auth = new Auth(request);
			if(AuthManager.loginCheck(auth)) {
				if(action.contentEquals("create_schedule")) { // 스케줄 생성
					if(AuthManager.csrfCheck(auth)) {
						create(request,response,auth);
					}else {
						request.setAttribute("err_body", "csrf 토큰이 일치하지 않습니다. 알맞은 접근으로 시도하세요.");
						request.setAttribute("forward_url", "index.jsp");
						HttpUtil.forward(request, response, "/WEB-INF/pages/fail.jsp");
					}
				}else if(action.contentEquals("delete_schedule")) {
					if(AuthManager.csrfCheck(auth)) {
						delete(request,response,auth);
					}else {
						request.setAttribute("err_body", "csrf 토큰이 일치하지 않습니다. 알맞은 접근으로 시도하세요.");
						request.setAttribute("forward_url", "index.jsp");
						HttpUtil.forward(request, response, "/WEB-INF/pages/fail.jsp");
					}
				}else if(action.contentEquals("getSchedules")) { // 스케줄 리스트 불러오기
					read_many(request,response);
				}else {
					response.sendRedirect("index.jsp");
				}
			}else {
				response.sendRedirect("index.jsp");
			}
	}

	private void create(HttpServletRequest request, HttpServletResponse response, Auth auth)
			throws ServletException, IOException{
		Date s_date = Date.valueOf(request.getParameter("s_date"));
		String title = request.getParameter("title");
		String place = request.getParameter("place");
		String content = request.getParameter("content");
		boolean is_ob = Boolean.parseBoolean(request.getParameter("is_ob"));
		Schedule schedule = new Schedule();
		schedule.setContent(content);
		schedule.set_ob(is_ob);
		schedule.setPlace(place);
		schedule.setS_date(s_date);
		schedule.setTitle(title);
		if((AuthManager.isYBAdmin(auth) && is_ob) || (AuthManager.isOBAdmin(auth) && !is_ob)) {
			request.setAttribute("err_body", "YB 운영자는 YB 일정만, OB 운영자는 OB 일정만 등록가능합니다.");
			request.setAttribute("forward_url", "schedules.do?what=yb");
			HttpUtil.forward(request, response, "/WEB-INF/pages/fail.jsp");
			return;
		}
		if(AuthManager.isAdmin(auth)) { // 관리자권한 확인
			if(ScheduleService.create_schedule(schedule)) { // 생성 성공
				response.sendRedirect("schedules.do?what=yb");
			}else { // 생성 실패
				HttpUtil.forward(request, response, "/WEB-INF/pages/fail.jsp");
			}
		}else {
			response.sendRedirect("index.jsp");
		}
	}

	private void read_many(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException{
		String what = request.getParameter("what"); // all || yb || ob
		String date = request.getParameter("date"); // YYYY-mm 형태
		
		int year;
		int month;
		if(date==null || date.isEmpty()) { // 날짜 데이터 안넘어왔을 때
			Calendar cl = Calendar.getInstance();
			year = cl.get(Calendar.YEAR);
			month = cl.get(Calendar.MONTH)+1;
		}else { // 날짜 데이터 넘어왔을 때
			String[] arr= date.split("-");
			try {
				year = Integer.parseInt(arr[0]);
				month = Integer.parseInt(arr[1]);
			}catch(NumberFormatException e) { // 날짜 데이터가 이상할 때
				Calendar cl = Calendar.getInstance();
				year = cl.get(Calendar.YEAR);
				month = cl.get(Calendar.MONTH)+1;
			}
		}
		ArrayList<Schedule> list = null;
		
		if(what.contentEquals("all")) { // 전체 스케줄
			//if(permission>=3) { // ob 이상인지 확인
				list = ScheduleService.getScheduleList(what, year, month);
				request.setAttribute("schedules", list);
				HttpUtil.forward(request, response, "/WEB-INF/pages/schedules.jsp");
			/*}else {
				request.setAttribute("err_body", "권한이 없습니다.");
				request.setAttribute("forward_url", "schedules.do?what=yb");
				HttpUtil.forward(request, response, "/WEB-INF/pages/fail.jsp");
			}*/
		}else if(what.contentEquals("ob")) { //ob 스케줄
			//if(permission>=3) { //3= ob, 5 = ob관리자
				list = ScheduleService.getScheduleList(what, year, month);
				request.setAttribute("schedules", list);
				HttpUtil.forward(request, response, "/WEB-INF/pages/schedules.jsp");
			/*}else{
				request.setAttribute("err_body", "권한이 없습니다.");
				request.setAttribute("forward_url", "schedules.do?what=yb");
				HttpUtil.forward(request, response, "/WEB-INF/pages/fail.jsp");
			}*/
		}else if(what.contentEquals("yb")) { // yb 스케줄
			//if(permission>=1) { //1 =신입  2 = 재학생 4 = yb 관리자
				list = ScheduleService.getScheduleList(what, year, month); 
				request.setAttribute("schedules", list);
				HttpUtil.forward(request, response, "/WEB-INF/pages/schedules.jsp");
			/*}else {
				request.setAttribute("err_body", "권한이 없습니다.");
				HttpUtil.forward(request, response, "/WEB-INF/pages/fail.jsp");
			}*/
		}else {
			response.sendRedirect("index.jsp");
		}

	}

	private void delete(HttpServletRequest request, HttpServletResponse response,Auth auth)
			throws ServletException, IOException{
		int sid = Integer.parseInt(request.getParameter("sid"));
		Schedule schedule = new Schedule();
		schedule.setSid(sid);
		if(ScheduleService.is_ob(schedule)) { // ob 스케줄인 경우
			if(AuthManager.isOBAdmin(auth) || AuthManager.isGenrealAdmin(auth)) { // OB 운영자 혹은관리자
				if(ScheduleService.delete_schedule(schedule)) { // 성공
					request.setAttribute("ok_body", "삭제 성공");
					request.setAttribute("forward_url", "schedules.do?what=yb");
					HttpUtil.forward(request, response, "/WEB-INF/pages/ok.jsp");
				}else { // 실패
					request.setAttribute("err_body", "DB 에러");
					request.setAttribute("forward_url", "schedules.do?what=yb");
					HttpUtil.forward(request, response, "/WEB-INF/pages/fail.jsp");
				}
			}else { // 권한 없음
				request.setAttribute("err_body", "권한이 없습니다.");
				request.setAttribute("forward_url", "schedules.do?what=yb");
				HttpUtil.forward(request, response, "/WEB-INF/pages/fail.jsp");	
			}
		}else { // YB 일정인 경우
			if(AuthManager.isYBAdmin(auth) || AuthManager.isGenrealAdmin(auth)) { // YB 운영자 혹은 관리자인 경우
				if(ScheduleService.delete_schedule(schedule)) { // 성공
					request.setAttribute("ok_body", "삭제 성공");
					request.setAttribute("forward_url", "schedules.do?what=yb");
					HttpUtil.forward(request, response, "/WEB-INF/pages/ok.jsp");
				}else { // 실패
					request.setAttribute("err_body", "DB 에러");
					request.setAttribute("forward_url", "schedules.do?what=yb");
					HttpUtil.forward(request, response, "/WEB-INF/pages/fail.jsp");
				}
			}else { // 권한없음
				request.setAttribute("err_body", "권한이 없습니다.");
				request.setAttribute("forward_url", "schedules.do?what=yb");
				HttpUtil.forward(request, response, "/WEB-INF/pages/fail.jsp");	
			}
		}
	}
}
