package service;

import java.util.ArrayList;

import dao.NotiDAO;
import vo.NotiVO;

public class NotiService {
	public static boolean send_noti(NotiVO noti) {
		NotiDAO dao = NotiDAO.getInstance();
		return dao.send_noti(noti);
	}
	public static boolean read_noti(NotiVO noti) {
		NotiDAO dao = NotiDAO.getInstance();
		return dao.read_noti(noti);
	}
	public static boolean delete_noti(NotiVO noti) {
		NotiDAO dao = NotiDAO.getInstance();
		return dao.delete_noti(noti);
	}
	public static NotiVO getNoti(long nid) {
		NotiDAO dao = NotiDAO.getInstance();
		return dao.getNoti(nid);
	}
	public static ArrayList<NotiVO> getNotiList(String receiver){
		NotiDAO dao = NotiDAO.getInstance();
		return dao.getNotiList(receiver);
	}
	
}
