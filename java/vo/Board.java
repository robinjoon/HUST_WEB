// 게시판의 정보를 저장하는 클래스
package vo;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;

import exceptions.CreateBoardException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter(value = AccessLevel.PRIVATE)
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
	
	public Board(ResultSet rs) {
		try {
			this.setBoard_name(rs.getString("board_name"));
			this.setReadPermission(Permission.intToPermission(rs.getInt("read_permission")));
			this.setWritePermission(Permission.intToPermission(rs.getInt("write_permission")));
			this.setManagePermission(Permission.intToPermission(rs.getInt("manage_permission")));
			this.setCommentPermission(Permission.intToPermission(rs.getInt("comment_permission")));
			this.setBoard_description(rs.getString("board_description"));
		}catch(SQLException e) {
			throw new CreateBoardException("sql 에러"); 
		}
	}
	
	public Board() {
		this.board_description ="";
		this.board_name="";
		this.commentPermission = Permission.INVALID;
		this.managePermission = Permission.INVALID;
		this.readPermission = Permission.INVALID;
		this.writePermission = Permission.INVALID;
	}
	
	@Override
	public String toJson() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isInvalid() {
		if(board_name == "" || board_description == "" || commentPermission == Permission.INVALID
				|| managePermission == Permission.INVALID || readPermission == Permission.INVALID
				|| writePermission == Permission.INVALID) {
			return true;
		}else {
			return false;
		}
	}
	
}
