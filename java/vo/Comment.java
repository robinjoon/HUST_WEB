// 댓글의 정보를 저장하는 클래스
package vo;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import exceptions.CreateCommentException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import service.BoardService;
import tools.Tag;
@Getter
@Setter(value = AccessLevel.PRIVATE)
public class Comment implements Comparable<Comment>,VO{
	
	public static Comment INVALID_COMMENT = new Comment();
	
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
	
	public Comment(ResultSet rs) {
		try {
			this.setPid(rs.getInt("pid"));
			this.setCid(rs.getLong("cid"));
			this.setParent(rs.getLong("parent"));
			this.setWriter(rs.getString("writer"));
			this.setContent(rs.getString("content"));
			this.setWrite_date(rs.getTimestamp("write_date"));
			this.setBlind(rs.getBoolean("blind"));
		}catch(SQLException e) {
			throw new CreateCommentException("sql 에러");
		}
	}
	
	public Comment(HttpServletRequest request) {
		
		int pid = Integer.parseInt(request.getParameter("pid"));
		long parent = -1;
		try {
			parent = Long.parseLong(request.getParameter("parent"));
		}catch(NumberFormatException e) {
			parent = -1;
		}
		String content = request.getParameter("content");
		HttpSession session = request.getSession();
		String writer = (String)session.getAttribute("id");
		Comment comment = new Comment();
		comment.setPid(pid);
		comment.setContent(content);
		comment.setParent(parent);
		comment.setWrite_date(Timestamp.valueOf(LocalDateTime.now()));
		comment.setWriter(writer);
		comment.setBlind(false);
		try {
			long cid = Long.parseLong(request.getParameter("cid"));
			comment.setCid(cid);
		}catch(NullPointerException e) {
			
		}
	}
	
	private Comment() {
		this.cid =-1;
		this.blind=false;
		this.content = "";
		this.parent=-1;
		this.pid =-1;
		this.write_date = Timestamp.valueOf(LocalDateTime.MIN);
		this.writer="";
	}
	
	@Override
	public String toJson() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public boolean isInvalid() {
		if(cid==-1) {
			return true;
		}else if(content.equals("")) {
			return true;
		}else if(parent == -1) {
			return true;
		}else if(pid == -1) {
			return true;
		}else if(write_date.equals(Timestamp.valueOf(LocalDateTime.MIN))) {
			return true;
		}else if(writer.equals("")) {
			return true;
		}else {
			return false;
		}
	}
}
