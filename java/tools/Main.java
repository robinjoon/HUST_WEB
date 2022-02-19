package tools;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;

import dao.CommentDAO;
import dao.MemberDAO;
import dao.PostDAO;
import dao.SearchDAO;
import db.*;
import vo.*;

public class Main {

	public static void main(String[] args) throws NoSuchAlgorithmException {
		/*System.out.println(Secure.MD5("test"));
		System.out.println(Secure.sha256(Secure.MD5("test")));
		System.out.println(MemberDAO.getInstance().resetPw(""));*/
		/*Connection conn = DB.getConnection();
		ArrayList<String> boards = new ArrayList<String>();
		HashMap<String,Boolean> databoards = new HashMap<String,Boolean>();
		PreparedStatement pstmt = null;
		try {
			String sql = "select tablename,datafile from husterp.boards";
			
			pstmt = conn.prepareStatement(sql);
			
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				boards.add(rs.getString("tablename"));
				databoards.put(rs.getString("tablename"), rs.getBoolean("datafile"));
			}
			
		}catch(Exception e) {
			System.err.println(e);
		
		}
		boards.remove("hust2006");
		for(int i=0;i<boards.size();i++)
			System.out.println(boards.get(i)+" "+databoards.get(boards.get(i)));
		ArrayList<PandC> list = new ArrayList<PandC>();
		for(int i =0;i<boards.size();i++) {
			String boardname = boards.get(i);
			boolean is_data = databoards.get(boardname);
			conn = DB.getConnection();
			pstmt = null;
			try {
				String sql = "select * from husterp."+boardname +" where stat=1";
				
				pstmt = conn.prepareStatement(sql);
				
				ResultSet rs = pstmt.executeQuery();
				while(rs.next()) {
					PostVO post = new PostVO();
					post.setWriter(rs.getString("id"));
					post.setWrite_date(rs.getTimestamp("Datetime"));
					post.setTitle(rs.getString("title"));
					post.setContent(rs.getString("content"));
					post.setBoard_name(boardname);
					if(is_data) {
						post.setOrigin_file_name(rs.getString("file"));
						if(boardname.contains("2006")) {
							post.setSystem_file_name(rs.getString("file"));
						}else {
							post.setSystem_file_name(rs.getInt("cnt")+"");
						}
					}
					PandC pandc = new PandC();
					pandc.setPost(post);	
					ArrayList<CommentVO> clist = new ArrayList<CommentVO>();
					String sql2 = "select * from husterp."+boardname+"comment where cnt = ? and stat=1";
					PreparedStatement pstmt2 = conn.prepareStatement(sql2);
					pstmt2.setInt(1, rs.getInt("cnt"));
					ResultSet rs2 = pstmt2.executeQuery();
					while(rs2.next()) {
						CommentVO comment = new CommentVO();
						comment.setContent(rs2.getString("content"));
						comment.setWrite_date(rs2.getTimestamp("datetime"));
						comment.setWriter(rs2.getString("id"));
						clist.add(comment);
					}
					pandc.setComments(clist);
					list.add(pandc);
				}
				
			}catch(Exception e) {
				System.err.println(e);
			
			}
		}
		Collections.sort(list);
		ArrayList<CommentVO> commentlist = new ArrayList<CommentVO>();
		for(int i =0;i<list.size();i++) {
			PostVO post = list.get(i).getPost();
			post.setPid(i+1);
			PostDAO.getinstance().write_post2(post);
			ArrayList<CommentVO> clist = list.get(i).getComments();
			for(int j=0;j<clist.size();j++) {
				CommentVO c = clist.get(j);
				c.setPid(i+1);
				commentlist.add(c);
			}
		}
		Collections.sort(commentlist);
		for(int i=0;i<commentlist.size();i++) {
			commentlist.get(i).setCid(i+1);
			CommentDAO.getInstance().write_comment2(commentlist.get(i));
		}
		
		System.out.println("a");
		ArrayList<PostVO> list = SearchDAO.getInstance().searchPost("ro", "writer", 6);
		System.out.println(list.size());
		for(int i=0;i<list.size();i++)
			System.out.println(list.get(i).getTitle());*/
		String str ="so0108";
		str = Secure.MD5(str).toUpperCase();
		System.out.println(Secure.sha256(str));
	}

}
