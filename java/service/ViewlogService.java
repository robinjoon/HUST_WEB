package service;

import dao.ViewlogDAO;
import vo.Viewlog;

public class ViewlogService {
	public static boolean viewlogging(Viewlog view) {
		ViewlogDAO dao = ViewlogDAO.getinstance();
		return dao.logging(view);
	}
}
