<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import = "java.util.*,vo.*"%>
<%@ include file="../semipage/header.jsp" %>
<%@ include file="../semipage/nav.jsp" %>
    <%
    ArrayList<Member> list = (ArrayList<Member>)request.getAttribute("memberlist");
    %>
    <%
    if(!is_login){
    		response.sendRedirect("index.jsp");
    	}
        String csrf_token = (String)session.getAttribute("csrf_token");
    %>
<div class="inmain d-none">

<form class="form-inline" action="memberlist.do" method="get">
	<select class="form-control" name="group" id="group">
		<option value="전체">전체</option>
		<option value="신입">신입</option>
		<option value="재학">재학</option>
		<option value="일반휴학">일반휴학</option>
		<option value="군휴학">군휴학</option>
		<option value="졸업">졸업</option>
		<option value="탈퇴">탈퇴</option>
	</select>
	<select class="form-control" name="permission" id="permission">
		<option value="7">전체</option>
		<option value="0">게스트</option>
		<option value="1">신입회원</option>
		<option value="2">정회원</option>
		<option value="3">OB회원</option>
		<option value="4">YB운영진</option>
		<option value="5">OB운영진</option>
		<option value="6">관리자</option>
	</select>
	<button class="btn btn-primary"type=submit>멤버목록불러오기</button>
</form>
<form class="form-inline" action="getmember.do" method="get">
	<input class="form-control" name="member" placeholder="회원아이디">
	<button class="btn btn-success" type=submit>검색</button>
