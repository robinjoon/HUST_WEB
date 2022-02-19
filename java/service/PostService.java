package service;

import java.util.ArrayList;

import dao.PostDAO;
import vo.PostVO;

public class PostService {
	public static boolean writepost(PostVO post) {
		PostDAO dao = PostDAO.getinstance();
		return dao.write_post(post);
	}
	public static int writepost_v2(PostVO post) {
		PostDAO dao = PostDAO.getinstance();
		return dao.write_post_v2(post);
	}
	public static boolean deletepost(PostVO post) {
		PostDAO dao = PostDAO.getinstance();
		return dao.delete_post(post);
	}
	public static boolean modify_post(PostVO post) {
		PostDAO dao = PostDAO.getinstance();
		return dao.update_post(post);
	}
	public static ArrayList<PostVO> getPostlist(String bname, Integer page){
		PostDAO dao = PostDAO.getinstance();
		return dao.getPostlist(bname,page);
	}
	public static PostVO getPost(int pid) {
		PostDAO dao = PostDAO.getinstance();
		return dao.getPost(pid);
	}
	public static ArrayList<PostVO> getMyPosts(String writer){
		PostDAO dao = PostDAO.getinstance();
		return dao.getMyPosts(writer);
	}
	public static boolean increase_views(int pid) {
		PostDAO dao = PostDAO.getinstance();
		return dao.increase_views(pid);
	}
}
