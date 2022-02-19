package tools;
import java.util.*;

import javax.servlet.http.HttpSession;
public class Sessions {
	private static ArrayList<HttpSession> sessions= new ArrayList<>();
	
	public static ArrayList<HttpSession> getSessions() {
		return sessions;
	}
	
	public static void addSession(HttpSession session) {
		sessions.add(session);
	}
	
	public static void removeSession(HttpSession session) {
		sessions.remove(session);
	}
	
	public static void removeInvalidSession(String sessionID) {
		for(int i=0;i<sessions.size();i++) {
			if(sessions.get(i).getId().contentEquals(sessionID)) {
				sessions.remove(i);
			}
		}
	}
}
