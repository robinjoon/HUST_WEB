// 댓글의 정보를 저장하는 클래스
package vo;

import java.sql.Timestamp;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class Comment implements Comparable<Comment>,VO{
	
	private long cid; // 댓글 번호
	private long parent; // 대댓글일 경우 원댓글 번호.
	private int pid; // 댓글이 달린 게시글 번호
	private String writer; // 작성자 id
	private String content; // 댓글 내용
	private Timestamp write_date; // 댓글작성 시간
	private boolean blind;
	
	@Override
    public int compareTo(Comment c) {
		int a = this.getWrite_date().compareTo(c.getWrite_date());
		if(a==0) {
			return 0;
		}else if(a>0) {
			return 1;
		}else {
			return -1;
		}
    }
	@Override
	public String toJson() {
		// TODO Auto-generated method stub
		return null;
	}
}
