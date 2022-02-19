<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="vo.*,java.util.*"%>
<%@ include file="../semipage/header.jsp" %>
<%@ include file="../semipage/nav.jsp" %>
<%
	ArrayList<PostVO> list = (ArrayList<PostVO>)request.getAttribute("postlist");
	String board_name = (String)request.getAttribute("board_name");
	int total = (int)request.getAttribute("total");
	int now = (int)request.getAttribute("now");
	int start = (int)request.getAttribute("start");
	int end = (int)request.getAttribute("end");
	String manage = (String)request.getAttribute("manage");
	String read = (String)request.getAttribute("read");
	String write = (String)request.getAttribute("write");
	String comment = (String)request.getAttribute("comment");
	if(!is_login){
		response.sendRedirect("index.jsp");
	}
	
%>
<div class= "inmain d-none">
<br>
<div class="container-fluid">
<c:set var="board_name" value="<%=board_name %>"/>
<c:set var="board_description" value="<%=BoardDAO.getInstance().getBoard(board_name).getBoard_description() %>"/>
<ul class="list-group list-group-horizontal" id ="board_description">
	<li class="list-group-item w-25"><c:out value="${board_name}"/></li>
	<li class="list-group-item w-75" id="board_description_text"><c:out value="${board_description}"/></li>
</ul>
<ul class="list-group list-group-horizontal" id ="board_info">
	<li class="list-group-item w-25">게시판 관리자 : <%=manage %></li>
	<li class="list-group-item w-25">게시글쓰기 : <%=write %></li>
	<li class="list-group-item w-25">게시글읽기 : <%=read %></li>
	<li class="list-group-item w-25">댓글쓰기 : <%=comment %></li>
</ul>
<form class="form-inline my-2 my-lg-0" action="search_from_board.do" method="get">
	<input class="form-control mr-sm-2" type="search" placeholder="게시판 내 검색" aria-label="Search" name="search_word" required>
	<select class="form-control mr-sm-2" name="search_target" id="search_target">
     		<option value="title">제목</option>
     		<option value="content">본문</option>
		<option value="writer">작성자</option>
		<option value="origin_file_name">첨부파일이름</option>
	</select>
	<select class="form-control mr-sm-2" name="sort" id="sort">
     		<option value="write_date">최신순</option>
     		<option value="views">조회수 순</option>
	</select>
	<input type="hidden" name="board_name" value="<c:out value="${board_name}"/>">
	<button class="btn btn-outline-success my-2 my-sm-0" type="submit">게시판 내 검색</button>
</form>
<table class="table table-striped intable">
	<thead class="thead-light">
		<tr>
			<th>글번호</th>
			<th>글제목</th>
			<th>작성자</th>
			<th>작성일</th>
			<th>조회수</th>
		</tr>
	</thead>
	<%
	for(int i=0;i<list.size();i++){ // 공지 상단 우선 출력
		PostVO post = list.get(i);%>
		<%if(post.isIs_notice()){%><tr class="bg-primary notice">
			<c:set var="title" value="<%=post.getTitle() %>"/>
			<c:set var="writer" value="<%=post.getWriter() %>"/>
			<td><%=post.getPid() %></td>
			<td><a class="notice" href="postview.do?pid=<%=post.getPid()%>&board_name=<c:out value="${board_name}"/>"><c:out value="${title}"/></a></td>
			<td><a class="notice" href="getmember.do?member=<c:out value="${writer}"/>"><c:out value="${writer}"/></a></td>
			<td><%=post.getWrite_date().toString().substring(0, 10) %></td>
			<td><%=post.getViews() %></td>
		</tr>
	<% }}%>
	<%
	for(int i=(now-1)*10 ;i<=(now*10-1 < list.size()-1 ? now*10-1 : list.size()-1);i++){ // 일반 게시글 출력
		PostVO post = list.get(i);%>
		<%if(!post.isIs_notice()){%><tr>
			<c:set var="title" value="<%=post.getTitle() %>"/>
			<c:set var="writer" value="<%=post.getWriter() %>"/>
			<td><%=post.getPid() %></td>
			<td><a href="postview.do?pid=<%=post.getPid()%>&board_name=<c:out value="${board_name}"/>"><c:out value="${title}"/></a></td>
			<td><a href="getmember.do?member=<c:out value="${writer}"/>"><c:out value="${writer}"/></a></td>
			<td><%=post.getWrite_date().toString().substring(0, 10) %></td>
			<td><%=post.getViews() %></td>
		</tr>
	<% }}%>
</table>
	<ul class="pagination justify-content-center">
		<%if(total>10){ %>
			<li class="page-item"><a class="page-link" href="postsview.do?board_name=<c:out value="${board_name}"/>&page=1">&laquo;</a></li>
			<li class="page-item"><a class="page-link" href="postsview.do?board_name=<c:out value="${board_name}"/>&page=<%=start-1 >=1 ? start-1 : 1%>">&lt;</a></li>
		<%} %>
		<%for(int i=start;i<=end;i++) {
			if(i!=now){%>
				<li class="page-item"><a class="page-link" href="postsview.do?board_name=<c:out value="${board_name}"/>&page=<%=i%>"><%=i%></a></li>
			<%}else{%>
				<li class="page-item active"><a class="page-link" href="postsview.do?board_name=<c:out value="${board_name}"/>&page=<%=i%>"><%=i%></a></li>
			<%}%>
		<%} %>
		<%if(total>10){ %>
			<li class="page-item"><a class="page-link" href="postsview.do?board_name=<c:out value="${board_name}"/>&page=<%=end+1 >=total ? total : end+1%>">&gt;</a></li>
			<li class="page-item"><a class="page-link" href="postsview.do?board_name=<c:out value="${board_name}"/>&page=<%=total%>">&raquo;</a></li>
		<%} %>
		<%if(BoardDAO.getInstance().getPermissions_by_name(board_name).get("write")<=permission){ %>
			<li class="page-item"><a class="btn btn-success" href="writepost.jsp?board_name=<c:out value="${board_name}"/>">글쓰기</a></li>
		<%} %>
	</ul>
</div>
<br>
</div>
<script>
jQuery(document).ready(function(){
	setmain_from_inmain();
});
function ismobile(){
	if(window.innerWidth<768){
		$("#board_info").removeClass("list-group-horizontal");
		$("#board_description").removeClass("list-group-horizontal");
		$("li").removeClass("w-25");
		$("li").removeClass("w-75");
	}else{
		$("#board_info").addClass("list-group-horizontal");
		$("#board_description").addClass("list-group-horizontal");
		$("li").addClass("w-25");
		$("#board_description_text").removeClass("w-25");
		$(".nav-item").removeClass("w-25");
		$(".page-item").removeClass("w-25");
		$("#board_description_text").addClass("w-75");
	}
}
ismobile();
$(window).resize(ismobile);
</script>
</body>
</html>