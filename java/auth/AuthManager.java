package auth;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import vo.*;

public class AuthManager {
	
	public static boolean canReadMemberList(Member member) {
		Permission memberPermission = member.getPermission();
		if(memberPermission.compareTo(Permission.NEWBIE)>=0) {
			return true;
		}else {
			return false;
		}
	}
	
	public static boolean loginCheck(HttpServletRequest request) {
		HttpSession session = request.getSession();
		Integer permission = (int)session.getAttribute("permission");
		String id  = (String)session.getAttribute("id");
		if(permission!=null && id !=null) {
			return true;
		}else {
			return false;
		}
	}
	
	public static boolean csrfCheck(HttpServletRequest request) {
		HttpSession session = request.getSession();
		String server = (String)session.getAttribute("csrf_token");
		String client = request.getParameter("csrf_token");
		return server.contentEquals(client);
	}
	
	public static boolean canMakeBoard(Member member, Board board) {
		
		Permission memberPermission = member.getPermission();
		Permission readPermission = board.getReadPermission();
		Permission writePermission = board.getWritePermission();
		Permission managePermission = board.getManagePermission();
		Permission commentPermission = board.getCommentPermission();
		if(memberPermission == Permission.GENREAL_ADMIN) {
			return true;
		}else if(memberPermission == Permission.OB_ADMIN || memberPermission == Permission.YB_ADMIN) {
			if(memberPermission.compareTo(readPermission)<0 || memberPermission.compareTo(writePermission)<0
					|| memberPermission.compareTo(managePermission)<0 || memberPermission.compareTo(commentPermission)<0) {
				return false;
			}else {
				return true;
			}
		}else {
			return false;
		}
		
	}
	
	public static boolean canManage(Member member, Board board) {
		Permission memberPermission = member.getPermission();
		Permission boardPermission = board.getManagePermission();
		if(memberPermission == boardPermission) {
			return true;
		}else if(memberPermission == Permission.GENREAL_ADMIN) {
			return true;
		}else {
			return false;
		}
	}
	
	public static boolean canRead(Member member, Board board) {
		Permission memberPermission = member.getPermission();
		Permission boardPermission = board.getReadPermission();
		if(memberPermission == boardPermission) {
			return true;
		}else if(memberPermission == Permission.GENREAL_ADMIN) {
			return true;
		}else {
			return false;
		}
	}
	
	public static boolean canWrite(Member member, Board board) {
		Permission memberPermission = member.getPermission();
		Permission boardPermission = board.getWritePermission();
		if(memberPermission == boardPermission) {
			return true;
		}else if(memberPermission == Permission.GENREAL_ADMIN) {
			return true;
		}else {
			return false;
		}
	}
	
	public static boolean canComment(Member member, Board board) {
		Permission memberPermission = member.getPermission();
		Permission boardPermission = board.getCommentPermission();
		if(memberPermission == boardPermission) {
			return true;
		}else if(memberPermission == Permission.GENREAL_ADMIN) {
			return true;
		}else {
			return false;
		}
	}
}
