<%@page import="java.sql.Date"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" import="java.util.*,vo.*,dao.*,java.time.*"%>
	    <%@ include file="WEB-INF/semipage/header.jsp" %>
	    <%@ include file="WEB-INF/semipage/nav.jsp" %>

	<%
	ArrayList<Post> notice = PostDAO.getinstance().getNoticeBoard();
			ArrayList<Post> free = PostDAO.getinstance().getFreeBoard();
	%>
	<div class="inmain d-none">
	<br>
	<%
	if(!is_login || permission==0){
	%>
		<%
		if(permission==0){
		%>
		<h3>
			<font color="#ff0000">회원가입은 완료되었으나 운영자의 승인이 필요합니다. 운영자에게 아이디를 알려주세요.</font>
		</h3>
		<%
		}
		%>
		<h4>
			<b>H.U.S.T의 의미는?</b>
		</h4>
		<blockquote>
			<p>
				<b>H.U.S.T</b>는 Hongik University Security Team의 약자입니다.
			</p>
			<p>우리는 보안을 생각하며 보안을 위한 해킹을 지향하고 있습니다.</p>
			<p>인터넷 안에서의 자유와 평등 그리고 젊음. 저희는 이런 것들을 지키고 싶습니다.</p>
			<p>
				따라서 Security와 Hacking에 대한 열정을 가지고 있다면 누구나 <b>H.U.S.T</b>의 가족이 될 수
				있습니다.
			</p>
			<p>
				<b>H.U.S.T</b>는 활동에 제약이 없고 보안과 해킹에 관심있는 사람이면 누구나 참여할 수 있으며, 서로가 관심
				있는 분야에 대해자유롭게 토론하며 배울 수 있는 것이 장점이라고 할 수 있습니다.
			</p>
			<br />
			<p>인터넷 안에서의 자유와 평등 그리고 젊음.</p>
			<p>저희는 이런 것들을 지키고 싶습니다.</p>
		</blockquote>
		<h4>
			<b>H.U.S.T의 설립취지</b>
		</h4>
		<blockquote>
			<p>
				<b>H.U.S.T</b>는 2001년 2월 3일 제1대 <b>H.U.S.T</b> 회장인 97학번
				고영준(F-Luid)학우가 만든 홍익해커(http://cafe.daum.net/hongikhacker) 카페를 통해 온라인
				상에서 태어났습니다.
			</p>
			<p>홍익대학교에도 상당히 실력 있고 컴퓨터보안을 정말로 깊게 생각하는 사람들이 많음에도 불구하고 모일 수 있는
				장소나 구심점이 없는 것이 현실이었습니다.</p>
			<p>회원들 모두가 요즘과 같이 인터넷이 일반화된 시대에 신뢰성 없는 Web상에서 중요한 사업과 개인정보를 유출하는
				행위는 매우 위험한 일이라고 생각을 공유하고, 특히 IT강국인 동시에 보안에 가장 취약한 우리나라에서는 실력있고 윤리의식이
				강한 해커나 보안전문가를 양성하는 것이 중요한 일이라고 생각하기에 보안에 관심이 있는 학우들이 모일 수 있는 공간을 가짐이
				취지였습니다.</p>
		</blockquote>
		<h4>
			<b>H.U.S.T 회원들의 활동</b>
		</h4>
		<blockquote>
			<p>
				<b>H.U.S.T</b>는 2001년 2월 결성된 홍익해커(http://cafe.daum.net/hongikhacker)
				부터 당해 3월 <b>H.U.S.T</b>라는 이름으로 홍익대학교 정보통신공학과 학회에서 2008년 5월 홍익대학교
				정동아리로 승격 되어 오는 동안 <b>H.U.S.T</b>에 소속된 홍익대학교 재학생과 졸업생 그리고 뜻을 같이 하는
				소중한 분들로 구성 되어 정보보안에 대한 순수한 열정으로 함께 하고 있습니다.
			</p>
			<p>
				<b>H.U.S.T</b>는 매주 화요일과 목요일 두 번씩 교내 전산실에 모여 정보보안 관련 스터디와 세미나를 하고
				있습니다.
			<p>
				<b>H.U.S.T</b>에서 진행하는 보안관련 스터디와 세미나는 허스트 회원이 스스로 공부한 내용을 서로 공유 하고 매년
				선발된 신입회원들의 교육을 통해 대한민국의 정보보안 전문가 인력 양성을 하고 있습니다.
			</p>
			<p>
				<b>H.U.S.T</b>는 매년 1회 모든 회원이 모여 전체 세미나를 진행 하며 이를 통해 모든 회원의 화합과 정보
				공유를하고 있습니다.
			</p>
			<p>
				<b>H.U.S.T</b>는 회원을 통해 발견 되는 각종 취약점에 대한 정보를 KISA(한국정보보호진흥원), krCERT등
				유관기관과 해당 관계자의 협조하에 합법적인 테두리 안에서 정보보안을 목적으로 적극적으로 알리고 있습니다.
			</p>
			<p>
				<b>H.U.S.T</b>는 각종 보안관련 모임, KUCIS(한국 정보보안 동아리 연합회)에 소속된 타 40개 대학
				보안동아리와 연계하여 활발한 할동을하고 있습니다.
			</p>
			<p>
				<b>H.U.S.T</b>는 2001년의 <b>1st H.U.S.T Hacking Festival</b>을 시작으로 매년
				독립적으로 Hacking Festival 을 개최하고 있습니다.
			</p>
			<p>
				<b>H.U.S.T</b> Hacking Festival은 국내 보안 관련 대회 중 가장 오랫동안 지속되어온 대회라는
				자부심을 가지고 있습니다.
			</p>
			<h5>
				<b>지금 까지의 활동 및 실적</b>
			</h5>
			<h6>1. 해킹대회 활동 현황</h6>
			<ul>
				<li>2001년 - 제1회 H.U.S.T Hacking Festival 개최</li>
				<li>2003년 - 제2회 H.U.S.T World Hacking Festival 개최</li>
				<li>2004년 - 제3회 H.U.S.T Harmony Hacking Festival 개최</li>
				<li>2005년 - 제4회 Real H.U.S.T Penetration Test Festival 개최</li>
				<li>2006년 - 제5회 Real H.U.S.T Penetration Test Ⅱ Festival 개최</li>
				<li>2007년 - 제6회 H.U.S.T Hacking Festival(The Change) 개최</li>
				<li>2008년 - 제7회 H.U.S.T Hacking Festival(The Fair) 개최</li>
				<li>2009년 - 제8회 H.U.S.T Hacking Festival(Creative &amp; Fun) 개최</li>
				<li>2010년 - 제9회 H.U.S.T Hacking Festival(Absolute) 개최</li>
				<li>2011년 - 제10회 H.U.S.T Hacking Festival(Classic) 개최</li>
				<li>2012년 - 제11회 H.U.S.T Hacking Festival(Return) 개최</li>
				<li>2013년 - 제12회 H.U.S.T Hacking Festival(Real) 개최</li>
				<li>2014년 - 제13회 H.U.S.T Hacking Festival(Reboot) 개최</li>
				<li>2015년 - 제14회 H.U.S.T Hacking Festival(Game) 개최</li>
				<li>2017년 - 제15회 H.U.S.T Hacking Festival(Resurrection) 개최</li>
			</ul>
			<h6>2. 세미나 개최</h6>
			<ul>
				<li>블루투스 보안</li>
				<li>CGI &amp; WEB Security</li>
				<li>Wireless Lan &amp; IMT2000</li>
				<li>스니핑의 공격과 대응방향</li>
				<li>PGP 기술개념</li>
				<li>Shareware Crack</li>
				<li>해커의 윤리의식과 네티켓</li>
				<li>최근 해킹기법 동향파악과 시연</li>
				<li>BOF(BufferOverFlow) 개념과 원리</li>
				<li>DDOS 공격의 방법과 대응책</li>
				<li>공격 유형에 따른 보안 대책</li>
				<li>Penetration Analysis^KOF Internet-Based Computer Network</li>
				<li>무선네트워크 취약점과 보안</li>
				<li>키보드 보안</li>
				<li>어셈 블리어</li>
				<li>Active X</li>
				<li>Bug Finding Techniques</li>
				<li>Virus</li>
				<li>IDS</li>
				<li>Spoofing</li>
				<li>SQL Injection</li>
				<li>VOIP</li>
				<li>XSS</li>
				<li>CSRF</li>
				<li>Search Engine</li>
				<li>Network Security</li>
				<li>OSPF</li>
				<li>SSH</li>
				<li>Security of layer</li>
				<li>IA-32</li>
				<li>Revercing Engineering</li>
				<li>DLL injection</li>
				<li>Reverse Connection</li>
				<li>Regedit</li>
				<li>Shell code</li>
				<li>OWASP 10대취약점 및 대응 방안</li>
				<li>온라인게임 최신 해킹기법 및 대응 방안</li>
			</ul>
			<h6>3. 수상 내역</h6>
			<ul>
				<li>2009-05-13 KUCIS Open CTF 2위 수상</li>
				<li>2009-03-08 2009년 코드게이트 참가</li>
				<li>2008-08-07 KISA 제3회 S/W 보안 취약점 찾기대회 장려상 수상</li>
				<li>2008-04-15 Codegate 해킹방어대회 3위 수상</li>
				<li>2007-06-25 제4회 해킹 방어 대회 대상 정보통신부장관상 수상</li>
				<li>2007-02-27 아르고스 해킹페스티벌 동상 수상</li>
				<li>2006-06-30 전국인터넷모의해킹대회 대상 수상</li>
				<li>2006-06-19 제3회 해킹 방어대회 금상 수상</li>
				<li>2002-12-13 대덕 해킹 페스티발 대상 수상</li>
			</ul>
			<h6>4. 기타 현황</h6>
			<ul>
				<li>한국정보보호진흥원(KISA)지정 2002대학 정보보호 보안 동아리</li>
				<li>한국정보보호진흥원(KISA)지정 2008대학 정보보호 보안 동아리</li>
				<li>한국정보보호진흥원(KISA)지정 2009대학 정보보호 보안 동아리</li>
				<li>한국정보보호진흥원(KISA)지정 2011대학 정보보호 보안 동아리</li>
				<li>한국정보보호진흥원(KISA)지정 2012대학 정보보호 보안 동아리</li>
				<li>한국정보보호진흥원(KISA)지정 2013대학 정보보호 보안 동아리</li>
				<li>한국정보보호진흥원(KISA)지정 2015대학 정보보호 보안 동아리</li>
				<li>한국정보보호진흥원(KISA)지정 2016대학 정보보호 보안 동아리</li>
				<li>한국정보보호진흥원(KISA)지정 2017대학 정보보호 보안 동아리</li>
			</ul>
		</blockquote>
		<h4>
			<b>The Leader of H.U.S.T</b>
		</h4>
		<blockquote>
			<p>1대 : F-Luid(고영준)</p>
			<p>2대 : Mr.g(김형구)</p>
			<p>3대 : goldnboy(문병일)</p>
			<p>4대 : coybum(이범석)</p>
			<p>5대 : nohpro(노갑병)</p>
			<p>6대 : niky31(신삼일)</p>
			<p>7대 : blackscutum(민병우)</p>
			<p>8대 : kerz(윤홍상)</p>
			<p>9대 : Atom(안병학)</p>
			<p>10대 : clarus(김범석)</p>
			<p>11대 : leebaby(이정형)</p>
			<p>12대 : loossy(신상윤)</p>
			<p>13대 : KyuF3(연규철)</p>
			<p>14대 : DKe2(김덕현)</p>
			<p>15대 : Ar(박민혁)</p>
			<p>16대 : UNknown(김수원)</p>
			<p>17대 : ManPD(김만수)</p>
			<p>18대 : Armada(김형철)</p>
			<p>19대 : Morrigan(이수영)</p>
			<p>20대 : Cloud김범윤)</p>
			<p>21대 : Bugday(이충일)</p>
			<p>22대 : runa(홍영우)</p>
			<p>23대 : kill(길경서)</p>
		</blockquote>
		<%
		}else{
		%>
		<div class="container-fluid">
			<table class="table table-striped table-responsive-sm intable">
				<caption>공지게시판 최신글</caption>
				<thead class="thead-light">
					<tr>
						<th>글번호</th>
						<th>글제목</th>
						<th>작성자</th>
						<th>작성일</th>
					</tr>
					<%
					for(int i=0;i<notice.size();i++){
									Post post = notice.get(i);
					%>
								<c:set var="title" value="<%=post.getTitle()%>"/>
								<c:set var="writer" value="<%=post.getWriter()%>"/>
					<%
					if(post.isIs_notice()){
					%>
						<tr class="bg-primary notice">
							<td><%=post.getPid()%></td>
							<td><a class="notice" href="postview.do?pid=<%=post.getPid()%>&board_name=공지게시판" class="intable"><c:out value="${title}"/></a></td>
							<td><a class="notice" href="getmember.do?member=<c:out value="${writer}"/>"><c:out value="${writer}"/></a></td>
							<td><%=post.getWrite_date().toString().substring(0, 10)%></td>
						</tr>
					<%
					}else{
					%>
						<tr>
							<td><%=post.getPid()%></td>
							<td><a href="postview.do?pid=<%=post.getPid()%>&board_name=공지게시판" class="intable"><c:out value="${title}"/></a></td>
							<td><a href="getmember.do?member=<c:out value="${writer}"/>"><c:out value="${writer}"/></a></td>
							<td><%=post.getWrite_date().toString().substring(0, 10)%></td>
						</tr>
					 <%
					 }
					 %>
					<%
					}
					%>
				</thead>
			</table>
			<table class="table table-striped table-responsive-sm intable">
				<caption>자유게시판 최신글</caption>
				<thead class="thead-light">
					<tr>
						<th>글번호</th>
						<th>글제목</th>
						<th>작성자</th>
						<th>작성일</th>
					</tr>
					<%
					for(int i=0;i<free.size();i++){
									Post post = free.get(i);
					%>
						<c:set var="title" value="<%=post.getTitle() %>"/>
						<c:set var="writer" value="<%=post.getWriter() %>"/>
					<%if(post.isIs_notice()){ %>
						<tr class="bg-primary notice">
							<td><%=post.getPid() %></td>
							<td><a class="notice" href="postview.do?pid=<%=post.getPid()%>&board_name=자유게시판" class="intable"><c:out value="${title}"/></a></td>
							<td><a class="notice" href="getmember.do?member=<c:out value="${writer}"/>"><c:out value="${writer}"/></a></td>
							<td><%=post.getWrite_date().toString().substring(0, 10) %></td>
						</tr>
					<%}else{ %>
						<tr>
							<td><%=post.getPid() %></td>
							<td><a href="postview.do?pid=<%=post.getPid()%>&board_name=자유게시판" class="intable"><c:out value="${title}"/></a></td>
							<td><a href="getmember.do?member=<c:out value="${writer}"/>"><c:out value="${writer}"/></a></td>
							<td><%=post.getWrite_date().toString().substring(0, 10) %></td>
						</tr>
					<% }%>
					<%} %>
				</thead>
			</table>
			</div>
			<!-- 간단일정 -->
			<div class="row">
			<%
			LocalDate currentDate = LocalDate.now();
					java.sql.Date date = java.sql.Date.valueOf(currentDate);
					Schedule sc = ScheduleDAO.getInstance().getSchedule(date);
					Schedule next = ScheduleDAO.getInstance().nextSchedule(date);
					Schedule pre = ScheduleDAO.getInstance().preSchedule(date);
			%>
				<c:set var="title" value="<%=pre.getTitle() %>"/>
				<c:set var="place" value="<%=pre.getPlace() %>"/>
				<c:set var="content" value="<%=pre.getContent() %>"/>
				<div class="card col-sm">
  					<div class="card-body">
    					<h5 class="card-title">지난 일정 : <c:out value="${title}"/></h5>
    					<h6 class="card-subtitle mb-2 text-muted">날짜 : <%=pre.getS_date() %></h6>
    					<h6 class="card-subtitle mb-2 text-muted">장소 : <c:out value="${place}"/></h6>
    					<p class="card-text">내용  : <c:out value="${content}"/></p>
  					</div>
				</div>
				<c:set var="title" value="<%=sc.getTitle() %>"/>
				<c:set var="place" value="<%=sc.getPlace() %>"/>
				<c:set var="content" value="<%=sc.getContent() %>"/>
				<div class="card col-sm">
  					<div class="card-body">
    					<h5 class="card-title">오늘의 일정 : <c:out value="${title}"/></h5>
    					<h6 class="card-subtitle mb-2 text-muted">날짜 : <%=sc.getS_date() %></h6>
    					<h6 class="card-subtitle mb-2 text-muted">장소 : <c:out value="${place}"/></h6>
    					<p class="card-text">내용  : <c:out value="${content}"/></p>
  					</div>
				</div>
				<c:set var="title" value="<%=next.getTitle() %>"/>
				<c:set var="place" value="<%=next.getPlace() %>"/>
				<c:set var="content" value="<%=next.getContent() %>"/>
				<div class="card col-sm">
  					<div class="card-body">
    					<h5 class="card-title">다가오는 일정 : <c:out value="${title}"/></h5>
    					<h6 class="card-subtitle mb-2 text-muted">날짜 : <%=next.getS_date() %></h6>
    					<h6 class="card-subtitle mb-2 text-muted">장소 : <c:out value="${place}"/></h6>
    					<p class="card-text">내용  : <c:out value="${content}"/></p>
  					</div>
				</div>
			</div>
		</div>
		<%} %>
		<br>
	</div>
	<script>
		setmain_from_inmain();
	</script>
</body>
</html>