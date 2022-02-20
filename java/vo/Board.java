// 게시판의 정보를 저장하는 클래스
package vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Board implements VO{ 
	
	private String board_name; //게시판 이름
	private String board_description; // 게시판 설명
	private Permission writePermission;
	private Permission readPermission;
	private Permission managePermission;
	private Permission commentPermission;
	
	
	@Override
	public String toJson() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
