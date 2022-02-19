<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="java.util.*,vo.*,dao.*"%>
	<%@ include file="WEB-INF/semipage/header.jsp" %>
	<%@ include file="WEB-INF/semipage/nav.jsp" %>
    <%
    PostDAO pdao=PostDAO.getinstance();
    	Post post = pdao.getPost(Integer.parseInt(request.getParameter("pid")));
    	if(!is_login){
    		response.sendRedirect("index.jsp");
    	}
    	String board_name = request.getParameter("board_name");
    	String csrf_token = (String)session.getAttribute("csrf_token");
    %>

<div class="inmain d-none">
<br>
	<form method="post" action="edit_post.do" onsubmit="postForm()">
	<div class="row">
			<div class="col-sm">
				<label for="pid">글번호</label> 
				<input class="form-control" type="text" name="pid" value=<%=post.getPid() %> readonly>
			</div>
			<div class="col-sm">
				<label for="title">제목</label> 
				<input class="form-control" type="text" name="title" value="<%=post.getTitle()%>">
			</div>
			<div class="col-sm">
				<label for="board_name">게시판 이름 </label> 
				<select class="form-control" name="board_name" id="board_name">
				<%
				for(int i=0;i<boardlist.size();i++){
				if(boardlist.get(i).getWrite_permission()>permission)continue;
				%>
				<c:set var="board_name" value="<%=boardlist.get(i).getBoard_name() %>"/>
					<%if(boardlist.get(i).getBoard_name().contentEquals(board_name)){%>
					<option value="<c:out value="${board_name}"/>" selected><c:out value="${board_name}"/></option>
					<%}else{ %>
					<option value="<c:out value="${board_name}"/>"><c:out value="${board_name}"/></option>
					<%} %>
				<%}%>
				</select>			
			</div>
			<div class="col-sm">
				<label for="writer">작성자 아이디</label> 
				<input class="form-control" type="text" name="writer" value=<%=(String) session.getAttribute("id")%> readonly>
			</div>
			<div class="col-sm">
				<label for="notice">공지</label> <input class="form-control"
					type="checkbox" name="is_notice" value="true">
			</div>
		</div>
		<textarea name="content" style="display: none;" class="contents"></textarea>
		<textarea id="summernote"><%=post.getContent() %></textarea>
		<input type="hidden" name="csrf_token" value="<%=csrf_token%>">
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
		$('.contents').val(markupStr);}
  setmain_from_inmain();
  </script>