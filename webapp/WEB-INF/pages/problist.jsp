<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="vo.*,java.util.*"%>
<%@ include file="../semipage/header.jsp" %>
<%@ include file="../semipage/nav.jsp" %>
<%
	ArrayList<ProblemVO> problist = (ArrayList<ProblemVO>)request.getAttribute("problems");
	ArrayList<Integer> solved_list = (ArrayList<Integer>)request.getAttribute("solved_list");
	ArrayList<MemberVO> rank = (ArrayList<MemberVO>)request.getAttribute("rank");
	ArrayList<String> category = (ArrayList<String>)request.getAttribute("category_list");
	HashMap<String,ArrayList<ProblemVO>> probmap = (HashMap<String,ArrayList<ProblemVO>>)request.getAttribute("probmap");
	Collections.sort(solved_list);
	category.add("문제추가");
	category.add("랭킹");
	if(!is_login){
		response.sendRedirect("index.jsp");
	}
	String csrf_token = (String)session.getAttribute("csrf_token");
%>
<div class="inmain d-none">
<br>
<ul class="nav nav-tabs" id="myTab" role="tablist">
<%for(int i=0;i<category.size();i++){ %>
  <li class="nav-item">
    <a class="nav-link<%if(i==0)out.print(" active"); %>" id="<%=category.get(i) %>-tab" data-toggle="tab" href="#<%=category.get(i) %>" role="tab" aria-controls="<%=category.get(i) %>" aria-selected="true"><%=category.get(i) %></a>
  </li>
<%} %>
</ul>
<div class="tab-content">
<%for(int i=0;i<category.size();i++){ %>
<div class="tab-pane<%if(i==0)out.print(" active"); %>" id="<%=category.get(i) %>" role="tabpanel" aria-labelledby="<%=category.get(i) %>-tab">
<%
	if(category.get(i).contentEquals("ALL")){%>
	<table class="table table-responsive table-bordered">
		<%for(int j=0;j<problist.size();j++){%>
		<%if(j%4==0){ %>
		<tr>
		<%} %>
		<td class="<%if(solved_list.indexOf(problist.get(j).getProbid())!=-1){%> text-white bg-success mb-3<%}%>">
		<c:set var="title" value="<%=problist.get(j).getTitle() %>"/>
		<c:set var="answer" value="<%=problist.get(j).getAnswer() %>"/>
		<c:set var="writer" value="<%=problist.get(j).getWriter() %>"/>
			<div class="card border-0<%if(solved_list.indexOf(problist.get(j).getProbid())!=-1){%> text-white bg-success mb-3<%}%>">
  				<div class="card-body">
    				<h5 class="card-title"><c:out value="${title}"/></h5>
    				<h6 class="card-subtitle mb-2">출제자 : <a href="getmember.do?member=<c:out value="${writer}"/>"><c:out value="${writer}"/></a> 점수 : <%=problist.get(j).getPoint() %></h6>
    				<%if(!session.getAttribute("id").equals(problist.get(j).getWriter())){
    					if(solved_list.indexOf(problist.get(j).getProbid())!=-1){%>
    					<a href="getprob.do?probid=<%=problist.get(j).getProbid() %>" class="btn btn-primary">문제보기</a>
    					<%}else{ %>
    					<a href="getprob.do?probid=<%=problist.get(j).getProbid() %>" class="btn btn-primary">문제풀기</a>
    					<%} %>
    				<%}else{%>
    				<div class="btn-group" role="group">
	    				<a href="getprob.do?probid=<%=problist.get(j).getProbid() %>" class="btn btn-primary">문제수정</a>
	    				<a href="deleteprob.do?probid=<%=problist.get(j).getProbid() %>&csrf_token=<%=csrf_token %>" class="btn btn-danger">문제삭제</a>
	    			</div>
    				<%} %>
  				</div>
			</div>
		</td>
		<%if((j+1)%4==0){ %>
		</tr>
		<%} %>
	<%	}%>
	</table>
	<%}else if(!category.get(i).contentEquals("문제추가")&&!category.get(i).contentEquals("랭킹")){%>
		<%ArrayList<ProblemVO> probs = probmap.get(category.get(i)); %>
		<table class="table table-responsive table-bordered">
		<%for(int j=0;j<probs.size();j++){%>
		<%if(j%(probs.size()>=4?4:probs.size())==0){ %>
		<tr>
		<%} %>
		<td class="<%if(solved_list.indexOf(probs.get(j).getProbid())!=-1){%> text-white bg-success mb-3<%}%>">
				<c:set var="title" value="<%=probs.get(j).getTitle() %>"/>
		<c:set var="content" value="<%=probs.get(j).getContent() %>"/>
		<c:set var="answer" value="<%=probs.get(j).getAnswer() %>"/>
		<c:set var="writer" value="<%=probs.get(j).getWriter() %>"/>
		<div class="card border-0 <%if(solved_list.indexOf(probs.get(j).getProbid())!=-1){%> text-white bg-success mb-3<%}%>">
				<div class="card-body">
				<h5 class="card-title"><c:out value="${title}"/></h5>
    			<h6 class="card-subtitle mb-2">출제자 : <a href="getmember.do?member=<c:out value="${writer}"/>"><c:out value="${writer}"/></a> 점수 : <%=probs.get(j).getPoint() %></h6>
				<%if(!session.getAttribute("id").equals(probs.get(j).getWriter())){
    					if(solved_list.indexOf(probs.get(j).getProbid())!=-1){%>
    						<a href="getprob.do?probid=<%=probs.get(j).getProbid() %>" class="btn btn-primary">문제보기</a>
    					<%}else{ %>
    						<a href="getprob.do?probid=<%=probs.get(j).getProbid() %>" class="btn btn-primary">문제풀기</a>
    					<%} %>
    			<%}else{%>
    				<div class="btn-group" role="group">
	    				<a href="getprob.do?probid=<%=probs.get(j).getProbid() %>" class="btn btn-primary">문제수정</a>
	    				<a href="deleteprob.do?probid=<%=probs.get(j).getProbid() %>&csrf_token=<%=csrf_token %>" class="btn btn-danger">문제삭제</a>
	    			</div>
    			<%} %>
				</div>
		</div>
		</td>
		<%if((j+1)%(probs.size()>=4?4:probs.size())==0){ %>
		</tr>
		<%} %>
<%		
		}%>
	</table>
<%	}else if(category.get(i).contentEquals("문제추가")){
%>	
		<div class="card">
  			<div class="card-body">
   	 			<form class="form-group" method="post" action="writeprob.do">
    				<input class="form-control" placeholder="문제제목을 입력하세요." type ="text" name="title" required>
    				<select class="form-control" name="category">
						<option value="reversing">reversing</option>
						<option value="pwnable">pwnable</option>
						<option value="forensics">forensics</option>
						<option value="network">network</option>
						<option value="misc" selected>misc</option>
					</select>
    				<textarea class="form-control" placeholder="내용을 입력하세요." name="content" required></textarea>
    				<select class="form-control" name="point">
						<option value="100" selected>100</option>
						<%for(int j=1;j<=10;j++) {%>
						<option value="<%=j*100 %>"><%=j*100 %></option>
						<%} %>
					</select>
    				<input class="form-control" placeholder="정답을 입력하세요." type ="text" name="answer" required>
    				<input type="hidden" name="csrf_token" value="<%=csrf_token%>">
    				<input class="btn btn-primary" type="submit" value="문제등록">
    			</form> 
  			</div>
		</div>
 
<%	}else if(category.get(i).contentEquals("랭킹")){ %>
		<ul class="list-group">
		<%if(rank!=null){ 
			for(int k=0;k<rank.size();k++){
		%>
			<c:set var="rankid" value="<%=rank.get(k).getId() %>"/>
			
			<%if(k==0){ %>
			<li class="list-group-item active">
			<%}else{ %>
			<li class="list-group-item">
			<%} %>
				<span class="badge badge-light"><%=k+1%></span>
				<c:out value="${rankid}"/>(<%=rank.get(k).getProb_score() %>) <%=rank.get(k).getProb_score_time() %>
			</li>
			<%}
			}%>
		</ul>
<%	} %>
</div>
<%} %>
</div>
<br>
</div>

<script>
var filter = "win16|win32|win64|macintel|mac|"; // PC일 경우 가능한 값
if( navigator.platform){
	if( filter.indexOf(navigator.platform.toLowerCase())<0 ){
		alert("모바일에선 문제은행을 이용할 수 없습니다.");
		window.history.back();
	}else{
		setmain_from_inmain();
	}
}
</script>
</body>
</html>