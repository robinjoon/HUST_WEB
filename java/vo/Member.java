// 회원 정보를 저장하는 클래스
package vo;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;

import exceptions.CreateMemberFaliException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter(value = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class Member implements VO{
	@NonNull
	private String id; // 회원 id
	private String pw; // 회원 비번(해싱된)
	private String name; // 회원 이름
	private int birthY; // 회원 생년
	private int admissionY; // 입학 년도
	private int joinY; // 회원가입 년도
	private String phone; // 전화번호
	private String email; // 이메일
	private String scholastic; // 현재 학적상태(신입 재학 졸업 휴학 군대 탈퇴 중 1)
	private int school_year; // 현재 학년
	private String interest; // 관심분야
	private String address; // 주소(본가)
	private String address_now; //주소(학기중)
	/*
	 0 : 게스트 : 가입한 유저. 사용가능 기능 = 회원탈퇴분
	 1 : 신입회원 : 관리자가 승인한 신규회원.
	 2 : 정회원 : 활동한지 1년이상된 회원
	 3 : OB회원 : 졸업후 OB가 된 회원(기준은 별도 오비규칙따라서). OB운영진이 승급권한 가짐.
	 4 : YB운영진 : YB운영진. 게스트의 신입회원으로의 승인, 신입회원의 정회원으로의 승급, 정회원의  YB운영진  임명가능. 기타 YB들이 쓰는 게시판 관리가능. 정회원, 신입회원의 강퇴가능.
	 5 : OB운영진 : OB운영진. OB회원관리, 정회원의 OB회원의로의 승급 권한 가짐. YB운영진과는 별도의 존재. 서로 별도의 권한을 가짐. 신입회원>정회원, 정회원>YB운영진의 변경은 불가.
	 6 : 관리자 : 사이트 총 관리자. 사이트 메인화면 편집, 기타 회원의 관리, 등등의 모든 권한을 가짐. 홈페이지 개발자의 권한. 
	 */
	private Permission permission;
	private String etc; // 기타 메모할사항
	private long prob_score; // 문제은행 점수
	private String solved_prob=""; //푼 문제 목록. ,를 구분자로 함.
	private Timestamp prob_score_time; // 최근 문제를 푼 시간. db에서 update 시마다 자동으로 갱신.
	private boolean mypost_comment_noti_allow; // 내 게시글에 달린 댓글 알림 허용 여부.
	private boolean mycomment_comment_noti_allow; // 내가 작성한 댓글(대댓글 포함)에 달린 대댓글 알림 허용여부.
	private boolean call_noti_allow; // 특정 게시글에서 댓글로 다른 회원이 나를 호출하는 것의 허용여부.
	
	public Member(HttpServletRequest request, Permission permission, boolean pwCheckEnable) throws Exception {
		String id,pw,pw_check,name,phone,email,scholastic,interest,address,address_now,etc;
		Integer birthY,admissionY,joinY,school_year;
		boolean mypost_comment_noti_allow = Boolean.parseBoolean(request.getParameter("mypost_comment_noti_allow"));
		boolean mycomment_comment_noti_allow = Boolean.parseBoolean(request.getParameter("mycomment_comment_noti_allow"));
		boolean call_noti_allow = Boolean.parseBoolean(request.getParameter("call_noti_allow"));
		
		id = request.getParameter("id");
		pw = request.getParameter("pw");
		pw_check = request.getParameter("pw_check");
		if(pwCheckEnable && !pw.equals(pw_check)) {
			throw new Exception();
		}
		name = request.getParameter("name"); 
		phone = request.getParameter("phone");
		email = request.getParameter("email"); scholastic = request.getParameter("scholastic");
		interest = request.getParameter("interest");
		address = request.getParameter("address");address_now = request.getParameter("address_now");
		etc = request.getParameter("etc");
		try {
			birthY = Integer.parseInt(request.getParameter("birthY"));
		}catch(NumberFormatException e) {
			birthY=0;
		}
		try {
			admissionY = Integer.parseInt(request.getParameter("admissionY"));
		}catch(NumberFormatException e) {
			admissionY=0;
		}
		try {
			joinY = Integer.parseInt(request.getParameter("joinY"));
		}catch(NumberFormatException e) {
			joinY=0;
		}
		try {
			school_year = Integer.parseInt(request.getParameter("school_year"));
		}catch(NumberFormatException e) {
			school_year=0;
		}
		this.id = id;
		this.pw = pw;
		this.name = name;
		this.birthY = birthY;
		this.admissionY = admissionY;
		this.joinY = joinY;
		this.phone = phone;
		this.email = email;
		this.scholastic = scholastic;
		this.school_year = school_year;
		this.interest = interest;
		this.address = address;
		this.address_now = address_now;
		this.etc = etc;
		this.mycomment_comment_noti_allow = mycomment_comment_noti_allow;
		this.mypost_comment_noti_allow = mypost_comment_noti_allow;
		this.call_noti_allow = call_noti_allow;
		this.permission = permission;
		this.prob_score =0;
	}
	
	public Member(String id, String pw, String name, int birthY, int admissionY, int joinY, String phone,
			String email, String scholastic, int school_year, String interest, String address, String address_now,
			Permission permission, String etc, int prob_score) {
		this.id = id;
		this.pw = pw;
		this.name = name;
		this.birthY = birthY;
		this.admissionY = admissionY;
		this.joinY = joinY;
		this.phone = phone;
		this.email = email;
		this.scholastic = scholastic;
		this.school_year = school_year;
		this.interest = interest;
		this.address = address;
		this.address_now = address_now;
		this.permission = permission;
		this.etc = etc;
		this.prob_score = prob_score;
	}

	public Member(ResultSet rs) {
		try {
			this.setId(rs.getString("id"));
			this.setName(rs.getString("name"));
			this.setBirthY(rs.getInt("birthY"));
			this.setAdmissionY(rs.getInt("admissionY"));
			this.setJoinY(rs.getInt("joinY"));
			this.setSchool_year(rs.getInt("school_year"));
			this.setPhone(rs.getString("phone"));
			this.setEmail(rs.getString("email"));
			this.setScholastic(rs.getString("scholastic"));
			this.setInterest(rs.getString("interest"));
			this.setAddress(rs.getString("address"));
			this.setAddress_now(rs.getString("address_now"));
			this.setPermission(Permission.intToPermission(rs.getInt("permission")));
			this.setEtc(rs.getString("etc"));
			this.setProb_score(rs.getLong("prob_score"));
			this.setSolved_prob(rs.getString("solved_prob"));
			this.setMypost_comment_noti_allow(rs.getBoolean("mypost_comment_noti_allow"));
			this.setMycomment_comment_noti_allow(rs.getBoolean("mycomment_comment_noti_allow"));
			this.setCall_noti_allow(rs.getBoolean("call_noti_allow"));
		}catch(SQLException e) {
			throw new CreateMemberFaliException("sql 에러");
		}
	}
	
	public Member() {
		this.id="invalid";
		this.permission = Permission.INVALID;
	}

	public Member(String id, String pw) {
		this.id = id;
		this.pw = pw;
	}
	
	@Override
	public String toJson() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isInvalid() {
		if(this.id.equals("invalid") && this.permission == Permission.INVALID) {
			return true;
		}else {
			return false;
		}
	}
	
}
