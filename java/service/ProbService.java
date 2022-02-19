package service;

import java.util.ArrayList;

import dao.ProblemDAO;
import vo.MemberVO;
import vo.ProblemVO;

public class ProbService {
	public static boolean write(ProblemVO prob) {
		ProblemDAO dao = ProblemDAO.getInstance();
		return dao.write_problem(prob);
	}
	public static boolean modify(ProblemVO prob) {
		ProblemDAO dao = ProblemDAO.getInstance();
		return dao.update_problem(prob);
	}
	public static boolean delete(ProblemVO prob) {
		ProblemDAO dao = ProblemDAO.getInstance();
		return dao.delete_problem(prob);
	}
	public static ArrayList<ProblemVO> getProbList(){
		ProblemDAO dao = ProblemDAO.getInstance();
		return dao.getProbList();
	}
	public static ProblemVO getProb(int probid){
		ProblemDAO dao = ProblemDAO.getInstance();
		return dao.getProb(probid);
	}
	public static boolean scoring(ProblemVO prob,String user_id){
		ProblemDAO dao = ProblemDAO.getInstance();
		return dao.scoring(prob, user_id);
	}
	public static ArrayList<Integer> getSolvedList(String user_id){
		ProblemDAO dao = ProblemDAO.getInstance();
		return dao.getSolvedList(user_id);
	}
	public static ArrayList<MemberVO> getRanking(){
		ProblemDAO dao = ProblemDAO.getInstance();
		return dao.getRanking();
	}
}
