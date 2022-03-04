package dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import db.DB;
import exceptions.CreateCommentException;
import exceptions.DeleteCommentFailException;
import exceptions.UpdateCommentFailException;
import vo.*;
public class CommentDAO {
	private static CommentDAO dao;
	private CommentDAO() {}
	public static CommentDAO getInstance() {
		if(dao==null) {
			dao = new CommentDAO();
		}
		return dao;
	}
	public void write_comment(Comment comment) { //댓글작성. 보안고려 X
		Connection conn = DB.getConnection();
		PreparedStatement pstmt = null;
		try {
			String sql = "insert into comments(pid,writer,content,parent)";
			sql = sql + " values(?,?,?,?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, comment.getPid());
			pstmt.setString(2, comment.getWriter());
			pstmt.setString(3, comment.getContent());
			pstmt.setLong(4, comment.getParent());
			pstmt.executeUpdate();
		}catch(Exception e) {
			throw new CreateCommentException("sql 에러");
		}
	}

	public void update_comment(Comment comment) { // 댓글 수정. 보안고려 X
		Connection conn = DB.getConnection();
		PreparedStatement pstmt = null;
		try {
			String sql = "update comments set content = ? where cid = ? and writer = ? and parent = ?";
			pstmt = conn.prepareStatement(sql);
		
			pstmt.setString(1, comment.getContent());
			pstmt.setLong(2, comment.getCid());
			pstmt.setString(3, comment.getWriter());
			pstmt.setLong(4, comment.getParent());
			if(pstmt.executeUpdate()==0){
				throw new UpdateCommentFailException("업데이트 실패");
			}
		}catch(Exception e) {
			System.err.println(e);
			throw new UpdateCommentFailException("업데이트 실패");
		}
	}
	
	public void delete_comment(Comment comment) { // 댓글 삭제. 실제로는 댓글을 비활성화 하는 것.
		Connection conn = DB.getConnection();
		PreparedStatement pstmt = null;
		try {
			String sql = "update comments set blind = 1 where cid = ? and writer =?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, comment.getCid());
			pstmt.setString(2, comment.getWriter());
			System.out.println(comment.getCid() + " "+ comment.getWriter());
			if(pstmt.executeUpdate()==0) {
				throw new DeleteCommentFailException("sql 에러");
			}
		}catch(Exception e) {
			System.err.println(e);
			throw new DeleteCommentFailException("sql 에러");
		}
	}
	
	private ArrayList<Comment> getComments_no_parent(int pid){ //게시글 댓글중 부모댓글만 가져오기.
		ArrayList<Comment> commentlist = new ArrayList<Comment>();
		Connection conn = DB.getConnection();
		PreparedStatement pstmt = null;
		try {
			String sql = "select * from comments where pid = ? and parent=-1";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, pid);
			
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				Comment comment = new Comment(rs);
				commentlist.add(comment);
			}
			return commentlist;
		}catch(Exception e) {
			System.err.println(e);
			return new ArrayList<Comment>();
		}
	}
	
	
	public ArrayList<ArrayList<Comment>> getCommentList_v2(int pid) throws Exception{ //게시글 댓글리스트 불러오기 
		/*
		 * 댓글리스트는 이중 ArrayList로 표현됨. 내부 ArrayList의 첫번째 원소는 부모댓글(원댓글)이 없는 댓글이고,
		 * 나머지 원소들은 모두 첫번재 원소를 부모댓글로 하는 자식댓글(대댓글)들임.
		 * 손자댓글은 존재하지 않음. 이를 위한 검증코드 추가필요.
		 * 삭제된 댓글의 경우 컨트롤러단에서 처리.
		 * */
		ArrayList<ArrayList<Comment>> commentlist = new ArrayList<ArrayList<Comment>>();
		Connection conn = DB.getConnection();
		PreparedStatement pstmt = null;
		String sql = "select * from comments where pid = ? and parent!=-1";
		
		pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, pid);
		
		ResultSet rs = pstmt.executeQuery();
		
		ArrayList<Comment> comments = getComments_no_parent(pid); // 부모댓글들을 모아둔 리스트
		for(int i=0;i<comments.size();i++) { // 부모댓글들을 내부 ArrayList에 각각 집어넣고, 이런 내부 리스트들을 바깥리스트에 집어넣음.
			ArrayList<Comment> comment_group = new ArrayList<Comment>();
			comment_group.add(comments.get(i));
			commentlist.add(comment_group);
		}
		ArrayList<Comment> tmp = new ArrayList<Comment>(); // 자식댓글 임시저장하는 리스트
		while(rs.next()) { // 자식댓글들을 임시저장 리스트에 저장.
			Comment comment = new Comment(rs);
			tmp.add(comment);
		}
		for(int i=0;i<tmp.size();i++) { // 임시저장리스트로부터 자식댓글들을 각자의 부모댓글이 포함된 리스트로 옮김.
			Comment comment = tmp.get(i);
			for(int j =0;j<commentlist.size();j++) {
				Comment parent = commentlist.get(j).get(0);
				if(parent.getCid()==comment.getParent()) {
					commentlist.get(j).add(comment);
				}
			}
		}
		return commentlist;
	}
	public Comment getComment(long cid) {
		if(cid == -1) {
			throw new RuntimeException("Invalid Parent");
		}
		Connection conn = DB.getConnection();
		PreparedStatement pstmt = null;
		try {
			String sql = "select * from comments where cid = ?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1,cid);
			
			ResultSet rs = pstmt.executeQuery();
			Comment comment = Comment.INVALID_COMMENT;
			while(rs.next()) {
				comment = new Comment(rs);
			}
			return comment;
		}catch(Exception e) {
			System.err.println(e);
			return Comment.INVALID_COMMENT;
		}
	}
	public ArrayList<Comment> getMyComments(String id){ //내가 쓴 댓글 불러오기.
		ArrayList<Comment> commentlist = new ArrayList<Comment>();
		Connection conn = DB.getConnection();
		PreparedStatement pstmt = null;
		try {
			String sql = "select * from comments where writer = ?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1,id);
			
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				Comment comment = new Comment(rs);
				commentlist.add(comment);
			}
			return commentlist;
		}catch(Exception e) {
			System.err.println(e);
			return new ArrayList<Comment>();
		}
	}
}
