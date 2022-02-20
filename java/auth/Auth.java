package auth;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import vo.Permission;

@RequiredArgsConstructor
public class Auth {
	@NonNull
	private String id;
	@NonNull
	@Getter
	private Permission permission;
	
	private String csrfServer;
	private String csrfClient;
	
	public Auth(String id, int permission) throws Exception {
		this.id = id;
		this.permission = Permission.intToPermission(permission);
	}
	
	public Auth(HttpServletRequest request) throws Exception {
		HttpSession session = request.getSession();
		String id = (String)session.getAttribute("id");
		Permission permission = Permission.intToPermission((Integer)session.getAttribute("permission"));
		String server = (String)session.getAttribute("csrf_token");
		String client = request.getParameter("csrf_token");
		this.id = id;
		this.permission = permission;
		this.csrfServer = server;
		this.csrfClient = client;
	}
	
	boolean isLogin() {
		if(id!=null && permission!=null) {
			return true;
		}else {
			return false;
		}
	}
	
	boolean isSafeFromCSRF() {
		if(csrfServer==null || csrfClient==null) {
			return false;
		}else {
			return csrfServer.contentEquals(csrfClient);
		}
	}
}
