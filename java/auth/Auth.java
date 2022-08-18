package auth;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import lombok.AccessLevel;
import lombok.Getter;
import vo.Permission;

public class Auth {
	@Getter(value = AccessLevel.PACKAGE)
	private String id;
	@Getter
	private Permission permission;
	
	private String csrfServer;
	private String csrfClient;
	
	public Auth(String id, int permission) throws Exception {
		this.id = id;
		this.permission = Permission.intToPermission(permission);
	}
	
	public Auth(String id, Permission permission){
		this.id = id;
		this.permission = permission;
	}
	
	public Auth(HttpServletRequest request){
		try {
			HttpSession session = request.getSession();
			String id = (String)session.getAttribute("id");
			Permission permission = (Permission)session.getAttribute("permission");
			String server = (String)session.getAttribute("csrf_token");
			String client = request.getParameter("csrf_token");
			this.id = id;
			this.permission = permission;
			this.csrfServer = server;
			this.csrfClient = client;
		}catch(Exception e) {
			
		}
	}
	
	boolean isLogin() {
		if(id!=null && permission!=null) {
			return true;
		}else {
			return false;
		}
	}
	
	boolean isSafeFromCSRF() {
		if(!isLogin() || csrfServer==null || csrfClient==null) {
			return false;
		}else {
			return csrfServer.contentEquals(csrfClient);
		}
	}
}
