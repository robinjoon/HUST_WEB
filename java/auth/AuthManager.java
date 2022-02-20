package auth;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import vo.*;

public class AuthManager {
	
	public static boolean canReadMemberList(Auth auth) {
		Permission memberPermission = auth.getPermission();
		if(memberPermission.compareTo(Permission.NEWBIE)>=0) {
			return true;
		}else {
			return false;
		}
	}
	
	public static boolean loginCheck(Auth auth) {
		return auth.isLogin();
	}
	
	public static boolean csrfCheck(Auth auth) {
		return auth.isSafeFromCSRF();
	}
	public static boolean canMakeBoard(Auth auth, Board board) {
		
		Permission memberPermission = auth.getPermission();
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
	
	public static boolean canManageBoard(Auth auth, Board board) {
		Permission memberPermission = auth.getPermission();
		Permission boardPermission = board.getManagePermission();
		if(memberPermission == boardPermission) {
			return true;
		}else if(memberPermission == Permission.GENREAL_ADMIN) {
			return true;
		}else {
			return false;
		}
	}
	
	
	
	public static boolean canReadBoard(Auth auth, Board board) {
		Permission memberPermission = auth.getPermission();
		Permission boardPermission = board.getReadPermission();
		if(memberPermission.compareTo(boardPermission)>=0) {
			return true;
		}else if(memberPermission == Permission.GENREAL_ADMIN) {
			return true;
		}else {
			return false;
		}
	}
	
	public static boolean canWriteBoard(Auth auth, Board board) {
		Permission memberPermission = auth.getPermission();
		Permission boardPermission = board.getWritePermission();
		if(memberPermission.compareTo(boardPermission)>=0) {
			return true;
		}else if(memberPermission == Permission.GENREAL_ADMIN) {
			return true;
		}else {
			return false;
		}
	}
	
	public static boolean canCommentBoard(Auth auth, Board board) {
		Permission memberPermission = auth.getPermission();
		Permission boardPermission = board.getCommentPermission();
		if(memberPermission.compareTo(boardPermission)>=0) {
			return true;
		}else if(memberPermission == Permission.GENREAL_ADMIN) {
			return true;
		}else {
			return false;
		}
	}
}
