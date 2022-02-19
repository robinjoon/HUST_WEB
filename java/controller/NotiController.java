package controller;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import service.NotiService;
import tools.HttpUtil;
import vo.*;
public class NotiController implements Controller {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response, String action)
			throws ServletException, IOException {
		if(action.contentEquals("send_noti")) {
			/*알림을 송신하는 것은 각 알림이 발생하는 상황을 담당하는 컨트롤러에 구현되어있음.*/
		}else if(action.contentEquals("get_noti")) {
			read(request,response);
		}else if(action.contentEquals("get_notilist")) {
			read_many(request,response);
		}else if(action.contentEquals("read_noti")) {
			update(request,response); // noti 의 read 컬럼을 업데이트 하는것이므로 update 메소드.
		}else if(action.contentEquals("delete_noti")) {
			delete(request,response);
		}

	}
	
	private void read(HttpServletRequest request,HttpServletResponse response) throws IOException {
		HttpSession session = (HttpSession)request.getSession();
		String id = (String)session.getAttribute("id");
		long nid = Long.parseLong(request.getParameter("nid"));
		if(id!=null) {
			Noti noti = NotiService.getNoti(nid);
			request.setAttribute("noti", noti);
			HttpUtil.forward(request, response, "/WEB-INF/pages/noti.jsp");
		}else {
			 response.sendRedirect("index.jsp");
		 }
	}
	private void read_many(HttpServletRequest request,HttpServletResponse response) throws IOException {
		HttpSession session = (HttpSession)request.getSession();
		String id = (String)session.getAttribute("id");
		if(id!=null) {
			 ArrayList<Noti> notilist = NotiService.getNotiList(id);
			 request.setAttribute("notilist", notilist);
			 HttpUtil.forward(request, response, "/WEB-INF/pages/notilist.jsp");
		 }else {
			 response.sendRedirect("index.jsp");
		 }
	}
	private void update(HttpServletRequest request,HttpServletResponse response) throws IOException {
		HttpSession session = (HttpSession)request.getSession();
		String id = (String)session.getAttribute("id");
		long nid = Long.parseLong(request.getParameter("nid"));
		Noti noti = NotiService.getNoti(nid);
		if(id!=null && id.contentEquals(noti.getReceiver())) {
			if(NotiService.read_noti(noti)) {
				response.sendRedirect("notilist.do");
			}else {
				request.setAttribute("err_body", "알림읽기 실패");
				request.setAttribute("forward_url", "notilist.do");
				HttpUtil.forward(request, response, "/WEB-INF/pages/fail.jsp");
			}
		}else {
			response.sendRedirect("index.jsp");
		}
	}
	private void delete(HttpServletRequest request,HttpServletResponse response) throws IOException {
		HttpSession session = (HttpSession)request.getSession();
		String id = (String)session.getAttribute("id");
		Long nid = Long.parseLong(request.getParameter("nid"));
		Noti noti = NotiService.getNoti(nid);
		if(id!=null && id.contentEquals(noti.getReceiver())) {
			if(NotiService.delete_noti(noti)) {
				request.setAttribute("ok_body", "알림삭제 성공");
				request.setAttribute("forward_url", "notilist.do");
				HttpUtil.forward(request, response, "/WEB-INF/pages/ok.jsp");
			}else {
				request.setAttribute("err_body", "알림삭제 실패");
				request.setAttribute("forward_url", "notilist.do");
				HttpUtil.forward(request, response, "/WEB-INF/pages/fail.jsp");
			}
		}else {
			response.sendRedirect("index.jsp");
		}
	}

}
