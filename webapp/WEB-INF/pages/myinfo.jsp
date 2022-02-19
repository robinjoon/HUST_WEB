<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import = "vo.*,java.util.*,service.*"%>
<%@ include file="../semipage/header.jsp" %>
<%@ include file="../semipage/nav.jsp" %>
    <%
    	Member member = (Member)request.getAttribute("member");
    	ArrayList<Post> list = (ArrayList<Post>)request.getAttribute("list");
    	ArrayList<Comment> clist = (ArrayList<Comment>)request.getAttribute("clist");
    %>
    <%
	if(!is_login){
		response.sendRedirect("index.jsp");
	}
    String csrf_token=(String)session.getAttribute("csrf_token");
	%>
<div class="inmain d-none">
<br>
<div class="container">
	<div class="row">
		<div class="col-md-2">
			<button type="button" class="btn btn-primary" data-toggle="modal" data-target="#myinfo">내정보 확인</button>
		</div>
		<div class="col-md-2">
			<button type="button" class="btn btn-primary" onclick="myposts();">내 게시글 확인</button>
		</div>
		<div class="col-md-2">
			<button type="button" class="btn btn-primary" onclick="mycomments();">내가 단 댓글 확인</button>
		</div>
		<div class="col-md-2">
			<a type="button" class="btn btn-primary" href="notilist.do">
				알림 확인
				<span class="badge badge-pill badge-light"><%=unread_noti_count %></span>
			</a>
		</div>
	</div>
	<%if(list!=null&&!list.isEmpty()){ %>
	<div class="row d-none" id="myposts">
		<table class="table table-striped table-bordered">
			<caption>내가 쓴 게시글</caption>
			<thead class="thead-light">
				<tr>
					<th>글번호</th>
					<th>게시판</th>
					<th>글제목</th>
					<th>작성일</th>
				</tr>
				<%for(int i=0;i<list.size();i++){
					Post post = list.get(i);%>
					<c:set var="title" value="<%=post.getTitle() %>"/>
					<c:set var="board" value="<%=post.getBoard_name()%>"/>
				<tr>
					<td><%=post.getPid() %></td>
					<td><c:out value="${board}"/></td>
					<td><a href="postview.do?pid=<%=post.getPid()%>" class="intable"><c:out value="${title}"/></a></td>
					<td><%=post.getWrite_date().toString().substring(0, 10) %></td>
				</tr>
				<% }%>
			</thead>
		</table>
	</div>
	<div class="row d-none" id="mycomments">
		<table class="table table-striped table-bordered">
			<caption>내가 쓴 댓글</caption>
			<thead class="thead-light">
				<tr>
					<th>글번호</th>
					<th>댓글본문</th>
					<th>작성일</th>
				</tr>
				<%for(int i=0;i<clist.size();i++){
					Comment comment = clist.get(i);%>
					<c:set var="content" value="<%=comment.getContent() %>"/>
				<tr>
					<td><a href="postview.do?pid=<%=comment.getPid()%>" class="intable"><%=comment.getPid()%></a></td>
					<td><c:out value="${content}"/></td>
					<td><%=comment.getWrite_date().toString().substring(0, 10) %></td>
				</tr>
				<% }%>
			</thead>
		</table>
	</div>
	<%} %>
