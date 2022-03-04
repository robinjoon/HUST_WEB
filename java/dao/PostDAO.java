package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import db.DB;
import vo.Post;

public class PostDAO {
	private static PostDAO dao;
	
	private PostDAO() {}
	
	public static PostDAO getinstance() {
		if(dao==null) {
			dao = new PostDAO();
		}
		return dao;
	}
	
	public boolean write_post(Post post) { 
		// 게시글 쓰기. 게시글 작성 성공여부를 반환함.
		Connection conn = DB.getConnection();
		PreparedStatement pstmt = null;
		try {
			String sql = "insert into posts (writer,title,content,is_notice,origin_file_name,system_file_name,board_name)";
			sql = sql +" values(?,?,?,?,?,?,?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, post.getWriter());
			pstmt.setString(2, post.getTitle());
			pstmt.setString(3, post.getContent());
			pstmt.setBoolean(4, post.is_notice());
			pstmt.setString(5, post.getOrigin_file_name());
			pstmt.setString(6, post.getSystem_file_name());
			pstmt.setString(7, post.getBoard_name());
			if(pstmt.executeUpdate()==1) {
				return true;
			}else {
				return false;
			}
		}catch(Exception e) {
			System.err.println(e);
			return false;
		}
	}
	public int write_post_v2(Post post) throws Exception{ 
		// 게시글 쓰기. 작성된 게시글의 게시글번호를 반환함.
		Connection conn = DB.getConnection();
		PreparedStatement pstmt = null;
		try {
			String sql = "insert into posts (writer,title,content,is_notice,origin_file_name,system_file_name,board_name)";
			sql = sql +" values(?,?,?,?,?,?,?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, post.getWriter());
			pstmt.setString(2, post.getTitle());
			pstmt.setString(3, post.getContent());
			pstmt.setBoolean(4, post.is_notice());
			pstmt.setString(5, post.getOrigin_file_name());
			pstmt.setString(6, post.getSystem_file_name());
			pstmt.setString(7, post.getBoard_name());
			if(pstmt.executeUpdate()==1) {
				ResultSet generatedKeys = pstmt.getGeneratedKeys();
				return generatedKeys.getInt(1);
			}else {
				throw new Exception("fail write post");
			}
		}catch(Exception e) {
			System.err.println(e);
			throw new Exception("fail write post");
		}
	}
	/*public boolean write_post2(PostVO post) { // 게시글 쓰기.
		Connection conn = DB.getConnection();
		PreparedStatement pstmt = null;
		try {
			String sql = "insert into posts (writer,title,content,is_notice,origin_file_name,system_file_name,board_name,pid,write_date)";
			sql = sql +" values(?,?,?,?,?,?,?,?,?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, post.getWriter());
			pstmt.setString(2, post.getTitle());
			pstmt.setString(3, post.getContent());
			pstmt.setBoolean(4, post.is_notice());
			pstmt.setString(5, post.getOrigin_file_name());
			pstmt.setString(6, post.getSystem_file_name());
			pstmt.setString(7, post.getBoard_name());
			pstmt.setInt(8, post.getPid());
			pstmt.setTimestamp(9, post.getWrite_date());
			pstmt.executeUpdate();
			return true;
		}catch(Exception e) {
			System.err.println(e);
			return false;
		}
	}*/
	
	public boolean delete_post(Post post) { // 게시글 삭제. 댓글 고려 X 수정필요
		Connection conn = DB.getConnection();
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement("update posts set blind=1 where pid = ? and writer = ?");
			pstmt.setInt(1, post.getPid());
			pstmt.setString(2, post.getWriter());
			pstmt.executeUpdate();
			System.out.println("pid : "+post.getPid());
			System.out.println("writer : "+post.getWriter());
			return true;
		}catch(Exception e) {
			System.err.println(e);
			return false;
		}
	}
	
	public boolean update_post(Post post) { // 게시글 업데이트. 보안고려X, 수정필요
		Connection conn = DB.getConnection();
		PreparedStatement pstmt = null;
		try {
			String sql = "update posts set title = ?,content = ?,board_name = ?,is_notice =? where pid = ? and writer = ?";
			System.out.println(sql);
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, post.getTitle());
			pstmt.setString(2, post.getContent());
			pstmt.setString(3, post.getBoard_name());
			pstmt.setBoolean(4, post.is_notice());
			//pstmt.setTimestamp(4, post.getWrite_date());
			pstmt.setInt(5, post.getPid());
			pstmt.setString(6, post.getWriter());
			pstmt.executeUpdate();
			return true;
		}catch(Exception e) {
			System.err.println(e);
			return false;
		}
	}

	public Post getPost(int pid) { // 게시글 하나 불러오기. 
		Connection conn = DB.getConnection();
		PreparedStatement pstmt = null;
		Post post = new Post();
		try {
			String sql = "select * from posts where pid = ?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, pid);
			
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				post = new Post(rs);
			}
			return post;
		}catch(Exception e) {
			System.err.println(e);
			return null;
		}
	}

	public ArrayList<Post> getPostlist(String bname, Integer page) {
		ArrayList<Post> postlist = new ArrayList<Post>();
		Connection conn = DB.getConnection();
		PreparedStatement pstmt = null;
		try {
			String sql = "select * from posts where blind=0 and board_name=?  order by pid desc";

			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, bname);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				Post post = new Post(rs);
				postlist.add(post);
			}
			return postlist;
		} catch (Exception e) {
			System.err.println(e);
			return postlist;
		}
	}
	
	public ArrayList<Post> getNoticeBoard(){
		Connection conn = DB.getConnection();
		PreparedStatement pstmt = null;
		ArrayList<Post> list = new ArrayList<Post>();
		try {
			String sql = "select * from posts where blind=0 and board_name=\"공지게시판\" order by write_date desc limit 5";
			
			pstmt = conn.prepareStatement(sql);
			
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				Post post = new Post(rs);
				list.add(post);
			}
			return list;
		}catch(Exception e) {
			System.err.println(e);
			return null;
		}
	}
	public ArrayList<Post> getFreeBoard(){
		Connection conn = DB.getConnection();
		PreparedStatement pstmt = null;
		ArrayList<Post> list = new ArrayList<Post>();
		try {
			String sql = "select * from posts where blind=0 and board_name=\"자유게시판\" order by write_date desc limit 5";
			
			pstmt = conn.prepareStatement(sql);
			
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				Post post = new Post(rs);
				list.add(post);
			}
			return list;
		}catch(Exception e) {
			System.err.println(e);
			return null;
		}
	}
	
	public ArrayList<Post> getMyPosts(String writer){
		Connection conn = DB.getConnection();
		PreparedStatement pstmt = null;
		ArrayList<Post> list = new ArrayList<Post>();
		try {
			String sql = "select * from posts where writer = ?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, writer);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				Post post = new Post(rs);
				list.add(post);
			}
			return list;
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public boolean increase_views(int pid) {
		Connection conn = DB.getConnection();
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement("update posts set views = views+1 where pid= ?");
			pstmt.setInt(1, pid);
			pstmt.executeUpdate();
			return true;
		}catch(Exception e) {
			System.err.println(e);
			return false;
		}
	}
}
