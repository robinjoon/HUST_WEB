package auth;

import vo.*;

public class AuthManager {
	
	public static boolean canReadMemberList(Auth auth) {
		Permission memberPermission = auth.getPermission();
		int per = Permission.permissionToInt(memberPermission);
		if(per>0) {
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
	
	public static boolean isWriter(Auth auth, Post post) {
		if(auth.getId().equals(post.getWriter())) {
			return true;
		}else {
			return false;
		}
	}
	
	public static boolean isWriter(Auth auth, Comment comment) {
		if(auth.getId().equals(comment.getWriter())) {
			return true;
		}else {
			return false;
		}
	}
	
	public static boolean isAdmin(Auth auth) {
		Permission permission = auth.getPermission();
		int per = Permission.permissionToInt(permission);
		if(per >=4) {
			return true;
		}else {
			return false;
		}
	}
	
	public static boolean isYBAdmin(Auth auth) {
		Permission permission = auth.getPermission();
		if(permission == Permission.YB_ADMIN) {
			return true;
		}else {
			return false;
		}
	}
	public static boolean isOBAdmin(Auth auth) {
		Permission permission = auth.getPermission();
		if(permission == Permission.OB_ADMIN) {
			return true;
		}else {
			return false;
		}
	}
	public static boolean isGenrealAdmin(Auth auth) {
		Permission permission = auth.getPermission();
		if(permission == Permission.GENREAL_ADMIN) {
			return true;
		}else {
			return false;
		}
	}
}
