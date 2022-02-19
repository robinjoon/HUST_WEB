// 조회수 구현시 새로고침연타로 조회수 올라가는걸 막기 위한 VO
package vo;
import java.sql.Timestamp;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Viewlog implements VO {

	private String id;
	private int pid;
	private Timestamp date;
	
	@Override
	public String toJson() {
		// TODO Auto-generated method stub
		return null;
	}

}
