<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import= "java.util.*,vo.*,java.sql.Date"%>
<%@ include file="../semipage/header.jsp" %>
<%@ include file="../semipage/nav.jsp" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<%
ArrayList<Schedule> list = (ArrayList<Schedule>)request.getAttribute("schedules");

Calendar cal = Calendar.getInstance(); // 서버시간
	if(list!=null)
if(list.size()>0){
		cal.setTime(list.get(0).getS_date());
	}
String dates = request.getParameter("date"); // 사용자가 선택한 YYYY-MM
int year = cal.get(Calendar.YEAR); // 서버년도
int month = cal.get(Calendar.MONTH) +1; //서버 월
int date = cal.get(Calendar.DATE); // 서버 일
if(dates!=null && !dates.isEmpty()){ // 사용자가 년월을 선택하지 않았다면
		String[] arr =dates.split("-");
		try {
		year = Integer.parseInt(arr[0]); // 사용자 선택 년
		month = Integer.parseInt(arr[1]); // 사용자 선택 월
	}catch(NumberFormatException e) { //잘못된입력
		Calendar cl = Calendar.getInstance();
		year = cl.get(Calendar.YEAR);
		month = cl.get(Calendar.MONTH)+1;
	}
}
cal.set(year,month-1,1);
int start = 1;
int dow  = cal.get(Calendar.DAY_OF_WEEK);
	start = dow; // 시작요일.
int end = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
String csrf_token = (String)session.getAttribute("csrf_token");
%>
<!DOCTYPE html>
<html>
<head>
<style>
td{
white-space:nowrap; overflow:hidden; 
}
button.schedule{
width:80%;
overflow: hidden;
text-overflow: ellipsis;
white-space: nowrap;
}
.hasDatepicker {
    position: relative;
    z-index: 9999;
}
</style>
</head>
<body>
<div class="inmain d-none">
<br>
<h2><%=year %>/<%=month %></h2>
<div class="row">
	<form class="form-inline col" action ="schedules.do" method="get">
			<input class="form-control" type="text" name="date" id="datepicker" placeholder="<%=year%>-<%=month%>">
			<select class="form-control" name="what" id="what">
					<option value="all">all</option>
					<option value="yb">yb</option>
					<option value="ob">ob</option>
			</select>
			<button class="btn btn-primary" type="submit">확인</button>
	</form>
	<div class="btn-group col">
		<button class="btn btn-primary" type="button">OB 일정</button>
		<button class="btn btn-success" type="button">YB 일정</button>
	</div>	
</div>
	<table class="table table-bordered" style="table-layout: fixed;">
	<thead>
	    <tr>
	    	<th scope="col" class="text-center text-danger">일</th>
			<th scope="col" class="text-center">월</th>
			<th scope="col" class="text-center">화</th>
			<th scope="col" class="text-center">수</th>
			<th scope="col" class="text-center">목</th>
			<th scope="col" class="text-center">금</th>
			<th scope="col" class="text-center text-primary">토</th>
	    </tr>
	</thead>
  	<tbody>
  <%for(int i=0;i<6;i++){ %>
    	<tr>
      <%for(int j=0;j<7;j++){ %>
      		<td><%if(i*7+j+1>=start && i*7+j+2-start<=end ){ %>
      <%=i*7+j+2-start %>
      		<br>
      <%}else{ %>
      <br>
      <%} %>
      	<%for(int k=0;k<list.size();k++){
    	  	Date s_date = list.get(k).getS_date();
    	  	int day = s_date.getDate();
    	  	if(day==i*7+j+2-start){ // 일정이 있는 경우	
    	  %>
    	  <!-- Button trigger modal -->

<button type="button" class="btn btn-sm <%if(list.get(k).is_ob()){%>btn-primary<%}else{ %>btn-success<%} %> schedule" data-toggle="modal" data-target="#modal<%=k%>">
<c:set var="title" value="<%=list.get(k).getTitle() %>"/>
<c:out value="${title}"/>
</button>
<br>
<!-- 일정 모달-->
<div class="modal fade" id="modal<%=k%>" tabindex="-1" role="dialog" aria-labelledby="modal<%=k%>Title" aria-hidden="true">
  <div class="modal-dialog modal-dialog-scrollable" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="modal<%=k%>Title"><c:out value="${title}"/></h5>
        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
          <span aria-hidden="true">&times;</span>
        </button>
      </div>
      <div class="modal-body">
        <c:set var="place" value="<%=list.get(k).getPlace() %>"/>
        장소: <c:out value="${place}"/>
      	<hr>
		<%
			String str = list.get(k).getContent();
			str = str.replaceAll("\"", "&#034;");
			str = str.replaceAll("'", "&#039;");
			str = str.replaceAll("<", "&lt;");
			str = str.replaceAll(">", "&gt;");
			str = str.replaceAll("\n", "<br>");
		%>
		내용<br><%=str %>
		</div>
      <div class="modal-footer">
      	<a class="btn btn-danger" href="delete_schedule.do?sid=<%=list.get(k).getSid() %>&csrf_token=<%=csrf_token%>">일정삭제</a>
        <button type="button" class="btn btn-secondary" data-dismiss="modal">닫기</button>
      </div>
    </div>
  </div>
</div>
			<%}%>	
		<%}%>
	<%}%>
      </td>
    </tr>
  <%} %>
  </tbody>
