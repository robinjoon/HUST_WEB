package tools;
import java.io.IOException;

import javax.servlet.*;
import javax.servlet.http.*;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class HttpUtil {
	private HttpServletRequest req;
	private HttpServletResponse rep;
	private String path;
	private boolean redirect;
	
	public void response() throws IOException {
		if(redirect) {
			sendRedirect();
		}else {
			forward();
		}
	}
	
	private void sendRedirect() throws IOException {
		rep.sendRedirect(path);
	}
	
	private void forward() {
		try {
			RequestDispatcher dispatcher = req.getRequestDispatcher(path);
			dispatcher.forward(req, rep);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void forward(HttpServletRequest request, HttpServletResponse response, String path) {
		
		try {
			RequestDispatcher dispatcher = request.getRequestDispatcher(path);
			dispatcher.forward(request, response);
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	public static void forward(HttpServletRequest request, HttpServletResponse response, String path,Object obj) {
		
		try {
			request.setAttribute("data", obj);
			RequestDispatcher dispatcher = request.getRequestDispatcher(path);
			dispatcher.forward(request, response);
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
}
