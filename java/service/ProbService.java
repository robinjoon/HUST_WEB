package service;

import java.util.ArrayList;

import dao.ProblemDAO;
import vo.Member;
import vo.Problem;

public class ProbService {
	public static boolean write(Problem prob) {
		ProblemDAO dao = ProblemDAO.getInstance();
		return dao.write_problem(prob);
	}
	public static boolean modify(Problem prob) {
		ProblemDAO dao = ProblemDAO.getInstance();
		return dao.update_problem(prob);
	}
	public static boolean delete(Problem prob) {
		ProblemDAO dao = ProblemDAO.getInstance();
		return dao.delete_problem(prob);
	}
	public static ArrayList<Problem> getProbList(){
		ProblemDAO dao = ProblemDAO.getInstance();
		return dao.getProbList();
	}
	public static Problem getProb(int probid){
		ProblemDAO dao = ProblemDAO.getInstance();
		return dao.getProb(probid);
	}
	public static boolean scoring(Problem prob,String user_id){
		ProblemDAO dao = ProblemDAO.getInstance();
		return dao.scoring(prob, user_id);
	}
	public static ArrayList<Integer> getSolvedList(String user_id){
		ProblemDAO dao = ProblemDAO.getInstance();
		return dao.getSolvedList(user_id);
	}
	public static ArrayList<Member> getRanking(){
		ProblemDAO dao = ProblemDAO.getInstance();
		return dao.getRanking();
	}
}
