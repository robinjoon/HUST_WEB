<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<%@ include file="WEB-INF/semipage/header.jsp" %>
	<%@ include file="WEB-INF/semipage/nav.jsp" %>
<%
if(is_login){
	response.sendRedirect("index.jsp");
}
%>

<div class="inmain d-none">
<br>
<div class="row">
	<form action="login.do" method="post" class="col-8 offset-2">
		<div class="form-group">
			<label for="id">User ID</label> 
			<input type="text"class="form-control" name="id" id="id" placeholder="ID">
		</div>
		<div class="form-group">
			<label for="pw">User PW</label> 
			<input type="password" class="form-control" name="pw" id="pw" placeholder="Password">
		</div>
		<button type="submit" class="btn btn-success btn-block">login</button>
	</form>
</div>
<br>
</div>
<script>
setmain_from_inmain();
</script>
</body>
</html>