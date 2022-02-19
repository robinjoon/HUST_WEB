package listener;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import tools.Sessions;
 
@WebListener

public class LoginUserCounter implements HttpSessionListener {

	public void sessionCreated(HttpSessionEvent se)  {
    	
    }
 
 
    public void sessionDestroyed(HttpSessionEvent se)  {
    	Sessions.removeInvalidSession(se.getSession().getId());
    }
     
}