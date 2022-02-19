<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ include file="WEB-INF/semipage/header.jsp" %>
<%@ include file="WEB-INF/semipage/nav.jsp" %>
<div class="inmain d-none">
<br>
		<form action="join.do" method="post" id="jform">
			<div class="form-group">
				<label for="id">아이디</label> <span class="badge badge-primary">필수(아이디에는 숫자,한글,알파벳 대소문자만 사용할 수 있습니다.)</span>
				<input type="text"class="form-control" name="id" id="id" placeholder="아이디" required>
				<button class="btn btn-primary" type="button" onclick="sendMessage();" id="id_check">아이디중복 확인</button>
			</div>
			<div class="alert alert-success" id="id-success">사용가능한 아이디 입니다.</div> 
			<div class="alert alert-danger" id="id-danger">사용할 수 없는 아이디 입니다.</div>
			<div class="row">
			<div class="form-group col">
				<label for="pw">비밀번호</label> <span class="badge badge-primary">필수</span>
				<input type="password" class="form-control pw" name="pw" id="pw" placeholder="비밀번호" required>
			</div>
			<div class="form-group col">
				<label for="pw_check">비밀번호 확인</label> <span class="badge badge-primary">필수</span>
				<input type="password" class="form-control pw" name="pw_check" id="pw_check" placeholder="비밀번호 확인" required>
			</div>
			</div>
			<div class="alert alert-success" id="alert-success">비밀번호가 일치합니다.</div> 
			<div class="alert alert-danger" id="alert-danger">비밀번호가 일치하지 않습니다.</div>
			<div class="row">
			<div class="form-group col">
				<label for="name">이름</label> <span class="badge badge-primary">필수(이름에는 숫자,한글,알파벳 대소문자만 사용할 수 있습니다.)</span>
				<input type="text" class="form-control" name="name" id="name" onchange="check_name" placeholder="이름" required>
			</div>
			<div class="form-group col">
				<label for="scholastic">현재 학적상태</label> <span class="badge badge-primary">필수</span>
				<select class="form-control" name="scholastic" id="scholastic" required>
					<option value="신입">신입</option>
					<option value="재학">재학</option>
					<option value="일반휴학">일반휴학</option>
					<option value="군휴학">군휴학</option>
					<option value="졸업">졸업</option>
					<option value="탈퇴">탈퇴</option>
				</select>
			</div>
			</div>
			<div class="row">
			<div class="form-group col">
				<label for="email">이메일</label> <span class="badge badge-warning">게시글/댓글알림을 받기 위해서는 필수(아직 미구현)</span>
				<input type="email"class="form-control" name="email" id="email" placeholder="이메일">
			</div>
			<div class="form-group col">
				<label for="phone">전화번호</label> <span class="badge badge-secondary">선택</span>
				<input type="tel" class="form-control" name="phone" id="phone" placeholder="전화번호">
			</div>
			</div>
			<div class="row">
			<div class="form-group col">
				<label for="birthY">생년</label> <span class="badge badge-secondary">선택</span>
				<select class="form-control" name="birthY" id="birthY">
					<option value="2002">2002</option>
					<option value="2001" selected>2001</option>
				<%for(int i=2000;i>=1960;i--){ %>
					<option value="<%=i%>"><%=i%></option>
				<%} %>
				</select>
			</div>
			<div class="form-group col">
				<label for="admissionY">입학년도</label> <span class="badge badge-secondary">선택</span>
				<select class="form-control" name="admissionY" id="admissionY">
					<option value="2020" selected>2020</option>
				<%for(int i=2019;i>=1980;i--){ %>
					<option value="<%=i%>"><%=i%></option>
				<%} %>
				</select>
			</div>
			<div class="form-group col">
				<label for="joinY">가입년도</label> <span class="badge badge-secondary">선택</span>
				<select class="form-control" name="joinY" id="joinY">
					<option value="2020" selected>2020</option>
				<%for(int i=2019;i>=1980;i--){ %>
					<option value="<%=i%>"><%=i%></option>
				<%} %>
				</select>
			</div>
			<div class="form-group col">
				<label for="school_year">학년</label> <span class="badge badge-secondary">선택</span>
				<select class="form-control" name="school_year" id="school_year">
					<option value="1" selected>1</option>
					<option value="2">2</option>
					<option value="3">3</option>
					<option value="4">4</option>
					<option value="5">5</option>
				</select>
			</div>
			</div>
			<div class="row">
			<div class="form-group col">
				<label for="address">주소</label> <span class="badge badge-secondary">선택</span>
				<input type="text"class="form-control" name="address" id="address" placeholder="주소">
			</div>
			<div class="form-group col">
				<label for="address_now">현재 주소</label> <span class="badge badge-secondary">선택</span>
				<select class="form-control" name="address_now" id="address_now">
					<option value="자취">자취</option>
					<option value="신기숙사">신기숙사</option>
					<option value="구기숙사">구기숙사</option>
					<option value="통학">통학</option>
					<option value="기타" selected>기타</option>
				</select>
			</div>
			</div>
			<div class="row">
			<div class="form-group col">
				<label for="interest">관심분야</label> <span class="badge badge-secondary">선택</span>
				<input type="text"class="form-control" name="interest" id="interest" placeholder="관심분야">
			</div>
			<div class="form-group col">
				<label for="etc">기타</label> <span class="badge badge-secondary">선택</span>
				<input type="text"class="form-control" name="etc" id="etc" placeholder="기타사항">
			</div>
			</div>
			<div class="form-group">
				<label for="mypost_comment_noti_allow">내가 작성한 게시글에 달린 댓글 알림 허용</label> <span class="badge badge-secondary">선택</span>
				<select class="form-control" name="mypost_comment_noti_allow" id="mypost_comment_noti_allow">
					<option value="true" selected>허용</option>
					<option value="false">거부</option>
				</select>
			</div>
			<div class="form-group">
				<label for="mycomment_comment_noti_allow">내가 작성한 댓글(대댓글)에 달린 대댓글 알림 허용</label> <span class="badge badge-secondary">선택</span>
				<select class="form-control" name="mycomment_comment_noti_allow" id="mycomment_comment_noti_allow">
					<option value="true" selected>허용</option>
					<option value="false">거부</option>
				</select>
			</div>
			<div class="form-group">
				<label for="call_noti_allow">댓글로 자신을 호출(태그)하는 것을 허용</label> <span class="badge badge-secondary">선택</span>
				<select class="form-control" name="call_noti_allow" id="call_noti_allow">
					<option value="true" selected>허용</option>
					<option value="false">거부</option>
				</select>
			</div>
			<button type="button" class="btn btn-success" id="join" onclick="sendform('jform')">회원가입</button>
		</form>
		<br>
	</div>
	<script>
		setmain_from_inmain();
		var id_ok = false;
		var pw_ok = false;
		var name_ok = false;
		$("#join").hide();
		$("#id-success").hide(); 
		$("#id-danger").hide();
		var webSocket = new WebSocket("wss://hust.net/idcheck");
		webSocket.onmessage = function(message) {
			if(message.data=="true"){
				$("#id").attr("readonly","");
				$("#id-success").show(); 
				$("#id-danger").hide();
				$("#id_check").hide();
				id_ok = true;
			}else{
				$("#id-success").hide(); 
				$("#id-danger").show();
				id_ok = false;
			}
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
		function sendMessage(){
		    // Wait until the state of the socket is not ready and send the message when it is...
		    waitForSocketConnection(webSocket, function(){
		        if($("#id").val().length>0){
		        	console.log("message sent!!!");
			        webSocket.send($("#id").val());
		        }
		    });
		}
		function checkpw(){ 
			$("#alert-success").hide(); 
			$("#alert-danger").hide();
			$("input").keyup(function(){
				var pwd1=$("#pw").val();
				var pwd2=$("#pw_check").val(); 
				if(pwd1 != "" || pwd2 != ""){ 
					if(pwd1 == pwd2){ 
						if(pwd1.length>=8){
							$("#alert-success").show(); 
							$("#alert-danger").hide(); 
							pw_ok=true;
						}else{
							$("#alert-danger").text("비밀번호는 8자리 이상이여야 합니다.");
							$("#alert-success").hide(); 
							$("#alert-danger").show();
						}
						can_join();
					}else{ 
						$("#alert-danger").text("비밀번호가 일치하지 않습니다.");
						$("#alert-success").hide(); 
						$("#alert-danger").show();
						pw_ok = false;
						can_join();
						} 
					} 
				}); 
			}
		checkpw();
		function can_join(){
			if(id_ok&&pw_ok){
				$("#join").show();
			}else{
				$("#join").hide();
			}
		}
		setInterval(can_join, 3000);
		function sendform(id){
			var form = document.getElementById(id);
			form.submit();
		}
	</script>
</body>
</html>