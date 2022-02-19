// 일정 정보를 저장하는 클래스

package vo;

import java.sql.Date;
import java.time.*;

public class ScheduleVO implements VO{
	private int sid; // 일정번호
	private Date s_date; // 일정 날짜
	private String title; // 일정 제목
	private String place; // 일정 장소
	private String content; //일정 내용(상세시간 등)
	private boolean is_ob; // ob 전용인지 여부
	
	public ScheduleVO() {
		s_date = Date.valueOf(LocalDate.now());
		title="일정 없음";
		place = title;
		content = title;
	}
	
	public int getSid() {
		return sid;
	}

	public void setSid(int sid) {
		this.sid = sid;
	}

	public Date getS_date() {
		return s_date;
	}
	public void setS_date(Date s_date) {
		this.s_date = s_date;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getPlace() {
		return place;
	}
	public void setPlace(String place) {
		this.place = place;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public boolean isIs_ob() {
		return is_ob;
	}
	public void setIs_ob(boolean is_ob) {
		this.is_ob = is_ob;
	}
	@Override
	public String toJson() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
