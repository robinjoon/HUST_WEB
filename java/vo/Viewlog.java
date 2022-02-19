// 조회수 구현시 새로고침연타로 조회수 올라가는걸 막기 위한 VO
package vo;
import java.sql.Timestamp;
public class Viewlog implements VO {

	private String id;
	private int pid;
	private Timestamp date;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getPid() {
		return pid;
	}
	public void setPid(int pid) {
		this.pid = pid;
	}
	public Timestamp getDate() {
		return date;
	}
	public void setDate(Timestamp date) {
		this.date = date;
	}

	@Override
	public String toJson() {
		// TODO Auto-generated method stub
		return null;
	}

}
