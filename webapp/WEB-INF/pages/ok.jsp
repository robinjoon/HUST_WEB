<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
    	String body = (String)request.getAttribute("ok_body");
		if(body==null || body.isBlank() || body.isEmpty()){
			body = "성공";
		}
		String url = (String)request.getAttribute("forward_url");
		if(url==null || url.isBlank() || url.isEmpty()){
			url = "index.jsp";
		}
%>
<script>alert("<%=body%>");
window.parent.location.href = "<%=url%>";
</script>