<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" import="java.util.*,vo.*,dao.*,service.*,tools.*,listener.*,auth.*"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
boolean is_login = false;
boolean is_admin = false;
String id = (String) session.getAttribute("id");
	Permission per = (Permission)session.getAttribute("permission");
	if(id!=null && per != null){
		is_login=true;
		if(Permission.permissionToInt(per)>=4){
			is_admin=true;
		}
	}
	LinkedList<Board> boardlist = new LinkedList<Board>();
	BoardDAO dao = BoardDAO.getInstance();
	if (is_login) {
		boardlist = dao.getBoardlist(Permission.permissionToInt(per));
	}
	ArrayList<Noti> notis = (ArrayList<Noti>)NotiService.getNotiList(id);
	int unread_noti_count =0;
	if(notis!=null){
		for(int i=0;i<notis.size();i++){
	if(!notis.get(i).isRead()){
		unread_noti_count++;
	}
		}
	}
	ArrayList<HttpSession> sessions = Sessions.getSessions();
  	String csrf_token_s = (String)session.getAttribute("csrf_token");
  	Auth auth = new Auth(id,per);
  	Integer permission;
  	try{
  		permission = Permission.permissionToInt(per);
  	}catch(Exception e){
  		permission = -1;
  	}

%>

<!--div class="jumbotron" style="text-align: center;">
	<div class="page-header">
		<h1>
			HUST <small>Hongik University Security Team</small>
		</h1>
	</div>
	<p>홍익대학교 유일의 정보보호보안동아리 HUST 공식홈페이지입니다.</p>
</div-->
<nav class="navbar navbar-expand-lg navbar-light bg-light">
  <a class="navbar-brand" href="index.jsp">
    <img src="images/brand_hust_net.png" alt="">
  </a>
  <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
    <span class="navbar-toggler-icon"></span>
  </button>

  <div class="collapse navbar-collapse" id="navbarSupportedContent">
    <ul class="navbar-nav mr-auto">
      <li class="nav-item">
        <a class="nav-link" href="schedules.do?what=all">일정</a>
      </li>
      <li class="nav-item">
        <a class="nav-link" href="memberlist.do?group=재학&permission=7">회원목록</a>
      </li>
      <li class="nav-item">
        <a class="nav-link" href="problist.do">문제은행</a>
      </li>
      <%if(is_login){ %>
      <li class="nav-item dropdown">
        <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
          게시판
        </a>
        <div class="dropdown-menu" aria-labelledby="navbarDropdown">
          <%for(int i=0;i<boardlist.size();i++){ 
          	if(!boardlist.get(i).getBoard_name().contains("2006_")){%>
          <c:set var="board_name" value="<%=boardlist.get(i).getBoard_name()%>"/>
          <a class="dropdown-item" href="postsview.do?board_name=<c:out value="${board_name}"/>&page=1"><c:out value="${board_name}"/></a>
          <%}
          	} %>
        </div>
      </li>
      <li class="nav-item dropdown">
        <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
          과거 자료게시판
        </a>
        <div class="dropdown-menu" aria-labelledby="navbarDropdown">
          <%for(int i=0;i<boardlist.size();i++){ 
          	if(boardlist.get(i).getBoard_name().contains("2006_")){%>
          <c:set var="board_name" value="<%=boardlist.get(i).getBoard_name()%>"/>
          <a class="dropdown-item" href="postsview.do?board_name=<c:out value="${board_name}"/>&page=1"><c:out value="${board_name}"/></a>
          <%}
          	} %>
        </div>
      </li>
      <%} %>
      <%if(!is_login){ %>
      <li class="nav-item">
        <a class="nav-link" href="login.jsp">로그인</a>
      </li>
      <li class="nav-item">
        <a class="nav-link" href="join.jsp">회원가입</a>
      </li>
      <%}else{ %>
      <li class="nav-item">
        <c:set var="id" value="<%=id%>"/>
        <a class="nav-link" href="mypage.do"><c:out value="${id}"/></a>
      </li>
      <li class="nav-item">
        <a class="nav-link" href="notilist.do"><span class="badge badge-pill badge-primary">알림 <%=unread_noti_count %></span></a>
      </li>
      <li class="nav-item">
        <a class="nav-link" href="logout.jsp?csrf_token=<%=csrf_token_s%>">로그아웃</a>
      </li>
      <%} %>
      <%if(is_admin){ %>
      <li class="nav-item">
        <a class="nav-link" href="admin.do">관리자 페이지</a>
      </li>
      <%} %>
    </ul>
    <form class="form-inline my-2 my-lg-0" action="search.do" method="get">
    	<input class="form-control mr-sm-2" type="search" placeholder="검색" aria-label="Search" name="search_word" required>
      	<select class="form-control mr-sm-2" name="search_target" id="search_target">
      		<option value="title">제목</option>
      		<option value="content">본문</option>
			<option value="writer">작성자</option>
			<option value="origin_file_name">첨부파일이름</option>
		</select>
		<select class="form-control mr-sm-2" name="sort" id="sort">
      		<option value="write_date">최신순</option>
      		<option value="views">조회수 순</option>
		</select>
		<button class="btn btn-outline-success my-2 my-sm-0" type="submit">사이트 내 검색</button>
    </form>
  </div>
</nav>
<div class="container main border rounded">

</div>
