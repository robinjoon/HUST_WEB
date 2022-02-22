package service;

import java.util.ArrayList;

import dao.PostDAO;
import vo.Post;

public class PostService {
	public static boolean writepost(Post post) {
		PostDAO dao = PostDAO.getinstance();
		return dao.write_post(post);
	}
	public static int writepost_v2(Post post) throws Exception{
		PostDAO dao = PostDAO.getinstance();
		return dao.write_post_v2(post);
	}
	public static boolean deletepost(Post post) {
		PostDAO dao = PostDAO.getinstance();
		return dao.delete_post(post);
	}
	public static boolean modify_post(Post post) {
		PostDAO dao = PostDAO.getinstance();
		return dao.update_post(post);
	}
	public static ArrayList<Post> getPostlist(String bname, Integer page){
		PostDAO dao = PostDAO.getinstance();
		return dao.getPostlist(bname,page);
	}
	public static Post getPost(int pid) {
		PostDAO dao = PostDAO.getinstance();
		return dao.getPost(pid);
	}
	public static ArrayList<Post> getMyPosts(String writer){
		PostDAO dao = PostDAO.getinstance();
		return dao.getMyPosts(writer);
	}
	public static boolean increase_views(int pid) {
		PostDAO dao = PostDAO.getinstance();
		return dao.increase_views(pid);
	}
}
