package service;

import java.util.ArrayList;

import dao.NotiDAO;
import vo.Noti;

public class NotiService {
	public static boolean send_noti(Noti noti) {
		NotiDAO dao = NotiDAO.getInstance();
		return dao.send_noti(noti);
	}
	public static boolean read_noti(Noti noti) {
		NotiDAO dao = NotiDAO.getInstance();
		return dao.read_noti(noti);
	}
	public static boolean delete_noti(Noti noti) {
		NotiDAO dao = NotiDAO.getInstance();
		return dao.delete_noti(noti);
	}
	public static Noti getNoti(long nid) {
		NotiDAO dao = NotiDAO.getInstance();
		return dao.getNoti(nid);
	}
	public static ArrayList<Noti> getNotiList(String receiver){
		NotiDAO dao = NotiDAO.getInstance();
		return dao.getNotiList(receiver);
	}
	
}
