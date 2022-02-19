<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="tools.*"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<jsp:include page="/WEB-INF/semipage/header.jsp" />
<%
String csrf_token_server = (String)session.getAttribute("csrf_token");
String csrf_token_client = request.getParameter("csrf_token");
if(session.getAttribute("id")!=null&& csrf_token_server!=null && csrf_token_client!=null){
	if(csrf_token_server.contentEquals(csrf_token_client)){
		Sessions.removeSession(session);
		session.invalidate();
		HttpUtil.forward(request, response, "/WEB-INF/pages/ok.jsp");
	}else{
		request.setAttribute("err_body", "csrf 토큰이 일치하지 않습니다. 알맞은 접근으로 시도하세요.");
		request.setAttribute("forward_url", "index.jsp");
		HttpUtil.forward(request, response, "/WEB-INF/pages/fail.jsp");
	}
}else{
	HttpUtil.forward(request, response, "/WEB-INF/pages/fail.jsp");
}
%>