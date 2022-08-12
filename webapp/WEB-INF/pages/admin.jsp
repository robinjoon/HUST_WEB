<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="java.util.*,vo.*,dao.*"%>
<%@ include file="../semipage/header.jsp" %>
<%@ include file="../semipage/nav.jsp" %>
<%
if(!is_admin){
		response.sendRedirect("index.jsp");
	}
String csrf_token = (String)session.getAttribute("csrf_token");
%>
<div class="inmain d-none">
<br>
<ul class="nav nav-tabs" id="myTab" role="tablist">
  <li class="nav-item">
    <a class="nav-link active" id="add-tab" data-toggle="tab" href="#add" role="tab" aria-controls="add" aria-selected="true">게시판 추가</a>
  </li>
  <li class="nav-item">
    <a class="nav-link" id="remove-tab" data-toggle="tab" href="#remove" role="tab" aria-controls="remove" aria-selected="false">게시판 삭제</a>
  </li>
  <li class="nav-item">
    <a class="nav-link" id="edit-tab" data-toggle="tab" href="#edit" role="tab" aria-controls="edit" aria-selected="false">게시판 수정</a>
  </li>
  <li class="nav-item">
    <a class="nav-link" id="connect-tab" data-toggle="tab" href="#connect_member" role="tab" aria-controls="connect_member" aria-selected="false">현재접속자</a>
  </li>
</ul>
<div class="tab-content" id="myTabContent">
  <div class="tab-pane fade show active" id="add" role="tabpanel" aria-labelledby="add-tab">
  <ul class="list-group">
		<li class="list-group-item">
			<h3>게시판 추가메뉴</h3>
			<h4 class="bg-danger text-warning">게시판이름에 띄어쓰기를 절대 넣지 마세요.</h4>
			<form action="create_board.do" method="post">
				<label for="board_name">게시판 이름</label> 
				<input type="text"class="form-control" name="board_name" id="board_name" placeholder="게시판이름">
				<label for="board_description">게시판 설명</label> 
				<textarea class="form-control" name="board_description" id="board_description" placeholder="게시판 설명"></textarea>
				<label for="read_permission">읽기권한</label> 
				<select class="form-control" name="rp" id="rp">
					<%
					if(per==6){
					%>
					<option value=1>신입회원</option>
					<option value=2>정회원</option>
					<option value=3>OB회원</option>
					<option value=4>YB운영진</option>
					<option value=5>OB운영진</option>
					<option value=6>관리자</option>
					<%
					}else if(per==4){
					%>
					<option value=1>신입회원</option>
					<option value=2>정회원</option>
					<option value=4>YB운영진</option>
					<%
					}else{
					%>
					<option value=3>OB회원</option>
					<option value=5>OB운영진</option>
					<%
					}
					%>
				</select>
				<label for="write_permission">쓰기권한</label> 
				<select class="form-control" name="wp" id="wp">
					<%
					if(per==6){
					%>
					<option value=1>신입회원</option>
					<option value=2>정회원</option>
					<option value=3>OB회원</option>
					<option value=4>YB운영진</option>
					<option value=5>OB운영진</option>
					<option value=6>관리자</option>
					<%
					}else if(per==4){
					%>
					<option value=1>신입회원</option>
					<option value=2>정회원</option>
					<option value=4>YB운영진</option>
					<%
					}else{
					%>
					<option value=3>OB회원</option>
					<option value=5>OB운영진</option>
					<%
					}
					%>
				</select>
				<label for="manage_permission">관리권한</label> 
				<select class="form-control" name="mp" id="mp">
					<%
					if(per==6){
					%>
					<option value=4>YB운영진</option>
					<option value=5>OB운영진</option>
					<option value=6>관리자</option>
					<option value=7>YB/OB공동관리</option>
					<%
					}else{
					%>
					<option value=<%=per%>><%=per%></option>
					<option value=7>YB/OB공동관리</option>
					<%
					}
					%>
				</select>
				<label for="comment_permission">댓글권한</label> 
				<select class="form-control" name="cp" id="cp">
					<%
					if(per==6){
					%>
					<option value=1>신입회원</option>
					<option value=2>정회원</option>
					<option value=3>OB회원</option>
					<option value=4>YB운영진</option>
					<option value=5>OB운영진</option>
					<option value=6>관리자</option>
					<%
					}else if(per==4){
					%>
					<option value=1>신입회원</option>
					<option value=2>정회원</option>
					<option value=4>YB운영진</option>
					<%
					}else{
					%>
					<option value=3>OB회원</option>
					<option value=5>OB운영진</option>
					<%
					}
					%>
				</select>
				<input type="hidden" name="csrf_token" value="<%=csrf_token%>">
			<button type="submit" class="btn btn-primary">게시판 생성</button>
			</form>
		</li>
	</ul>
  </div>
  <div class="tab-pane fade" id="remove" role="tabpanel" aria-labelledby="remove-tab">
  <ul class="list-group">
		<li class="list-group-item"><h3>게시판 삭제메뉴</h3></li>
		<%
		for(int i = 0; i < boardlist.size(); i++) {
		%>
			<%
			if(AuthManager.canManageBoard(auth, boardlist.get(i))){
			%>
			<li class="list-group-item"><%=boardlist.get(i).getBoard_name()%>
				<a class='btn btn-danger btn-sm' href='delete_board.do?board_name=<%=boardlist.get(i).getBoard_name()%>&csrf_token=<%=csrf_token%>' role='button' >삭제</a>
			</li>
			<%
			}
			%>
		<%
		}
		%>
	</ul>
  </div>
  <div class="tab-pane fade" id="edit" role="tabpanel" aria-labelledby="edit-tab">
  	<ul class="list-group">
		<li class="list-group-item">
			<h3>게시판 수정메뉴</h3>
			<h4 class="bg-danger text-warning">게시판이름에 띄어쓰기를 절대 넣지 마세요.</h4>
			<form action="update_board.do" method="post">
				<label for="board_name">게시판 이름</label> 
				<select class="form-control" name="board_name" id="board_name">
					<%
					LinkedList<Board> blist=BoardDAO.getInstance().getBoardlist(permission);
								for(int i=0;i<blist.size();i++){
									Board board = blist.get(i);
					%>
					<c:set var="bname" value="<%=board.getBoard_name() %>"/>
					<option value=<c:out value="${bname}"/>><c:out value="${bname}"/></option>
					<% }
					%>
				</select>
				<label for="board_description">게시판 설명</label> 
				<textarea class="form-control" name="board_description" id="board_description" placeholder="게시판 설명"></textarea>
				<label for="read_permission">읽기권한</label> 
				<select class="form-control" name="rp" id="rp">
					<%if(per==6){ %>
					<option value=1>신입회원</option>
					<option value=2>정회원</option>
					<option value=3>OB회원</option>
					<option value=4>YB운영진</option>
					<option value=5>OB운영진</option>
					<option value=6>관리자</option>
					<%}else if(per==4){ %>
					<option value=1>신입회원</option>
					<option value=2>정회원</option>
					<option value=4>YB운영진</option>
					<%}else{ %>
					<option value=3>OB회원</option>
					<option value=5>OB운영진</option>
					<%} %>
				</select>
				<label for="write_permission">쓰기권한</label> 
				<select class="form-control" name="wp" id="wp">
					<%if(per==6){ %>
					<option value=1>신입회원</option>
					<option value=2>정회원</option>
					<option value=3>OB회원</option>
					<option value=4>YB운영진</option>
					<option value=5>OB운영진</option>
					<option value=6>관리자</option>
					<%}else if(per==4){ %>
					<option value=1>신입회원</option>
					<option value=2>정회원</option>
					<option value=4>YB운영진</option>
					<%}else{ %>
					<option value=3>OB회원</option>
					<option value=5>OB운영진</option>
					<%} %>
				</select>
				<label for="manage_permission">관리권한</label> 
				<select class="form-control" name="mp" id="mp">
					<%if(per==6){ %>
					<option value=4>YB운영진</option>
					<option value=5>OB운영진</option>
					<option value=6>관리자</option>
					<option value=7>YB/OB공동관리</option>
					<%}else{ %>
					<option value=<%=per %>><%=per %></option>
					<%} %>
				</select>
				<label for="comment_permission">댓글권한</label> 
				<select class="form-control" name="cp" id="cp">
					<%if(per==6){ %>
					<option value=1>신입회원</option>
					<option value=2>정회원</option>
					<option value=3>OB회원</option>
					<option value=4>YB운영진</option>
					<option value=5>OB운영진</option>
					<option value=6>관리자</option>
					<%}else if(per==4){ %>
					<option value=1>신입회원</option>
					<option value=2>정회원</option>
					<option value=4>YB운영진</option>
					<%}else{ %>
					<option value=3>OB회원</option>
					<option value=5>OB운영진</option>
					<%} %>
				</select>
				<input type="hidden" name="csrf_token" value="<%=csrf_token %>">
			<button type="submit" class="btn btn-primary">게시판 생성</button>
			</form>
		</li>
	</ul>
  </div>
  <div class="tab-pane fade" id="connect_member" role="tabpanel" aria-labelledby="connect-tab">
  	<div class="list-group">
  
	</div>
  </div>
