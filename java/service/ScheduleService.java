package service;

import java.util.ArrayList;

import dao.ScheduleDAO;
import vo.ScheduleVO;

public class ScheduleService {
	public static boolean create_schedule(ScheduleVO schedule) {
		ScheduleDAO dao = ScheduleDAO.getInstance();
		return dao.create(schedule);
	}
	
	public static boolean delete_schedule(ScheduleVO schedule) {
		ScheduleDAO dao = ScheduleDAO.getInstance();
		return dao.delete(schedule);
	}
	
	public static boolean update_schedule(ScheduleVO schedule) {
		ScheduleDAO dao = ScheduleDAO.getInstance();
		return dao.update(schedule);
	}
	
	public static ArrayList<ScheduleVO> getScheduleList(String what, int year, int month){
		ScheduleDAO dao = ScheduleDAO.getInstance();
		return dao.getScheduleList(what,year,month);
	}
	
	public static boolean is_ob(ScheduleVO schedule) {
		ScheduleDAO dao = ScheduleDAO.getInstance();
		return dao.is_ob(schedule);
	}
}