</form>
	<table class="table table-bordered">
		<thead>
			<tr>
			<th scope="col">아이디</th>
			<th scope="col">이름</th>
			<th scope="col" class="scholastic">학적</th>
			<th scope="col" class="school_year">학년</th>
			<th scope="col" class="interest">관심분야</th>
			<th scope="col">회원등급</th>
			<th scope="col">더보기</th>
			<%
			if(permission>=4){
			%>
			<th scope="col">회원등급변경하기</th>
			<%
			}
			%>
			</tr>
		</thead>
		<tbody>
			<%
			for(int i=0;i<list.size();i++){
						Member member = list.get(i);
			%>
				<c:set var="member_id" value="<%=member.getId() %>"/>
			  	<c:set var="member_name" value="<%=member.getName() %>"/>
			  	<c:set var="member_phone" value="<%=member.getPhone() %>"/>
			  	<c:set var="member_email" value="<%=member.getEmail() %>"/>
			  	<c:set var="member_scholastic" value="<%=member.getScholastic() %>"/>
				<c:set var="member_interest" value="<%=member.getInterest() %>"/>
				<c:set var="member_address" value="<%=member.getAddress() %>"/>
				<c:set var="member_address_now" value="<%=member.getAddress_now() %>"/>
				<c:set var="member_etc" value="<%=member.getEtc() %>"/>
				<tr>
				<td><c:out value="${member_id}"/></td>
				<td><c:out value="${member_name}"/></td>
				<td class="scholastic"><c:out value="${member_scholastic}"/></td>
				<td class="school_year"><%= member.getSchool_year() %></td>
				<td class="interest"><c:out value="${member_interest}"/></td>
				<td><%
					switch(member.getPermission()){
					case 0: 
						out.print("게스트");
						break;
					case 1: 
						out.print("신입회원");
						break;
					case 2: 
						out.print("정회원");
						break;
					case 3: 
						out.print("OB회원");
						break;
					case 4: 
						out.print("YB운영진");
						break;
					case 5: 
						out.print("OB운영진");
						break;
					case 6: 
						out.print("관리자");
						break;
					}
				%></td>
				<td><button type="button" class="btn btn-primary" data-toggle="modal" data-target="#member<c:out value="${member_id}"/>modal">더보기</button></td>
				<%if(permission>=4){ %>
				<td>
					<form class="form-inline" method="post" action="update_member_per.do">
					<input class="form-control" type="text" name ="id" value="<c:out value="${member_id}"/>" readonly>
					<select class="form-control" name="edit_per" id="edit_per">
					<%if(permission==4){ %>
						<option value="0">게스트</option>
						<option value="1">신입회원</option>
						<option value="2">정회원</option>
						<option value="4">YB운영진</option>
					<%}else if(permission==5){ %>
						<option value="2">정회원</option>
						<option value="3">OB회원</option>
						<option value="5">OB운영진</option>
					<%}else if(permission==6){ %>
						<option value="0">게스트</option>
						<option value="1">신입회원</option>
						<option value="2">정회원</option>
						<option value="3">OB회원</option>
						<option value="4">YB운영진</option>
						<option value="5">OB운영진</option>
						<option value="6">관리자</option>
					<%} %>
					</select>
					<input type="hidden" name="csrf_token" value="<%=csrf_token %>">
					<button class="btn btn-primary"type=submit>회원등급변경</button>
					</form>
				</td>
				<%} %>
				</tr>
				<div class="modal fade" id="member<c:out value="${member_id}"/>modal" tabindex="-1" role="dialog" aria-labelledby="<%=member.getId()%>kLabel" aria-hidden="true">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="<%=list.get(i).getId()%>kLabel">멤버정보</h5>
        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
          <span aria-hidden="true">&times;</span>
        </button>
      </div>
      <div class="modal-body">
      	<form>
			<div class="row">
			<div class="form-group col">
				<label for="id">아이디</label>
				<input type="text" class="form-control" name="id" id="id" placeholder="아이디"  value="<c:out value="${member_id}"/>" readonly>
			</div>
			<div class="form-group col">
				<label for="name">이름</label>
				<input type="text" class="form-control" name="name" id="name" placeholder="이름" value="<c:out value="${member_name}"/>" readonly>
			</div>
			</div>
			<div class="row">
			<div class="form-group col">
				<label for="phone">전화번호</label> 
				<input type="tel" class="form-control" name="phone" id="phone" placeholder="전화번호" value="<c:out value="${member_phone}"/>" readonly>
				
			</div>
			<div class="form-group col">
				<label for="email">이메일</label>
				<input type="email"class="form-control" name="email" id="email" placeholder="이메일" value="<c:out value="${member_email}"/>" readonly>
			</div>
			</div>
			<div class="row">
			<div class="form-group col">
				<label for="birthY">생년</label> 
				<input type="text" class="form-control" name="birthY" id="birthY" placeholder="생년" value="<%=member.getBirthY() %>" readonly>
			</div>
			<div class="form-group col">
				<label for="admissionY">입학년도</label> 
				<input type="text" class="form-control" name="admissionY" id="admissionY" placeholder="입학년도" value="<%=member.getAdmissionY() %>" readonly>
			</div>
			<div class="form-group col">
				<label for="joinY">가입년도</label> 
				<input type="text" class="form-control" name="joinY" id="joinY" placeholder="가입년도" value="<%=member.getJoinY() %>" readonly>
			</div>
			<div class="form-group col">
				<label for="school_year">학년</label> 
				<input type ="text" class="form-control" name="school_year" id="school_year" value="<%=member.getSchool_year() %>" readonly>
			</div>
			</div>
			<div class="row">
			<div class="form-group col">
				<label for="scholastic">현재 학적상태</label>
				<input type="text" class="form-control" name="scholastic" id="scholastic" value="<c:out value="${member_scholastic}"/>" readonly>
			</div>
			<div class="form-group col">
				<label for="interest">관심분야</label> 
				<input type="text"class="form-control" name="interest" id="interest" placeholder="관심분야" value="<c:out value="${member_interest}"/>" readonly>
			</div>
			</div>
			<div class="form-group">
				<label for="address_now">현재 주소</label> 
				<input type="text" class="form-control" name="address_now" id="address_now" placeholder="자취" value="<c:out value="${member_address_now}"/>" readonly>
			</div>
			<div class="form-group">
				<label for="address">주소</label> 
				<input type="text"class="form-control" name="address" id="address" placeholder="주소" value="<c:out value="${member_address}"/>" readonly>
			</div>
			<div class="form-group">
				<label for="etc">기타</label> 
				<input type="text"class="form-control" name="etc" id="etc" placeholder="기타사항" value="<c:out value="${member_etc}"/>" readonly>
			</div>
			<div class="form-group">
				<label for="score">문제은행 점수</label> 
				<input type="text"class="form-control" name="score" id="score" value="<%=member.getProb_score() %>" readonly>
			</div>
		</form>
      </div>
      <div class="modal-footer">
        <a type="button" class="btn btn-warning" href="resetpw.do?id=<%=member.getId()%>&csrf_token=<%=csrf_token%>">비밀번호 초기화</a>
        <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
      </div>
    </div>
  </div>
</div>
				<%}%>
		</tbody>
	</table>
</div>
<script>
setmain_from_inmain();
$(function(){
	var url = decodeURIComponent(location.href);
	url = decodeURIComponent(url);
	var params;
    params = url.substring( url.indexOf('?')+1, url.length );
    params = params.split("&");
    var size = params.length;
    var key, value;
    for(var i=0 ; i < size ; i++) {
        key = params[i].split("=")[0];
        value = params[i].split("=")[1];
        params[key] = value;
    }
 	$("#group").val(params["group"]);
 	$("#permission").val(params["permission"]);
});
function ismobile(){
	if(window.innerWidth<768){
		$(".scholastic").hide();
		$(".interest").hide();
		$(".school_year").hide();
	}else{
		$(".scholastic").show();
		$(".interest").show();
		$(".school_year").show();
	}
}
ismobile();
$(window).resize(ismobile);
</script>