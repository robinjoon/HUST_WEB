package controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import tools.HttpUtil;

public class MappingController implements Controller {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response, String action)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
			if(action.contentEquals("adminpage")) {
				Integer permission =  (Integer)request.getSession().getAttribute("permission");
				if(permission!=null) {
					if(permission>=4) {
						HttpUtil.forward(request, response, "/WEB-INF/pages/admin.jsp");
					}else {
						response.sendRedirect("index.jsp");
					}
				}else {
					response.sendRedirect("index.jsp");
				}
			}
	}

}
