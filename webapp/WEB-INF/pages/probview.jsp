<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import = "java.util.*,vo.*,org.jsoup.*,org.jsoup.nodes.*, org.jsoup.safety.*"%>
<%@ include file="../semipage/header.jsp" %>
<%@ include file="../semipage/nav.jsp" %>
<%
	Problem prob = (Problem)request.getAttribute("problem");
	ArrayList<Integer> solved_list = (ArrayList<Integer>)request.getAttribute("solved_list");
	ArrayList<Member> rank = (ArrayList<Member>)request.getAttribute("rank");

	Collections.sort(solved_list);
	if(!is_login){
		response.sendRedirect("index.jsp");
	}
	String csrf_token=(String)session.getAttribute("csrf_token");
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
	.addAttributes("u","style")
	.addAttributes("a","herf");
%>
<div class="inright d-none">
<br>
<ul class="list-group">
<%if(rank!=null){ 
	for(int i=0;i<rank.size();i++){
%>
	<c:set var="rankid" value="<%=rank.get(i).getId() %>"/>
	
	<%if(i==0){ %>
	<li class="list-group-item active">
	<%}else{ %>
	<li class="list-group-item">
	<%} %>
		<span class="badge badge-light"><%=i+1%></span>
		<c:out value="${rankid}"/>(<%=rank.get(i).getProb_score() %>)
	</li>
	<%}
	}%>
</ul>
</div>
<c:set var="title" value="<%=prob.getTitle() %>"/>
<c:set var="answer" value="<%=prob.getAnswer() %>"/>
<c:set var="writer" value="<%=prob.getWriter() %>"/>

<div class="inmain d-none">
<br>
<div class="card <%if(solved_list.indexOf(prob.getProbid())==-1){%>border-dark<%}else{ %>border-success<%} %> mb-3">
  <div class="card-body">
    <h5 class="card-title"><%=prob.getProbid()%>. <c:out value="${title}"/>[<%=prob.getPoint()%>]</h5>
    <h6 class="card-subtitle mb-2 text-muted">출제자 : <a href="getmember.do?member=<c:out value="${writer}"/>"><c:out value="${writer}"/></a></h6>
    <p class="card-text"><%=Jsoup.clean(prob.getContent().replaceAll("\n", "<br>"), mywhitelist) %></p>
    <%if(!prob.getWriter().contentEquals((String)session.getAttribute("id"))){ %>
    <form class="input-group" method="post" action="scoring.do">
    	<input type="hidden" name="probid" value="<%=prob.getProbid() %>">
    	<%if(solved_list.indexOf(prob.getProbid())==-1){ %>
    	<input class="form-control" placeholder="정답을 입력하세요." type ="text" name="answer">
    	<input class="btn btn-primary" type="submit" value="정답인증">
    	<%}else{ %>
    	<input class="form-control" type ="text" name="answer" value="<c:out value="${answer}"/>" readonly>
    	<%} %>
    	<input type="hidden" name="csrf_token" value="<%=csrf_token%>">
    </form>
    <%}else{ %>
    <form class="form-group" method="post" action="modifyprob.do">
    	<input class="form-control" placeholder="문제제목을 입력하세요." type ="text" name="title" required>
    	<select class="form-control" name="category" required>
			<option value="reversing">reversing</option>
			<option value="pwnable">pwnable</option>
			<option value="forensics">forensics</option>
			<option value="network">network</option>
			<option value="misc" selected>misc</option>
		</select>
    	<textarea class="form-control" placeholder="내용을 입력하세요." name="content" required><%=prob.getContent() %></textarea>
    	<select class="form-control" name="point" required>
			<option value="100" selected>100</option>
			<%for(int j=1;j<=10;j++) {%>
			<option value="<%=j*100 %>"><%=j*100 %></option>
			<%} %>
		</select>
    	<input class="form-control" placeholder="정답을 입력하세요." type ="text" name="answer" value="<c:out value="${answer}"/>" required>
    	<input type="hidden" name="probid" value="<%=prob.getProbid()%>">
    	<input type="hidden" name="csrf_token" value="<%=csrf_token%>">
    	<input class="btn btn-primary" type="submit" value="문제등록">
    </form> 
    <%} %>
  </div>
</div>
<br>
</div>
<script>
setmain_from_inmain();
setright_from_inright();
</script>