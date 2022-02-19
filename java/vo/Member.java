// 회원 정보를 저장하는 클래스
package vo;

import java.sql.Timestamp;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Member implements VO{
	
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
	private int permission;
	/*
	 0 : 게스트 : 가입한 유저. 사용가능 기능 = 회원탈퇴분
	 1 : 신입회원 : 관리자가 승인한 신규회원.
	 2 : 정회원 : 활동한지 1년이상된 회원
	 3 : OB회원 : 졸업후 OB가 된 회원(기준은 별도 오비규칙따라서). OB운영진이 승급권한 가짐.
	 4 : YB운영진 : YB운영진. 게스트의 신입회원으로의 승인, 신입회원의 정회원으로의 승급, 정회원의  YB운영진  임명가능. 기타 YB들이 쓰는 게시판 관리가능. 정회원, 신입회원의 강퇴가능.
	 5 : OB운영진 : OB운영진. OB회원관리, 정회원의 OB회원의로의 승급 권한 가짐. YB운영진과는 별도의 존재. 서로 별도의 권한을 가짐. 신입회원>정회원, 정회원>YB운영진의 변경은 불가.
	 6 : 관리자 : 사이트 총 관리자. 사이트 메인화면 편집, 기타 회원의 관리, 등등의 모든 권한을 가짐. 홈페이지 개발자의 권한. 
	 */
	private Permission permissionEnum;
	private String etc; // 기타 메모할사항
	private long prob_score; // 문제은행 점수
	private String solved_prob=""; //푼 문제 목록. ,를 구분자로 함.
	private Timestamp prob_score_time; // 최근 문제를 푼 시간. db에서 update 시마다 자동으로 갱신.
	private boolean mypost_comment_noti_allow; // 내 게시글에 달린 댓글 알림 허용 여부.
	private boolean mycomment_comment_noti_allow; // 내가 작성한 댓글(대댓글 포함)에 달린 대댓글 알림 허용여부.
	private boolean call_noti_allow; // 특정 게시글에서 댓글로 다른 회원이 나를 호출하는 것의 허용여부.
	
	public Member(String id, String pw, String name, int birthY, int admissionY, int joinY, String phone,
			String email, String scholastic, int school_year, String interest, String address, String address_now,
			int permission, String etc, int prob_score) {
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

	public Member() {}

	@Override
	public String toJson() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
