<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="vo.*,java.util.*"%>
<%@ include file="../semipage/header.jsp" %>
<%@ include file="../semipage/nav.jsp" %>
<%
ArrayList<Post> search_result = (ArrayList<Post>)request.getAttribute("search_result");
	if(!is_login){
		response.sendRedirect("index.jsp");
	}
%>

<div class= "inmain d-none">
<br>
<div class="container-fluid">
<table class="table table-striped intable">
	<thead class="thead-light">
		<tr>
			<th>게시판</th>
			<th>글번호</th>
			<th>글제목</th>
			<th>작성자</th>
			<th>작성일</th>
			<th>조회수</th>
		</tr>
	</thead>
	<%
	for(int i=0;i<search_result.size();i++){ 
			Post post = search_result.get(i);
	%>
		<c:set var="board_name" value="<%=post.getBoard_name()%>"/>
		<tr id="row<%=i%>">
			<c:set var="title" value="<%=post.getTitle() %>"/>
			<c:set var="writer" value="<%=post.getWriter() %>"/>
			<td><c:out value="${board_name}"/></td>
			<td><%=post.getPid() %></td>
			<td><a href="postview.do?pid=<%=post.getPid()%>&board_name=<c:out value="${board_name}"/>"><c:out value="${title}"/></a></td>
			<td><a href="getmember.do?member=<c:out value="${writer}"/>"><c:out value="${writer}"/></a></td>
			<td><%=post.getWrite_date().toString().substring(0, 10) %></td>
			<td><%=post.getViews() %></td>
		</tr>
	<% }%>
</table>
<%if(search_result!=null && !search_result.isEmpty()){ %>
	<ul class="pagination justify-content-center">
	<li class="page-item"><button class="page-link" onclick="firstpages();">&laquo;</button></li>
	<li class="page-item"><button class="page-link" onclick="prevpages();">&lt;</button></li>
	<%for(int i=1;i<=search_result.size()/10;i++){ %>
		<li class="page-item" id="page<%=i%>">
			<button class="page-link" onclick="pagenation(<%=i%>);"><%=i%></button>
		</li>
	<%} %>
	<li class="page-item"><button class="page-link" onclick="nextpages();">&gt;</button></li>
	<li class="page-item"><button class="page-link" onclick="lastpages();">&raquo;</button></li>
	</ul>
<%}else{ %>
<p class="text-center text-danger">검색결과가 없습니다.</p>
<%} %>
</div>
<br>
</div>
<script>
jQuery(document).ready(function(){
	setmain_from_inmain();
});
var min = 1;
var max = <%=search_result.size()/10%>>10?10:<%=search_result.size()/10%>;
var now = 1;

function pagenation(page){
	$("#page"+now).removeClass("active");
	now = page;
	$("#page"+now).addClass("active");
	for(var i=0;i<<%=search_result.size()%>;i++){
		var row = $("#row"+i);
		if(i>=(now-1)*10 && i<=now*10 -1){
			row.show();
		}else{
			row.hide();
		}
	}
}
function pages(){
	for(var i=1;i<=<%=search_result.size()/10%>;i++){
		var page = $("#page"+i);
		if(i>=min && i<=max){
			page.show();
		}else{
			page.hide();
		}
	}
}
function nextpages(){
	if(max>=<%=search_result.size()/10%>){
		return;
	}
	min+=10;
	max+=10;
	pages();
	pagenation(min);
}
function prevpages(){
	if(min<=1){
		return;
	}
	min-=10;
	max-=10;
	pages();
	pagenation(max);
}
function firstpages(){
	pagenation(1);
	min = 1; 
	max = <%=search_result.size()/10%>>10?10:<%=search_result.size()/10%>;
	pages();
}
function lastpages(){
	max = <%=search_result.size()/10%>;
	min = max - max%10;
	if(max<=10){
		return;
	}
	pagenation(max);
	pages();
}
pagenation(1);
pages();
</script>
</body>
</html>