</table>
<button type="button" class="btn btn-primary" data-toggle="modal" data-target="#addschedule">일정추가</button>		
<div class="modal fade" id="addschedule" data-backdrop="static" tabindex="-1" role="dialog" aria-labelledby="addschedulelabel" aria-hidden="true">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="addschedulelabel">일정추가</h5>
        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
          <span aria-hidden="true">&times;</span>
        </button>
      </div>
      <div class="modal-body">
		<form action="create_schedule.do" method="post">
			<div class="form-group">
				<div class="input-group">
					<div class="input-group-prepend">
    					<span class="input-group-text" id="s_date">날짜</span>
  					</div>
					<input class="form-control" type="text" name="s_date" id="datepicker2" aria-describedby="s_date">
				</div>
				<div class="input-group">
					<div class="input-group-prepend">
    					<span class="input-group-text" id="title">제목</span>
  					</div>
					<input class="form-control" type="text" name="title" aria-describedby="title">
				</div>
				<div class="input-group">
					<div class="input-group-prepend">
    					<span class="input-group-text" id="place">장소</span>
  					</div>
					<input class="form-control" type="text" name="place" aria-describedby="place">
				</div>				
				<div class="input-group">
					<div class="input-group-prepend">
    					<span class="input-group-text" id="content">내용</span>
  					</div>
					<textarea class="form-control" name="content" aria-describedby="content"></textarea>
				</div>
				<div class="input-group">
					<div class="input-group-prepend">
    					<span class="input-group-text" id="is_ob">OB</span>
  					</div>
					<input class="form-control" type="checkbox" value="true" name="is_ob">
				</div>
			</div>
			<button class="btn btn-primary" type="submit">일정추가</button>
			<input type="hidden" name="csrf_token" value="<%=csrf_token%>">
			<button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
		</form>
      </div>
    </div>
  </div>
</div>
<br>
</div>
<script>
setmain_from_inmain();
$( function() {
	$("#datepicker").datepicker( {
	    format: "yyyy-mm",
	    viewMode: "months", 
	    minViewMode: "months"
	});
  } );
$( function() {
	$("#datepicker2").datepicker( {
	    format: "yyyy-mm-dd"
	});
	$("#datepicker2").click(function(){
		$(".datepicker-container").css("z-index", "999999999");
	})
  } );
  
$( function() {
	$("#datepicker3").datepicker( {
	    format: "yyyy-mm-dd"
	});
	$("#datepicker3").click(function(){
		$(".datepicker-container").css("z-index", "999999999");
	})
  } );
</script>
</body>
</html>