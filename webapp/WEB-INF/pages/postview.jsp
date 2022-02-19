<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="vo.*, java.util.*, dao.BoardDAO, tools.*, org.jsoup.*,org.jsoup.nodes.*, org.jsoup.safety.*"%>
<%@ include file="../semipage/header.jsp" %>
<%@ include file="../semipage/nav.jsp" %>
<%
	Post post = (Post)request.getAttribute("post");
	ArrayList<ArrayList<vo.Comment>>comments = (ArrayList<ArrayList<vo.Comment>>)request.getAttribute("comments");
	if(!is_login){
		response.sendRedirect("index.jsp");
	}
	Whitelist mywhitelist = Whitelist.relaxed();
	mywhitelist.addProtocols("img", "src", "http", "https", "data")
	.addTags("font","iframe","i","img")
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
	.addAttributes("a","herf")
	.addAttributes("img","class");
	String csrf_token = (String)session.getAttribute("csrf_token");
	int manage= (int)request.getAttribute("manage_per");
	int comment_per = (int)request.getAttribute("comment_per");
%>
<c:set var="title" value="<%=post.getTitle() %>"/>
<c:set var="writer" value="<%=post.getWriter() %>"/>
<c:set var="board_name" value="<%=post.getBoard_name() %>"/>
<c:set var="system_file_name" value="<%=post.getSystem_file_name() %>"/>
<c:set var="origin_file_name" value="<%=post.getOrigin_file_name() %>"/>
<div class="inmain d-none">
<br>
	<i class="fas fa-chalkboard"><c:out value="${board_name}"/></i>
	<hr>
	<ul class="list-group">
		<li class="list-group-item"><h3><c:out value="${title}"/></h3></li>
		<li class="list-group-item"><i class="fas fa-eye"></i><%=post.getViews() %></li>
		<li class="list-group-item"><i class="fas fa-pen"></i><a href="getmember.do?member=<c:out value="${writer}"/>"><c:out value="${writer}"/></a></li>
		<li class="list-group-item"><i class="fas fa-clock"></i><%=post.getWrite_date() %></li>
		<li class="list-group-item"><%=Jsoup.clean(post.getContent(), mywhitelist)%></li>
		<%if(post.getSystem_file_name()!=null&&!post.getSystem_file_name().isEmpty()&&post.getOrigin_file_name()!=null){ %>
    	<li class="list-group-item"><i class="fas fa-file"></i><a href="download.do?pid=<%=post.getPid() %>"><c:out value="${origin_file_name}"/></a></li>
		<%} %>
		<li class="list-group-item">
		<%if(session.getAttribute("id").equals(post.getWriter())){ %>
			<a type="button" class="btn btn-warning float-right" href="edit_post.jsp?pid=<%=post.getPid()%>&writer=<c:out value="${writer}"/>&board_name=<c:out value="${board_name}"/>">게시글 수정</a>
		<%} %>
		<%if(session.getAttribute("id").equals(post.getWriter())||per == manage||per==6||(manage==7&&permission>=4)){ %>
			<a type="button" class="btn btn-danger float-right" href="delete_post.do?pid=<%=post.getPid()%>&writer=<c:out value="${writer}"/>&board_name=<c:out value="${board_name}"/>&csrf_token=<%=csrf_token%>">게시글 삭제</a>
		<%} %>
			<a type="button" class="btn btn-primary " href="postsview.do?board_name=<c:out value="${board_name}"/>&page=1">글목록</a>
		</li>
	</ul>
	<form action="write_comment.do" method="post" id="write_comment_form">
		<input type="hidden" value=<%=post.getPid() %> name="pid">
		<input type="hidden" value=<c:out value="${board_name}"/> name="board_name">
  		<input type="hidden" name="csrf_token" value="<%=csrf_token %>">
  		<%if(permission>=comment_per){ %>
  		<div class="input-group">
	  		<div class="input-group-prepend">
	    		<span class="input-group-text">댓글</span>
			</div>
			<textarea class="form-control" id="comment_input" aria-label="content" placeholder="댓글작성" name="content" required></textarea>
		</div>
		<div class="input-group">
			<div class="input-group-prepend">
	    		<span class="input-group-text">태그</span>
			</div>
			<input type="text" class="form-control" aria-label="태그" placeholder="알림을 보낼 회원의 id를 ','를 구분자로 입력하세요." name="tags" id="comment_tags_0"/>
  		</div>
 		<ul class="list-group" id="comment_tag_list_0">
 			<c:set var="member_id" value="<%=id %>"/>
			<li class="list-group-item"><input class="form-control" type="text" placeholder="태그할 회원을 이름이나 아이디로 검색하세요." id="comment_tag_list_input_0" onchange="sendMessage('<c:out value="${member_id}"/> csrf:<%=csrf_token_s%>',this,'comment_tag_list_0');"></li>
		</ul>
  		<%}%>
  		<button type="button" class="btn btn-primary" onclick="sendform('write_comment_form');">작성</button>
	</form>
	<div class="list-group">
		<%if(comments!=null){
			for(int i=0;i<comments.size();i++){
			ArrayList<vo.Comment> commentlist = comments.get(i);
			vo.Comment comment = commentlist.get(0);
		%>
		<c:set var="c_content" value="<%=comment.getContent() %>"/>
		<c:set var="c_writer" value="<%=comment.getWriter() %>"/>
		<div class="list-group-item" id="comment<%=comment.getCid() %>">
    		<form method="post" action="delete_comment.do" id="comment<%=comment.getCid() %>_delete">
    			<div class="input-group <%=comment.getCid()%>">
					<div class="input-group-prepend">
						<span class="input-group-text"><a href="getmember.do?member=<c:out value="${c_writer}"/>"><c:out value="${c_writer}"/></a></span>
					</div>
  					<textarea class="form-control" name="content" readonly aria-label="With textarea"><c:out value="${c_content}"/></textarea>
				</div>
    			<input type="hidden" value=<c:out value="${board_name}"/> name="board_name">
    			<input type="hidden" value=<%=post.getPid() %> name="pid">
    			<input type="hidden" value="<c:out value="${c_writer}"/>" name="writer">
    			<input type="hidden" value=<%=comment.getCid() %> name="cid">
    			<input type="hidden" value="<%=csrf_token %>" name="csrf_token">
    		<%if(!comment.isBlind() && (session.getAttribute("id").equals(comment.getWriter())||per == manage||per==6||(manage==7&&permission>=4))){ %>
    			<input type="hidden" name="csrf_token" value="<%=csrf_token %>">
    			<button id="delete<%=comment.getCid() %>" class="btn btn-danger btn-sm" type="submit">삭제</button>
    		<%} %>
    		<%if(session.getAttribute("id").equals(comment.getWriter())&&!comment.isBlind()){ %>
    		    <button type="button" class="btn btn-warning btn-sm" onclick="editbutton('comment<%=comment.getCid()%>')">수정</button>
    		<%} %>
    		<%if(permission>=comment_per &&!comment.isBlind()){ %>
				<button type="button" class="btn btn-info btn-sm ccomment-btn" onclick ="ccommentbutton('write_ccomment_form_<%=comment.getCid()%>');">
					대댓글달기
				</button>
			<%} %>
    		</form>
        	<form class="d-none" action="write_comment.do" method="post" id="write_ccomment_form_<%=comment.getCid()%>">
        		<div class="input-group">
					<input type="hidden" value=<%=post.getPid() %> name="pid">
					<input type="hidden" value=<%=comment.getCid() %> name="parent">
					<input type="hidden" value=<c:out value="${board_name}"/> name="board_name">
					<div class="input-group-prepend">
	    				<span class="input-group-text" id="addon-wrapping<%=comment.getCid() %>">댓글</span>
	  				</div>
	  				<textarea class="form-control" id="comment<%=comment.getCid() %>_input" placeholder="댓글작성" aria-label="content" aria-describedby="addon-wrapping<%=comment.getCid() %>" name="content" required></textarea>
	  				<input type="hidden" name="csrf_token" value="<%=csrf_token %>">
  				</div>
  				<div class="input-group">
  					<div class="input-group-prepend">
	    				<span class="input-group-text" id="tag_<%=comment.getCid() %>">태그</span>
	  				</div>
  					<input type="text" class="form-control" aria-label="태그" placeholder="알림을 보낼 회원의 id를 ','를 구분자로 입력하세요." name="tags" id="comment_tags_<%=comment.getCid()%>"/>
  				</div>
  				<ul class="list-group" id="comment_tag_list_<%=comment.getCid()%>">
					<li class="list-group-item"><input class="form-control" type="text" placeholder="태그할 회원을 이름이나 아이디로 검색하세요." id="comment_tag_list_input_<%=comment.getCid()%>" onchange="sendMessage('<%=id%> csrf:<%=csrf_token_s%>',this,'comment_tag_list_<%=comment.getCid()%>');"></li>
				</ul>
  				<button type="button" class="btn btn-sm btn-primary" onclick="sendform('write_ccomment_form_<%=comment.getCid()%>')">작성</button>
				<button type="button" class="btn btn-sm btn-warning" onclick="cancle_ccomment('write_ccomment_form_<%=comment.getCid()%>')">취소</button>
			</form>
	        <form class="d-none" action="edit_comment.do" method="post" id="comment<%=comment.getCid() %>_edit">
				<input type="hidden" value=<%=post.getPid() %> name="pid">
				<input type="hidden" value=<%=comment.getCid() %> name="cid">
				<input type="hidden" value=<c:out value="${board_name}"/> name="board_name">
				<div class="input-group <%=comment.getCid()%>">
					<div class="input-group-prepend">
						<span class="input-group-text"><a href="getmember.do?member=<c:out value="${c_writer}"/>"><c:out value="${c_writer}"/></a></span>
					</div>
  					<textarea class="form-control" name="content" aria-label="With textarea"><c:out value="${c_content}"/></textarea>
				</div>
		  		<input type="hidden" value="<%=csrf_token %>" name="csrf_token">
		  		<button type="submit" class="btn btn-sm btn-primary">작성</button>
		  		<button type="button" class="btn btn-sm btn-warning" onclick="cancle_edit('comment<%=comment.getCid() %>')">취소</button>
			</form>	
    		<div class="list-group">
    		<%for(int j=1;j<commentlist.size();j++){ 
    			comment = commentlist.get(j);
    		%>
    			<div class="list-group-item ccomment" id="comment<%=comment.getCid() %>">
    				<c:set var="c_content" value="<%=comment.getContent() %>"/>
					<c:set var="c_writer" value="<%=comment.getWriter() %>"/>
		    		<form method="post" action="delete_comment.do" id="comment<%=comment.getCid() %>_delete">
		    			<div class="input-group <%=comment.getCid()%>">
							<div class="input-group-prepend">
								<span class="input-group-text"><a href="getmember.do?member=<c:out value="${c_writer}"/>"><c:out value="${c_writer}"/></a></span>
							</div>
		  					<textarea class="form-control" name="content" readonly aria-label="With textarea"><c:out value="${c_content}"/></textarea>
						</div>
		    			<input type="hidden" value=<c:out value="${board_name}"/> name="board_name">
		    			<input type="hidden" value=<%=post.getPid() %> name="pid">
		    			<input type="hidden" value="<c:out value="${c_writer}"/>" name="writer">
		    			<input type="hidden" value=<%=comment.getCid() %> name="cid">
		    			<input type="hidden" value="<%=csrf_token %>" name="csrf_token">
		    		<%if(!comment.isBlind() && (session.getAttribute("id").equals(comment.getWriter())||per == manage||per==6||(manage==7&&permission>=4))){ %>
		    			<input type="hidden" name="csrf_token" value="<%=csrf_token %>">
		    			<button id="delete<%=comment.getCid() %>" class="btn btn-danger btn-sm" type="submit">삭제</button>
		    		<%} %>
		    		<%if(session.getAttribute("id").equals(comment.getWriter())&&!comment.isBlind()){ %>
		    		    <button type="button" class="btn btn-warning btn-sm" onclick="editbutton('comment<%=comment.getCid()%>')">수정</button>
		    		<%} %>
		    		</form>
			        <form class="d-none" action="edit_comment.do" method="post" id="comment<%=comment.getCid() %>_edit">
						<input type="hidden" value=<%=post.getPid() %> name="pid">
						<input type="hidden" value=<%=comment.getCid() %> name="cid">
						<input type="hidden" value=<c:out value="${board_name}"/> name="board_name">
						<div class="input-group <%=comment.getCid()%>">
							<div class="input-group-prepend">
								<span class="input-group-text"><a href="getmember.do?member=<c:out value="${c_writer}"/>"><c:out value="${c_writer}"/></a></span>
							</div>
		  					<textarea class="form-control" name="content" aria-label="With textarea"><c:out value="${c_content}"/></textarea>
						</div>
				  		<input type="hidden" value="<%=csrf_token %>" name="csrf_token">
				  		<button type="submit" class="btn btn-sm btn-primary">작성</button>
				  		<button type="button" class="btn btn-sm btn-warning" onclick="cancle_edit('comment<%=comment.getCid() %>')">취소</button>
					</form>
    			</div>
    		<%} %>
    		</div>
    	</div>
		<%} %>
	<%} %>
	</div>
