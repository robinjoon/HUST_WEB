// 문제은행의 문제의 정보를 저장하는 클래스
package vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Problem {
	
	private int probid; // 문제 번호
	private String title; // 문제 제목
	private String category;
	private String content; // 문제 내용
	private String writer; // 문제 작성자
	private int point; // 문제 배점
	private String answer; // 문제 정답
	
	
	
	
}
