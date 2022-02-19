package vo;
import java.sql.Date;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
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
	
	@Override
	public String toJson() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
