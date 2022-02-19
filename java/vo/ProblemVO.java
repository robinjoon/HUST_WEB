// 문제은행의 문제의 정보를 저장하는 클래스
package vo;

public class ProblemVO {
	
	private int probid; // 문제 번호
	private String title; // 문제 제목
	private String category;
	private String content; // 문제 내용
	private String writer; // 문제 작성자
	private int point; // 문제 배점
	private String answer; // 문제 정답
	
	public int getProbid() {
		return probid;
	}
	public void setProbid(int probid) {
		this.probid = probid;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getWriter() {
		return writer;
	}
	public void setWriter(String writer) {
		this.writer = writer;
	}
	public int getPoint() {
		return point;
	}
	public void setPoint(int point) {
		this.point = point;
	}
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	
	
}
