// 게시글의 정보를 저장하는 클래스 
// pid,views,blind 를 제외한 나머지 필드들이 모두 동일한 게시글이 두개 이상 존재할 수 없음. 즉, 중복게시글 업로드 불가.
package vo;

import java.sql.Timestamp;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
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
	
	
	@Override
	public String toJson() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
