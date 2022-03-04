package vo;
import java.sql.Date;
import java.sql.ResultSet;

import exceptions.CreateNotiFailException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter(value = AccessLevel.PRIVATE)
public class Noti implements VO{

	private long nid;
	private String sender;
	private String receiver;
	private String title;
	private String body;
	private String url;
	private Date date;
	private boolean read;
	private boolean notice;
	
	public Noti() {
		this.nid = -1;
		this.sender = "invalid";
	}
	
	public static Noti getReplyCommentNoti(Comment comment, String receiver) {
		Noti noti = new Noti();
		noti.setSender(comment.getWriter());
		noti.setReceiver(receiver);
		noti.setTitle("내가 쓴 댓글에 새 대댓글이 등록되었습니다.");
		noti.setBody(comment.getWriter()+" : "+comment.getContent());
		noti.setUrl("postview.do?pid="+comment.getPid());
		noti.setDate(new java.sql.Date(new java.util.Date().getTime()));
		noti.setNotice(false);
		return noti;
	}
	
	public static Noti getNewCommentNoti(Comment comment, String receiver) {
		Noti noti = new Noti();
		noti.setSender(comment.getWriter());
		noti.setReceiver(receiver);
		noti.setTitle("내가 쓴 게시글에 새 댓글이 등록되었습니다.");
		noti.setBody(comment.getWriter()+" : "+comment.getContent());
		noti.setUrl("postview.do?pid="+comment.getPid());
		noti.setDate(new java.sql.Date(new java.util.Date().getTime()));
		noti.setNotice(false);
		return noti;
	}
	
	public static Noti getTagNoti(Comment comment,String receiver) {
		Noti noti = new Noti();
		noti.setSender(comment.getWriter());
		noti.setReceiver(receiver);
		noti.setTitle(comment.getWriter()+"님이 댓글에 태그하셨습니다.");
		noti.setBody(comment.getContent());
		noti.setUrl("postview.do?pid="+comment.getPid());
		noti.setDate(new java.sql.Date(new java.util.Date().getTime()));
		noti.setNotice(false);
		return noti;
	}
	
	public static Noti getNoticeNoti(Post post, String receiver) {
		Noti noti = new Noti();
		noti.setSender(post.getWriter());
		noti.setReceiver(receiver);
		noti.setTitle(post.getBoard_name()+"에 새 공지가 등록되었습니다.");
		noti.setBody(post.getContent());
		noti.setUrl("postview.do?pid="+post.getPid());
		noti.setDate(new java.sql.Date(new java.util.Date().getTime()));
		noti.setNotice(true);
		return noti;
	}
	
	public Noti(ResultSet rs) {
		try {
		this.setNid(rs.getLong("nid"));
		this.setSender(rs.getString("sender"));
		this.setReceiver(rs.getString("receiver"));
		this.setTitle(rs.getString("title"));
		this.setBody(rs.getString("body"));
		this.setUrl(rs.getString("url"));
		this.setDate(rs.getDate("date"));
		this.setRead(rs.getBoolean("read"));
		this.setNotice(rs.getBoolean("notice"));
		}catch (Exception e) {
			throw new CreateNotiFailException("sql 에러");
		}
	}
	
	@Override
	public String toJson() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isInvalid() {
		if(nid == -1 && sender.contentEquals("invalid")) {
			return true;
		}else {
			return false;
		}
	}
}
