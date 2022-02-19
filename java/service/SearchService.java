package service;

import java.util.ArrayList;

import dao.SearchDAO;
import vo.Post;

public class SearchService {
	public static ArrayList<Post> searchPost(String search_word,String search_target, int permission, String sort){
		SearchDAO dao = SearchDAO.getInstance();
		return dao.searchPost(search_word, search_target, permission, sort);
	}
	public static ArrayList<Post> search_from_board(String board_name, String search_word,String search_target, int permission, String sort){
		SearchDAO dao = SearchDAO.getInstance();
		return dao.searchPost_from_board(board_name, search_word, search_target, permission, sort);
	}
}