</div>
<br>
</div>

<script>
jQuery(document).ready(function(){
	setmain_from_inmain();
});
var webSocket = new WebSocket("wss://hust.net/loginlist");
var messageTextArea = document.getElementById("connect_member");
// WebSocket 서버와 접속이 되면 호출되는 함수
webSocket.onopen = function(message) {
	// 콘솔 텍스트에 메시지를 출력한다.
	messageTextArea.value += "Server connect...\n";
};
// WebSocket 서버와 접속이 끊기면 호출되는 함수
webSocket.onclose = function(message) {
	// 콘솔 텍스트에 메시지를 출력한다.
	messageTextArea.value += "Server Disconnect...\n";
};
// WebSocket 서버와 통신 중에 에러가 발생하면 요청되는 함수
webSocket.onerror = function(message) {
	// 콘솔 텍스트에 메시지를 출력한다.
	messageTextArea.value += "error...\n";
};
webSocket.onmessage = function(message) {
	$("#connect_member").html(message.data)
	console.log(message)
}
function waitForSocketConnection(socket, callback){
    setTimeout(
        function () {
            if (socket.readyState === 1) {
                console.log("Connection is made")
                if (callback != null){
                    callback();
                }
            } else {
                console.log("wait for connection...")
                waitForSocketConnection(socket, callback);
            }

        }, 5); // wait 5 milisecond for the connection...
}
function sendMessage(msg){
    // Wait until the state of the socket is not ready and send the message when it is...
    waitForSocketConnection(webSocket, function(){
        console.log("message sent!!!");
        webSocket.send(msg);
    });
}
sendMessage("<%=id%> csrf:<%=csrf_token_s%>");
setInterval(function(){
	sendMessage("<%=id%> csrf:<%=csrf_token_s%>");
	}, 5000);
</script>