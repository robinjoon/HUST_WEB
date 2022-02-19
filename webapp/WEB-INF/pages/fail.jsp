<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
    	String body = (String)request.getAttribute("err_body");
		if(body==null || body.isBlank() || body.isEmpty()){
			body = "알수 없는 이유로 인해 실패하였습니다.";
		}
		String url = (String)request.getAttribute("forward_url");
		if(url==null || url.isBlank() || url.isEmpty()){
			url = "index.jsp";
		}
%>
<script>alert("<%=body%>");
window.parent.location.href = "<%=url%>";
</script>