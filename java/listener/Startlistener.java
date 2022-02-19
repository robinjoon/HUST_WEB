package listener;

import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import dao.ViewlogDAO;

@WebListener

public class Startlistener implements ServletContextListener {
	Timer timer = new Timer();
	TimerTask t_task = new TimerTask() {
		
		@Override
		public void run() {
			if(ViewlogDAO.getinstance().deletelogs()) {
				System.out.println("삭제완료");
			}
		}
	};
	public void contextInitialized(ServletContextEvent sce) throws RuntimeException{
        ServletContext context = sce.getServletContext();
        System.out.println("!!!!!웹 어플리케이션 시작!!!!!");
        System.out.println("!!!!!조회수 자동삭제 타이머 시작!!!!!");

        System.out.println("서버 정보 : " + context.getServerInfo());
        timer.schedule(t_task, 0, 600000);
    }
    
    public void contextDestroyed(ServletContextEvent sce)  {
        System.out.println("!!!!!웹 어플리케이션 종료!!!!!");
        System.out.println("!!!!!조회수 자동삭제 타이머 종료!!!!!");
        timer.cancel();
    }


}