<div class="modal fade" id="myinfo" data-backdrop="static" tabindex="-1" role="dialog" aria-labelledby="myinfo" aria-hidden="true">
  <div class="modal-dialog modal-xl" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="exampleModalLabel">내정보</h5>
        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
          <span aria-hidden="true">&times;</span>
        </button>
      </div>
      <div class="modal-body">
		<form action="change_myinfo.do" method="post" id="change_myinfo_form">
			<div class="row">
			<div class="form-group col">
				<label for="id">아이디</label> <span class="badge badge-primary">필수</span>
				<input type="text"class="form-control" name="id" id="id" placeholder="아이디" value=<%=member.getId() %> readonly>
			</div>
			<div class="form-group col">
				<label class="text-danger" for="pw">비밀번호(비밀번호 변경을 원하지 않으면 공란으로 해주세요!!!)</label> 
				<input type="password" class="form-control pw" name="pw" id="pw" placeholder="비밀번호">
			</div>
			</div>
			<div class="row">
			<div class="form-group col">
				<label for="name">이름</label> <span class="badge badge-primary">필수</span>
				<input type="text" class="form-control" name="name" id="name" placeholder="이름" value=<%=member.getName() %>>
			</div>
			<div class="form-group col">
				<label for="scholastic">현재 학적상태</label> <span class="badge badge-primary">필수</span>
				<select class="form-control" name="scholastic" id="scholastic" required>
					<option value="신입" <%if(member.getScholastic().equals("신입"))out.print("selected"); %>>신입</option>
					<option value="재학" <%if(member.getScholastic().equals("재학"))out.print("selected"); %>>재학</option>
					<option value="일반휴학" <%if(member.getScholastic().equals("일반휴학"))out.print("selected"); %>>일반휴학</option>
					<option value="군휴학" <%if(member.getScholastic().equals("군휴학"))out.print("selected"); %>>군휴학</option>
					<option value="졸업" <%if(member.getScholastic().equals("졸업"))out.print("selected"); %>>졸업</option>
					<option value="탈퇴" <%if(member.getScholastic().equals("탈퇴"))out.print("selected"); %>>탈퇴</option>
				</select>
			</div>
			</div>
			<div class="row">
			<div class="form-group col">
				<label for="email">이메일</label> <span class="badge badge-warning">게시글/댓글알림을 받기 위해서는 필수(아직 미구현)</span>
				<input type="email"class="form-control" name="email" id="email" placeholder="이메일" value=<%=member.getEmail() %>>
			</div>
			<div class="form-group col">
				<label for="phone">전화번호</label> <span class="badge badge-secondary">선택</span>
				<input type="tel" class="form-control" name="phone" id="phone" placeholder="전화번호" value=<%=member.getPhone() %>>
			</div>
			</div>
			<div class="row">
			<div class="form-group col">
				<label for="birthY">생년</label> <span class="badge badge-secondary">선택</span>
				<input type="text" class="form-control" name="birthY" id="birthY" value=<%=member.getBirthY() %>>
			</div>
			<div class="form-group col">
				<label for="admissionY">입학년도</label> <span class="badge badge-secondary">선택</span>
				<input type="text" class="form-control" name="admissionY" id="admissionY" placeholder="입학년도" value=<%=member.getAdmissionY() %> >
			</div>
			<div class="form-group col">
				<label for="joinY">가입년도</label> <span class="badge badge-secondary">선택</span>
				<input type="text" class="form-control" name="joinY" id="joinY" placeholder="가입년도" value=<%=member.getJoinY() %> >
			</div>
			<div class="form-group col">
				<label for="school_year">학년</label> <span class="badge badge-secondary">선택</span>
				<select class="form-control" name="school_year" id="school_year">
					<%
					for(int i=1;i<=5;i++){%>
					<option value="<%=i%>" <%if(i==member.getSchool_year())out.print("selected"); %>><%=i%></option>
					<%	
						}
					%>
				</select>
			</div>
			</div>
			<div class="row">
			<div class="form-group col">
				<label for="address">주소</label> <span class="badge badge-secondary">선택</span>
				<input type="text"class="form-control" name="address" id="address" placeholder="주소" value=<%=member.getAddress() %>>
			</div>
			<div class="form-group col">
				<label for="address_now">현재 주소</label> <span class="badge badge-secondary">선택</span>
				<input list="address_now_list" class="form-control" name="address_now" id="address_now" placeholder="자취" value=<%=member.getAddress_now() %>>
				<datalist id="address_now_list">
					<option value="자취">
					<option value="신기숙사">
					<option value="구기숙사">
					<option value="통학">
					<option value="기타">
				</datalist>
			</div>
			</div>
			<div class="row">
			<div class="form-group col">
				<label for="interest">관심분야</label> <span class="badge badge-secondary">선택</span>
				<input type="text"class="form-control" name="interest" id="interest" placeholder="관심분야" value=<%=member.getInterest() %>>
			</div>
			<div class="form-group col">
				<label for="score">문제은행 점수</label> 
				<input class="form-control form-control-sm" type="text"class="form-control" name="score" id="score" value=<%=member.getProb_score() %>  readonly>
			</div>
			<div class="form-group col">
				<label for="etc">기타</label> <span class="badge badge-secondary">선택</span>
				<input type="text"class="form-control" name="etc" id="etc" placeholder="기타사항" value=<%=member.getEtc() %>>
			</div>
			</div>
			<div class="form-group">
				<label for="mypost_comment_noti_allow">내가 작성한 게시글에 달린 댓글 알림 허용</label> <span class="badge badge-secondary">선택</span>
				<select class="form-control" name="mypost_comment_noti_allow" id="mypost_comment_noti_allow">
					<%if(member.getMypost_comment_noti_allow()){ %>
					<option value="true" selected>허용</option>
					<option value="false">거부</option>
					<%}else{ %>
					<option value="true">허용</option>
					<option value="false" selected>거부</option>
					<%} %>
				</select>
			</div>
			<div class="form-group">
				<label for="mycomment_comment_noti_allow">내가 작성한 댓글(대댓글)에 달린 대댓글 알림 허용</label> <span class="badge badge-secondary">선택</span>
				<select class="form-control" name="mycomment_comment_noti_allow" id="mycomment_comment_noti_allow">
					<%if(member.getMycomment_comment_noti_allow()){ %>
					<option value="true" selected>허용</option>
					<option value="false">거부</option>
					<%}else{ %>
					<option value="true">허용</option>
					<option value="false" selected>거부</option>
					<%} %>
				</select>
			</div>
			<div class="form-group">
				<label for="call_noti_allow">댓글로 자신을 호출(태그)하는 것을 허용</label> <span class="badge badge-secondary">선택</span>
				<select class="form-control" name="call_noti_allow" id="call_noti_allow">
					<%if(member.getCall_noti_allow()){ %>
					<option value="true" selected>허용</option>
					<option value="false">거부</option>
					<%}else{ %>
					<option value="true">허용</option>
					<option value="false" selected>거부</option>
					<%} %>
				</select>
			</div>
			<input type="hidden" name="csrf_token" value="<%=csrf_token %>">
			<button class="btn btn-primary" type="button" onclick="sendform('change_myinfo_form');">내 정보 변경</button>
			<button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
		</form>
      </div>
    </div>
  </div>
</div>
</div>
<br>
</div>
<script>
setmain_from_inmain();
function myposts(){
	var a = $("#myposts");
	if(a.hasClass("d-none")){
		a.removeClass("d-none");
	}else{
		a.addClass("d-none");
	}
}
function mycomments(){
	var a = $("#mycomments");
	if(a.hasClass("d-none")){
		a.removeClass("d-none");
	}else{
		a.addClass("d-none");
	}
}
function sendform(id){
	var form = document.getElementById(id);
	form.submit();
}
</script>