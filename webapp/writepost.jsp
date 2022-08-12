<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="java.util.*,dao.*,vo.*"%>
	<%@ include file="WEB-INF/semipage/header.jsp" %>
	<%@ include file="WEB-INF/semipage/nav.jsp" %>
<%
if(!is_login){
	response.sendRedirect("index.jsp");
}else if(AuthManager.canWriteBoard(auth, BoardService.getBoard(request.getParameter("board_name")))){ // 글쓰기 권한 > 내 권한
	response.sendRedirect("postsview.do?board_name="+request.getParameter("board_name")+"&page=1");
}
String csrf_token = (String)session.getAttribute("csrf_token");
if(request.getParameter("board_name")==null){
	response.sendRedirect("index.jsp");
}
session.setAttribute("board_name", request.getParameter("board_name"));
Cookie cookie = new Cookie("board_name",request.getParameter("board_name"));
cookie.setHttpOnly(true);
response.addCookie(cookie);
String member_id = (String)session.getAttribute("id");
%>
<c:set var="member_id" value="<%=member_id %>"/>
<div class="inmain d-none">
<br>
	<form method="post" action="writepost.do" enctype="multipart/form-data"
		onsubmit="postForm()">
		<div class="row">
			<div class="col-sm">
				<label for="title">제목</label> <input class="form-control"
					type="text" name="title">
			</div>
			<div class="col-sm">
				<label for="board_name">게시판 이름 </label> <input class="form-control"
					type="text" name="board_name"
					value=<%=request.getParameter("board_name")%> readonly>
			</div>
			<div class="col-sm">
				<label for="writer">작성자 아이디</label> <input class="form-control"
					type="text" name="writer"
					value=<c:out value="${member_id}"/> readonly>
			</div>
			<div class="col-sm">
				<label for="notice">공지</label> <input class="form-control"
					type="checkbox" name="is_notice" value="true">
			</div>
		</div>
		<textarea name="content" style="display: none;" class="contents"></textarea>
		<textarea id="summernote"></textarea>
		<input class="form-control" type="file" name="file">
		<input type="hidden" name="csrf_token" value="<%=csrf_token %>">
		<button type="submit" class="btn btn-primary">게시글 작성</button>
	</form>
	<br>
</div>

<script>
  $(document).ready(function() {
	  setSummerNote()
	});
  function postForm() {
		var markupStr = $('#summernote').summernote('code');
		$('.contents').val(markupStr);
		}
  setmain_from_inmain();
  </script>
</body>
</html>