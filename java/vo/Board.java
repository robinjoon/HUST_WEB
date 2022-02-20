// 게시판의 정보를 저장하는 클래스
package vo;

import javax.servlet.http.HttpServletRequest;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class Board implements VO{ 
	
	@NonNull
	private String board_name; //게시판 이름
	private String board_description; // 게시판 설명
	private Permission writePermission;
	private Permission readPermission;
	private Permission managePermission;
	private Permission commentPermission;
	
	public Board(HttpServletRequest request) throws NumberFormatException, Exception {
		String board_name=request.getParameter("board_name");
		Permission rp =	Permission.intToPermission(Integer.parseInt(request.getParameter("rp")));
		Permission wp = Permission.intToPermission(Integer.parseInt(request.getParameter("wp")));
		Permission mp = Permission.intToPermission(Integer.parseInt(request.getParameter("mp")));
		Permission cp = Permission.intToPermission(Integer.parseInt(request.getParameter("cp")));
		String board_description = request.getParameter("board_description");
		this.setBoard_name(board_name);
		this.setReadPermission(rp);
		this.setWritePermission(wp);
		this.setManagePermission(mp);
		this.setCommentPermission(cp);
		this.setBoard_description(board_description);
	}
	
	@Override
	public String toJson() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
