// 게시글의 정보를 저장하는 클래스 
// pid,views,blind 를 제외한 나머지 필드들이 모두 동일한 게시글이 두개 이상 존재할 수 없음. 즉, 중복게시글 업로드 불가.
package vo;

import java.sql.ResultSet;
import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;

import com.oreilly.servlet.MultipartRequest;

import auth.Auth;
import auth.AuthManager;
import exceptions.CreatePostFailException;
import exceptions.InvalidPostException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import service.BoardService;
import tools.Secure;
@Getter
@Setter(value = AccessLevel.PRIVATE)
public class Post implements VO{
	private int pid; // 게시글 번호
	private String writer; // 작성자
	private String board_name; // 게시판 이름
	private String title; // 게시글 제목
	private String content; // 게시글 본문
	private Timestamp write_date; // 게시글 작성날짜
	private boolean is_notice; // 공지글인지 여부
	private long views; // 조회수
	private String origin_file_name; // 실제 업로드시 설정된 파일이름
	private String system_file_name; // 서버에 저장되는 파일이름. 이름중복을 고려.
	private boolean blind; // 게시글 숨김여부. 삭제된 게시글은 blind가 true 인 채 남아있음.
	
	public static void addPid(Post post, int pid) {
		post.pid = pid;
	}
	
	public static void deleteNotice(Post post) {
		post.is_notice = false;
	}
	
	public static void checkCsrf(Post post) {
		String title = post.getTitle();
		String content = post.getContent();
		title = Secure.check_input(title);
		content = Secure.check_script(content);
		post.setTitle(title);
		post.setContent(content);
	}
	
	public static void blindPostProcessing(Auth auth,Post post){
		String content = post.getContent();
		post.setContent("원래 제목 : "+post.getTitle()+"<br>"+post.getContent());
		post.setTitle("이 게시글은 삭제된 게시글입니다.");
		content = Secure.check_script(content);
		post.setContent(content);
	}
	
	public Post() {
		this.pid = -2;
	}
	
	public static Post getPostFromRequestWhenDelete(HttpServletRequest request) {
		int pid = Integer.parseInt(request.getParameter("pid"));
		String writer = request.getParameter("writer");
		String board_name = request.getParameter("board_name");
		Post post = new Post();
		post.setPid(pid);
		post.setBoard_name(board_name);
		post.setWriter(writer);
		return post;
	}
	
	public Post(HttpServletRequest request) {
		Auth auth = new Auth(request);
		int pid = Integer.parseInt(request.getParameter("pid"));
		String board_name = request.getParameter("board_name");
		String title = request.getParameter("title");
		String content = request.getParameter("content");
		boolean is_notice = Boolean.parseBoolean(request.getParameter("is_notice"));
		this.setPid(pid);
		this.setBoard_name(board_name);
		this.setTitle(title);
		this.setContent(content);
		Board board = BoardService.getBoard(board_name);
		if(is_notice && AuthManager.canManageBoard(auth, board)) // 관리자가 아닌 사람이 공지게시글로 설정한 경우 공지가 아니게 변경.
			is_notice=false;
		this.set_notice(is_notice);
	}
	
	public Post(MultipartRequest multi, String writer) {
		String board_name = multi.getParameter("board_name");
		String title = multi.getParameter("title");
		String content = multi.getParameter("content");
		String origin_file_name = multi.getOriginalFileName("file");
		String system_file_name = multi.getFilesystemName("file");
		if(writer.isBlank()||board_name.isBlank()||title.isBlank()||content.isBlank()) {
			System.out.println(content);
			throw new InvalidPostException("필수 데이터 누락!");
		}
		boolean is_notice = Boolean.parseBoolean(multi.getParameter("is_notice"));		
		this.setWriter(writer);
		this.setBoard_name(board_name);
		this.setTitle(title);
		this.setContent(Secure.check_script(content));
		this.setContent(this.getContent().replaceAll("<img ", "<img class='w-100' "));
		this.setOrigin_file_name(origin_file_name!=null ? origin_file_name : ""); // 첨부파일이 없을 때 첨부파일의 이름을 공백으로 설정. 안하면 pid 가져오는 쿼리에서 null로인해 정상적인 값을 가져오지 못함. 
		this.setSystem_file_name(system_file_name!=null ? system_file_name : "");
		this.set_notice(is_notice);
	}
	
	public Post(ResultSet rs) {
		try {
			this.setPid(rs.getInt("pid"));
			this.setWriter(rs.getString("writer"));
			this.setBoard_name(rs.getString("board_name"));
			this.setTitle(rs.getString("title"));
			this.setContent(rs.getString("content"));
			this.setWrite_date(rs.getTimestamp("write_date"));
			this.set_notice(rs.getBoolean("is_notice"));
			this.setViews(rs.getLong("views"));
			this.setOrigin_file_name(rs.getString("origin_file_name"));
			this.setSystem_file_name(rs.getString("system_file_name"));
			this.setBlind(rs.getBoolean("blind"));
		}catch(Exception e) {
			throw new CreatePostFailException("sql 에러");
		}
	}
	
	@Override
	public String toJson() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isInvalid() {
		if(pid == -2) {
			return true;
		}else {
			return false;
		}
	}
	
}
