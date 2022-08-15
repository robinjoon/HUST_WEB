// 일정 정보를 저장하는 클래스

package vo;

import java.sql.Date;
import java.time.*;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class Schedule implements VO{
	private int sid; // 일정번호
	private Date s_date; // 일정 날짜
	private String title; // 일정 제목
	private String place; // 일정 장소
	private String content; //일정 내용(상세시간 등)
	private boolean is_ob; // ob 전용인지 여부
	
	public Schedule() {
		s_date = Date.valueOf(LocalDate.now());
		title="일정 없음";
		place = title;
		content = title;
	}
	
	@Override
	public String toJson() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isInvalid() {
		// TODO Auto-generated method stub
		return false;
	}
	
}
