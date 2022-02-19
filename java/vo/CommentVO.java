// 댓글의 정보를 저장하는 클래스
package vo;

import java.sql.Timestamp;

public class CommentVO implements Comparable<CommentVO>,VO{
	
	private long cid; // 댓글 번호
	private long parent; // 대댓글일 경우 원댓글 번호.
	private int pid; // 댓글이 달린 게시글 번호
	private String writer; // 작성자 id
	private String content; // 댓글 내용
	private Timestamp write_date; // 댓글작성 시간
	private boolean blind;
	
	public long getCid() {
		return cid;
	}
	public void setCid(long cid) {
		this.cid = cid;
	}
	public long getParent() {
		return parent;
	}
	public void setParent(long parent) {
		this.parent = parent;
	}
	public int getPid() {
		return pid;
	}
	public void setPid(int pid) {
		this.pid = pid;
	}
	public String getWriter() {
		return writer;
	}
	public void setWriter(String writer) {
		this.writer = writer;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Timestamp getWrite_date() {
		return write_date;
	}
	public void setWrite_date(Timestamp write_date) {
		this.write_date = write_date;
	}
	public boolean isBlind() {
		return blind;
	}
	public void setBlind(boolean blind) {
		this.blind = blind;
	}
	@Override
    public int compareTo(CommentVO c) {
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
