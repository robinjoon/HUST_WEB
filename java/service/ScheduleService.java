package service;

import java.util.ArrayList;

import dao.ScheduleDAO;
import vo.Schedule;

public class ScheduleService {
	public static boolean create_schedule(Schedule schedule) {
		ScheduleDAO dao = ScheduleDAO.getInstance();
		return dao.create(schedule);
	}
	
	public static boolean delete_schedule(Schedule schedule) {
		ScheduleDAO dao = ScheduleDAO.getInstance();
		return dao.delete(schedule);
	}
	
	public static boolean update_schedule(Schedule schedule) {
		ScheduleDAO dao = ScheduleDAO.getInstance();
		return dao.update(schedule);
	}
	
	public static ArrayList<Schedule> getScheduleList(String what, int year, int month){
		ScheduleDAO dao = ScheduleDAO.getInstance();
		return dao.getScheduleList(what,year,month);
	}
	
	public static boolean is_ob(Schedule schedule) {
		ScheduleDAO dao = ScheduleDAO.getInstance();
		return dao.is_ob(schedule);
	}
}
