<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="vo.*,org.jsoup.*,org.jsoup.nodes.*, org.jsoup.safety.*"%>
<%@ include file="../semipage/header.jsp" %>
<%@ include file="../semipage/nav.jsp" %>
<%
Noti noti = (Noti)request.getAttribute("noti");
	Whitelist mywhitelist = Whitelist.relaxed();
	mywhitelist.addProtocols("img", "src", "http", "https", "data")
	.addTags("font","iframe","i")
	.addAttributes("p", "style")
	.addAttributes("font", "style","color")
	.addAttributes("span", "style")
	.addAttributes("iframe", "class","src","frameborder","width","height")
	.addAttributes("h1", "style")
	.addAttributes("h2", "style")
	.addAttributes("h3", "style")
	.addAttributes("h4", "style")
	.addAttributes("h5", "style")
	.addAttributes("h6", "style")
	.addAttributes("li", "style")
	.addAttributes("pre","style")
	.addAttributes("b","style")
	.addAttributes("strike","style")
	.addAttributes("i","style")
	.addAttributes("u","style");
%>
<c:set var="title" value="<%=noti.getTitle() %>"/>

<c:set var="sender" value="<%=noti.getSender() %>"/>
<c:set var="receiver" value="<%=noti.getReceiver() %>"/>
<c:set var="url" value="<%=noti.getUrl() %>"/>
<div class="inmain">
<br>
	<ul class="list-group">
		<li class="list-group-item"><h3><c:out value="${title}"/></h3></li>
		<li class="list-group-item">알림보낸 사람 : <c:out value="${sender}"/></li>
		<li class="list-group-item">알림받은 사람 : <c:out value="${receiver}"/></li>
		<li class="list-group-item"><%=Jsoup.clean(noti.getBody(), mywhitelist)%></li>
		<li class="list-group-item"><a href="<c:out value="${url}"/>"><c:out value="${url}"/></a></li>
		<li class="list-group-item">
			<a class="btn btn-primary" href="notilist.do">알림리스트로</a>
			<%if(!noti.isRead()){ %>
			<a class="btn btn-success" href="readnoti.do?nid=<%=noti.getNid()%>">알림 확인</a>
			<%}%>
			<a class="btn btn-danger" href="deletenoti.do?nid=<%=noti.getNid()%>">알림 삭제</a>
			
		</li>
	</ul>
<br>
</div>
<script>
setmain_from_inmain();
</script>