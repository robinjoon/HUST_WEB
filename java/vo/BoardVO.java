// 게시판의 정보를 저장하는 클래스
package vo;

public class BoardVO implements VO{ 
	
	private String board_name; //게시판 이름
	private int write_permission; // 게시판 쓰기 권한
	private int read_permission; //게시판 읽기 권한
	private int manage_permission; // 게시판 관리권한
	private int comment_permission; // 게시판댓글쓰기권한
	private String board_description; // 게시판 설명
	public String getBoard_name() {
		return board_name;
	}
	public void setBoard_name(String board_name) {
		this.board_name = board_name;
	}
	public int getWrite_permission() {
		return write_permission;
	}
	public void setWrite_permission(int write_permission) {
		this.write_permission = write_permission;
	}
	public int getRead_permission() {
		return read_permission;
	}
	public void setRead_permission(int read_permission) {
		this.read_permission = read_permission;
	}
	public int getManage_permission() {
		return manage_permission;
	}
	public void setManage_permission(int manage_permission) {
		this.manage_permission = manage_permission;
	}
	public int getComment_permission() {
		return comment_permission;
	}
	public void setComment_permission(int comment_permission) {
		this.comment_permission = comment_permission;
	}
	public String getBoard_description() {
		return board_description;
	}
	public void setBoard_description(String board_description) {
		this.board_description = board_description;
	}
	@Override
	public String toJson() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
