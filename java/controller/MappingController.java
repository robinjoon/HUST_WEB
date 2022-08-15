package controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import auth.Auth;
import auth.AuthManager;
import tools.HttpUtil;

public class MappingController implements Controller {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response, String action)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		Auth auth = new Auth(request);
		if(action.contentEquals("adminpage")) {
			if(AuthManager.isAdmin(auth)) {
				HttpUtil.forward(request, response, "/WEB-INF/pages/admin.jsp");
			}else {
				response.sendRedirect("index.jsp");
			}
		}
	}

}
