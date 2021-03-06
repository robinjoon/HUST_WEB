package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import tools.HttpUtil;
import vo.Member;
import vo.Problem;
import service.ProbService;
public class ProbController implements Controller {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response, String action)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
			if(action.contentEquals("write")) {
				create(request,response);
			}else if(action.contentEquals("delete")) {
				delete(request,response);
			}else if(action.contentEquals("modify")) {
				update(request,response);
			}else if(action.contentEquals("getProbList")) {
				read_many(request,response);
			}else if(action.contentEquals("scoring")) {
				scoring(request,response);
			}else if(action.contentEquals("getProblem")) {
				read(request,response);
			}
	}
	private void create(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException{
		String title = request.getParameter("title");
		String content = request.getParameter("content");
		String category = request.getParameter("category");
		int point = Integer.parseInt(request.getParameter("point"));
		String answer = request.getParameter("answer");
		if(title.isBlank()||content.isBlank()||category.isBlank()||answer.isBlank()) {
			response.sendRedirect("problist.do?category=all");
		}
		Problem prob = new Problem();
		prob.setTitle(title);prob.setContent(content);
		prob.setPoint(point);prob.setAnswer(answer);
		prob.setCategory(category);
		
		HttpSession session = request.getSession();
		String id = (String)session.getAttribute("id");
		int permission = (Integer)session.getAttribute("permission");
		String csrf_token_server = (String)session.getAttribute("csrf_token");
		String csrf_token_client = request.getParameter("csrf_token");
		if(id != null && csrf_token_server!=null && csrf_token_client!=null) { // ????????? ?????? ??????
			if(permission<1) {
				request.setAttribute("err_body", "?????????????????? ??????????????? ???????????????. ?????? ??????????????? ????????? ?????????. ??????????????? ???????????????.");
				request.setAttribute("forward_url", "index.jsp");
				HttpUtil.forward(request, response, "/WEB-INF/pages/fail.jsp");
			}else {
				prob.setWriter(id); // ????????? = ???????????? ?????????
				if(csrf_token_server.contentEquals(csrf_token_client)) { // csrf ??????
					if(ProbService.write(prob)) { // ??????????????????
						response.sendRedirect("problist.do?category=all");
					}else{ //???????????? ??????
						HttpUtil.forward(request, response, "/WEB-INF/pages/fail.jsp");
					}
				}else {
					request.setAttribute("err_body", "csrf ????????? ???????????? ????????????. ????????? ???????????? ???????????????.");
					request.setAttribute("forward_url", "index.jsp");
					HttpUtil.forward(request, response, "/WEB-INF/pages/fail.jsp");
				}
			}
		}else {//????????? ???????????????
			HttpUtil.forward(request, response, "index.jsp");
		}
	}
	private void read(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException{
		int probid = Integer.parseInt(request.getParameter("probid"));
		HttpSession session = request.getSession();
		String id = (String)session.getAttribute("id");
		Integer permission =(Integer)session.getAttribute("permission");
		Problem problem = new Problem();
		problem.setProbid(probid);
		if(id!=null && permission>0) {
			problem = ProbService.getProb(probid);
			ArrayList<Integer> solved_list = ProbService.getSolvedList(id);
			ArrayList<Member> rank = ProbService.getRanking();
			request.setAttribute("solved_list", solved_list);
			request.setAttribute("problem", problem);
			request.setAttribute("rank", rank);
			HttpUtil.forward(request, response, "/WEB-INF/pages/probview.jsp");
		}else {
			response.sendRedirect("index.jsp");
		}
		
	}
	private void read_many(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException{
		HttpSession session = request.getSession();
		String id = (String)session.getAttribute("id");
		Integer permission =(Integer)session.getAttribute("permission");
		if(id==null||permission<=0) { //????????? ??????????????????
			response.sendRedirect("index.jsp");
			return;
		}
		ArrayList<Problem> list = ProbService.getProbList(); // ?????? ???????????????
		ArrayList<Integer> solved_list = ProbService.getSolvedList(id); // ???????????? ???????????? ??? ???????????????
		ArrayList<Member> rank = ProbService.getRanking(); // ?????? ???????????? ??????
		ArrayList<String> category_list = new ArrayList<String>(); // ?????? ???????????? ??????
		category_list.add("ALL");
		for(int i=0;i<list.size();i++) {
			Problem prob = list.get(i);
			if(category_list.indexOf(prob.getCategory())==-1) {
				category_list.add(prob.getCategory());
			}
		}
		HashMap<String,ArrayList<Problem>> probmap = new HashMap<String,ArrayList<Problem>>(); // ??? ??????????????? ???????????? ?????????????????? ???????????? ?????????
		for(int i=0;i<category_list.size();i++) {
			ArrayList<Problem> problist = new ArrayList<Problem>();
			for(int j=0;j<list.size();j++) {
				Problem prob = list.get(j);
				if(category_list.get(i).contentEquals(prob.getCategory())) {
					problist.add(prob);
				}
			}
			probmap.put(category_list.get(i), problist);
		}
		request.setAttribute("problems", list);
		request.setAttribute("solved_list", solved_list);
		request.setAttribute("category_list",category_list);
		request.setAttribute("probmap", probmap);
		request.setAttribute("rank", rank);
		HttpUtil.forward(request, response, "/WEB-INF/pages/problist.jsp");
		
	}
	private void update(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException{
		String title = request.getParameter("title");
		String content = request.getParameter("content");
		String category = request.getParameter("category");
		int point = Integer.parseInt(request.getParameter("point"));
		String answer = request.getParameter("answer");
		int probid = Integer.parseInt(request.getParameter("probid"));
		Problem prob = new Problem();
		prob.setTitle(title);prob.setContent(content);
		prob.setPoint(point);prob.setAnswer(answer);
		prob.setProbid(probid);
		prob.setCategory(category);

		HttpSession session = request.getSession();
		String id = (String)session.getAttribute("id");
		String csrf_token_server = (String)session.getAttribute("csrf_token");
		String csrf_token_client = request.getParameter("csrf_token");
		if(id != null && csrf_token_server!=null && csrf_token_client!=null) { // ????????? ?????? ??????
			prob.setWriter(id); // ????????? = ???????????? ?????????
			if(csrf_token_server.contentEquals(csrf_token_client)) { // csrf ??????
				if(ProbService.modify(prob)) { // ????????????
					response.sendRedirect("problist.do?category=all"); // ?????????????????? ??????
				}else {
					HttpUtil.forward(request, response, "index.jsp");
				}
			}else{
				request.setAttribute("err_body", "csrf ????????? ???????????? ????????????. ????????? ???????????? ???????????????.");
				request.setAttribute("forward_url", "index.jsp");
				HttpUtil.forward(request, response, "/WEB-INF/pages/fail.jsp");
			}
		}else {//????????? ???????????????
			HttpUtil.forward(request, response, "index.jsp");
		}
	}
	private void delete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException{
		int probid = Integer.parseInt(request.getParameter("probid"));
		Problem prob = new Problem();
		prob.setProbid(probid);
		HttpSession session = request.getSession();
		String id = (String)session.getAttribute("id");
		prob.setWriter(id);
		String csrf_token_server = (String)session.getAttribute("csrf_token");
		String csrf_token_client = request.getParameter("csrf_token");
		if(id != null && csrf_token_server!=null && csrf_token_client!=null) { //????????? ?????? ??????
			if(csrf_token_server.contentEquals(csrf_token_client)) { // csrf ??????
				if(ProbService.delete(prob)) { // ????????????
					response.sendRedirect("problist.do?category=all"); // ?????????????????? ??????
				}else { // ????????????
					HttpUtil.forward(request, response, "/WEB-INF/pages/fail.jsp");
				}
			}else {
				request.setAttribute("err_body", "csrf ????????? ???????????? ????????????. ????????? ???????????? ???????????????.");
				request.setAttribute("forward_url", "index.jsp");
				HttpUtil.forward(request, response, "/WEB-INF/pages/fail.jsp");
			}
		}else { // ????????? ??????????????????
			HttpUtil.forward(request, response, "index.jsp");
		}
	}
	private void scoring(HttpServletRequest request,HttpServletResponse response) throws IOException {
		String answer = request.getParameter("answer");
		int probid = Integer.parseInt(request.getParameter("probid"));
		HttpSession session = request.getSession();
		String id = (String)session.getAttribute("id");
		Integer permission =(Integer)session.getAttribute("permission");
		Problem problem = new Problem();
		problem.setAnswer(answer); problem.setProbid(probid);
		String csrf_token_server = (String)session.getAttribute("csrf_token");
		String csrf_token_client = request.getParameter("csrf_token");
		if(id != null && csrf_token_server!=null && csrf_token_client!=null && permission>=1) {
			if(csrf_token_server.contentEquals(csrf_token_client)) { // csrf ??????
				if(ProbService.scoring(problem, id)) {
					request.setAttribute("ok_body", "???????????????!");
					request.setAttribute("forward_url", "problist.do?category=all");
					HttpUtil.forward(request, response, "WEB-INF/pages/ok.jsp");
				}else {
					request.setAttribute("err_body", "???????????????!");
					request.setAttribute("forward_url", "problist.do?category=all");
					HttpUtil.forward(request, response, "WEB-INF/pages/fail.jsp");
				}
			}else {
				request.setAttribute("err_body", "csrf ????????? ???????????? ????????????. ????????? ???????????? ???????????????.");
				request.setAttribute("forward_url", "index.jsp");
				HttpUtil.forward(request, response, "/WEB-INF/pages/fail.jsp");
			}
		}else {
			response.sendRedirect("index.jsp");
		}
	}

}
