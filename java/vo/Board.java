// 게시판의 정보를 저장하는 클래스
package vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Board implements VO{ 
	
	private String board_name; //게시판 이름
	private int write_permission; // 게시판 쓰기 권한
	private int read_permission; //게시판 읽기 권한
	private int manage_permission; // 게시판 관리권한
	private int comment_permission; // 게시판댓글쓰기권한
	private String board_description; // 게시판 설명
	
	@Override
	public String toJson() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
