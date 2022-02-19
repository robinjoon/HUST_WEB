package controller;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import service.SearchService;
import tools.HttpUtil;
import vo.*;

public class SearchController implements Controller {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response, String action)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		if(action.contentEquals("search")) {
			search(request,response);
		}else if(action.contentEquals("search_from_board")){
			search_from_board(request,response);
		}else {
			response.sendRedirect("index.jsp");
		}
	}
	private void search(HttpServletRequest request,HttpServletResponse response) throws IOException {
		ArrayList<String> available_target_list = new ArrayList<String>();
		available_target_list.add("title");available_target_list.add("content");
		available_target_list.add("writer");available_target_list.add("origin_file_name");
		HttpSession session = request.getSession();
		String id = (String)session.getAttribute("id");
		Integer permission = (Integer)session.getAttribute("permission");
		if(id!=null&&permission!=null) {
			String search_word = request.getParameter("search_word");
			String search_target = request.getParameter("search_target");
			String sort = request.getParameter("sort");
			if(!available_target_list.contains(search_target)) {
				request.setAttribute("err_body", "사용할 수 없는 검색영역입니다.");
				HttpUtil.forward(request, response, "/WEB-INF/pages/fail.jsp");
			}else {
				ArrayList<Post> search_result = SearchService.searchPost(search_word, search_target, permission, sort);
				request.setAttribute("search_result", search_result);
				HttpUtil.forward(request, response, "/WEB-INF/pages/search_result.jsp");
			}
		}else {
			response.sendRedirect("index.jsp");
		}
	}
	private void search_from_board(HttpServletRequest request,HttpServletResponse response) throws IOException {
		ArrayList<String> available_target_list = new ArrayList<String>();
		available_target_list.add("title");available_target_list.add("content");
		available_target_list.add("writer");available_target_list.add("origin_file_name");
		HttpSession session = request.getSession();
		String id = (String)session.getAttribute("id");
		Integer permission = (Integer)session.getAttribute("permission");
		if(id!=null&&permission!=null) {
			String search_word = request.getParameter("search_word");
			String search_target = request.getParameter("search_target");
			String board_name = request.getParameter("board_name");
			String sort = request.getParameter("sort");
			if(!available_target_list.contains(search_target)) {
				request.setAttribute("err_body", "사용할 수 없는 검색영역입니다.");
				HttpUtil.forward(request, response, "/WEB-INF/pages/fail.jsp");
			}else {
				ArrayList<Post> search_result = SearchService.search_from_board(board_name, search_word, search_target, permission, sort);
				request.setAttribute("search_result", search_result);
				HttpUtil.forward(request, response, "/WEB-INF/pages/search_result.jsp");
			}
		}else {
			response.sendRedirect("index.jsp");
		}
	}

}