</div>
<ul id ="backup" hidden>
<li class="list-group-item"><input class="form-control" placeholder="태그할 회원을 이름이나 아이디로 검색하세요." type="text" id="root_comment_tag_list_inputhidden" onchange="sendMessage('<%=id%> csrf:<%=csrf_token_s%>',this,'comment_tag_list_id');" hidden></li>
</ul>
<script>
setmain_from_inmain();
function editbutton(id){
	var delete_form = $("#"+id+"_delete");
	var edit_form = $("#"+id+"_edit");
	delete_form.hide();
	edit_form.removeClass("d-none");
}
function cancle_edit(id){
	var delete_form = $("#"+id+"_delete");
	var edit_form = $("#"+id+"_edit");
	delete_form.show();
	edit_form.addClass("d-none");
}
function ccommentbutton(id){
	var ccomment_form = $("#"+id);
	ccomment_form.removeClass("d-none");
	$(".ccomment-btn").hide();
}
function cancle_ccomment(id){
	var ccomment_form = $("#"+id);
	ccomment_form.addClass("d-none");
	$(".ccomment-btn").show();
}
function sendform(id){
	var form = document.getElementById(id);
	form.submit();
}
function tags(id){
	var input = $("."+id);
	input.keydown(function(){
		
	});
}
var para = document.location.href.split("?");
var webSocket = new WebSocket("wss://hust.net/taglist");
var list = $("#comment_tag_list_0");
var ccid = "comment_tag_list_0";
webSocket.onmessage = function(message) {
	var a = $("#backup").html().replace('comment_tag_list_id',ccid)+message.data;
	a = a.replace('hidden','');
	a = a.replace('hidden','');
	$("#"+ccid).html(a);
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
function sendMessage(msg,input,id){
    // Wait until the state of the socket is not ready and send the message when it is...
    ccid = id;
    list = $("#"+id);
    waitForSocketConnection(webSocket, function(){
        console.log("message sent!!!");
        webSocket.send(msg+" "+input.value+" "+para);
    });
}

function editTag(msg){
	
	$("#"+ccid.replace("_list","s")).val($("#"+ccid.replace("_list","s")).val()+","+msg);
	$("li[name=tag_searched]").remove();
}
function sendform(id){
	var form = document.getElementById(id);
	form.submit();
}
</script>