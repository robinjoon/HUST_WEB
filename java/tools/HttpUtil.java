package tools;
import javax.servlet.*;
import javax.servlet.http.*;
public class HttpUtil {
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
