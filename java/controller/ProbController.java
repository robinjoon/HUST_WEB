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
		if(id != null && csrf_token_server!=null && csrf_token_client!=null) { // 로그인 여부 확인
			if(permission<1) {
				request.setAttribute("err_body", "신입회원부터 사용가능한 기능입니다. 현재 회원등급은 게스트 입니다. 운영진에게 문의하세요.");
				request.setAttribute("forward_url", "index.jsp");
				HttpUtil.forward(request, response, "/WEB-INF/pages/fail.jsp");
			}else {
				prob.setWriter(id); // 작성자 = 로그인된 사용자
				if(csrf_token_server.contentEquals(csrf_token_client)) { // csrf 체크
					if(ProbService.write(prob)) { // 문제작성성공
						response.sendRedirect("problist.do?category=all");
					}else{ //문제작성 실패
						HttpUtil.forward(request, response, "/WEB-INF/pages/fail.jsp");
					}
				}else {
					request.setAttribute("err_body", "csrf 토큰이 일치하지 않습니다. 알맞은 접근으로 시도하세요.");
					request.setAttribute("forward_url", "index.jsp");
					HttpUtil.forward(request, response, "/WEB-INF/pages/fail.jsp");
				}
			}
		}else {//로그인 안되었을때
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
		if(id==null||permission<=0) { //로그인 안되어있으면
			response.sendRedirect("index.jsp");
			return;
		}
		ArrayList<Problem> list = ProbService.getProbList(); // 전체 문제리스트
		ArrayList<Integer> solved_list = ProbService.getSolvedList(id); // 로그인된 사용자가 푼 문제리스트
		ArrayList<Member> rank = ProbService.getRanking(); // 현재 문제은행 랭킹
		ArrayList<String> category_list = new ArrayList<String>(); // 전체 카테고리 목록
		category_list.add("ALL");
		for(int i=0;i<list.size();i++) {
			Problem prob = list.get(i);
			if(category_list.indexOf(prob.getCategory())==-1) {
				category_list.add(prob.getCategory());
			}
		}
		HashMap<String,ArrayList<Problem>> probmap = new HashMap<String,ArrayList<Problem>>(); // 각 카테고리에 해당하는 문제리스트와 대응되는 해쉬맵
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
		if(id != null && csrf_token_server!=null && csrf_token_client!=null) { // 로그인 여부 확인
			prob.setWriter(id); // 작성자 = 로그인된 사용자
			if(csrf_token_server.contentEquals(csrf_token_client)) { // csrf 체크
				if(ProbService.modify(prob)) { // 수정성공
					response.sendRedirect("problist.do?category=all"); // 문제리스트로 이동
				}else {
					HttpUtil.forward(request, response, "index.jsp");
				}
			}else{
				request.setAttribute("err_body", "csrf 토큰이 일치하지 않습니다. 알맞은 접근으로 시도하세요.");
				request.setAttribute("forward_url", "index.jsp");
				HttpUtil.forward(request, response, "/WEB-INF/pages/fail.jsp");
			}
		}else {//로그인 안되었을때
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
		if(id != null && csrf_token_server!=null && csrf_token_client!=null) { //로그인 여부 확인
			if(csrf_token_server.contentEquals(csrf_token_client)) { // csrf 체크
				if(ProbService.delete(prob)) { // 삭제성공
					response.sendRedirect("problist.do?category=all"); // 문제리스트로 이동
				}else { // 삭제실패
					HttpUtil.forward(request, response, "/WEB-INF/pages/fail.jsp");
				}
			}else {
				request.setAttribute("err_body", "csrf 토큰이 일치하지 않습니다. 알맞은 접근으로 시도하세요.");
				request.setAttribute("forward_url", "index.jsp");
				HttpUtil.forward(request, response, "/WEB-INF/pages/fail.jsp");
			}
		}else { // 로그인 안되어있으면
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
			if(csrf_token_server.contentEquals(csrf_token_client)) { // csrf 체크
				if(ProbService.scoring(problem, id)) {
					request.setAttribute("ok_body", "정답입니다!");
					request.setAttribute("forward_url", "problist.do?category=all");
					HttpUtil.forward(request, response, "WEB-INF/pages/ok.jsp");
				}else {
					request.setAttribute("err_body", "틀렸습니다!");
					request.setAttribute("forward_url", "problist.do?category=all");
					HttpUtil.forward(request, response, "WEB-INF/pages/fail.jsp");
				}
			}else {
				request.setAttribute("err_body", "csrf 토큰이 일치하지 않습니다. 알맞은 접근으로 시도하세요.");
				request.setAttribute("forward_url", "index.jsp");
				HttpUtil.forward(request, response, "/WEB-INF/pages/fail.jsp");
			}
		}else {
			response.sendRedirect("index.jsp");
		}
	}

}
