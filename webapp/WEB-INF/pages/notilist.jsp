<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="java.util.*,vo.*"%>
<%@ include file="../semipage/header.jsp" %>
<%@ include file="../semipage/nav.jsp" %>
<%
	ArrayList<NotiVO> notilist = (ArrayList<NotiVO>)request.getAttribute("notilist");
%>
<div class="inmain">
<span class="badge badge-pill badge-primary">안읽은 공지 알림</span>
<span class="badge badge-pill badge-secondary">읽은 모든 알림</span>
<span class="badge badge-pill">안읽은 일반 알림</span>
<table class="table table-striped">
	<thead class="thead-light">
		<tr class="row">
			<th class="col-1">송신자</th>
			<th class="col-1">수신자</th>
			<th class="col-7">알림제목</th>
			<th class="col-2">작성일</th>
			<th class="col-1">읽기여부</th>
		</tr>
	</thead>
	<%
	for(int i=0;i<notilist.size();i++){ // 공지알림인지 댓글 알림인지 읽은 알림인지 명확히 구분 필요
		NotiVO noti = notilist.get(i);%>
		<%if(noti.isNotice() && !noti.isRead()){ %>
		<tr class="row bg-primary notice">
			<c:set var="sender" value="<%=noti.getSender() %>"/>
			<c:set var="receiver" value="<%=noti.getReceiver() %>"/>
			<c:set var="title" value="<%=noti.getTitle() %>"/>
			<td class="col-1"><a class="notice" href="getmember.do?member=<c:out value="${sender}"/>"><c:out value="${sender}"/></a></td>
			<td class="col-1"><a class="notice" href="getmember.do?member=<c:out value="${receiver}"/>"><c:out value="${receiver}"/></a></td>
			<td class="col-7"><a class="notice" href="notiview.do?nid=<%=noti.getNid()%>"><c:out value="${title}"/></a></td>
			<td class="col-2"><%=noti.getDate() %></td>
			<td class="col-1"><%=noti.isRead() %></td>
		</tr>
		<%}else if(noti.isRead()){ %>
		<tr class="row bg-secondary notice">
			<c:set var="sender" value="<%=noti.getSender() %>"/>
			<c:set var="receiver" value="<%=noti.getReceiver() %>"/>
			<c:set var="title" value="<%=noti.getTitle() %>"/>
			<td class="col-1"><a class="notice" href="getmember.do?member=<c:out value="${sender}"/>"><c:out value="${sender}"/></a></td>
			<td class="col-1"><a class="notice" href="getmember.do?member=<c:out value="${receiver}"/>"><c:out value="${receiver}"/></a></td>
			<td class="col-7"><a class="notice" href="notiview.do?nid=<%=noti.getNid()%>"><c:out value="${title}"/></a></td>
			<td class="col-2"><%=noti.getDate() %></td>
			<td class="col-1"><%=noti.isRead() %></td>
		</tr>
		<%}else if(!noti.isNotice() && !noti.isRead()){ %>
		<tr class="row ">
			<c:set var="sender" value="<%=noti.getSender() %>"/>
			<c:set var="receiver" value="<%=noti.getReceiver() %>"/>
			<c:set var="title" value="<%=noti.getTitle() %>"/>
			<td class="col-1"><a href="getmember.do?member=<c:out value="${sender}"/>"><c:out value="${sender}"/></a></td>
			<td class="col-1"><a href="getmember.do?member=<c:out value="${receiver}"/>"><c:out value="${receiver}"/></a></td>
			<td class="col-7"><a href="notiview.do?nid=<%=noti.getNid()%>"><c:out value="${title}"/></a></td>
			<td class="col-2"><%=noti.getDate() %></td>
			<td class="col-1"><%=noti.isRead() %></td>
		</tr>
		<%} %>
	<% }%>
</table>
</div>
<script>
setmain_from_inmain();
</script>