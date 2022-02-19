package websockets;

import java.util.ArrayList;
import java.util.StringTokenizer;

import javax.servlet.http.HttpSession;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.server.ServerEndpoint;

import tools.Sessions;

// WebSocket의 호스트 주소 설정
@ServerEndpoint("/loginlist")
public class LoginListSocket {
// WebSocket으로 브라우저가 접속하면 요청되는 함수
	@OnOpen
	public void handleOpen() {
// 콘솔에 접속 로그를 출력한다.
		System.out.println("client is now connected...");
	}

// WebSocket으로 메시지가 오면 요청되는 함수
	@OnMessage
	public String handleMessage(String message) { 
		// 메세지에 요청을 보낸 사람의 id와 csrf_token이 보내짐. 세션목록에 있는 모든 세션중 두 값이 일치하고 권한이 운영진이상인 경우 로그인된 회원정보를 보내줌. 
		System.out.println(message);
		try{
			StringTokenizer st = new StringTokenizer(message," ");
			String sid = st.nextToken();
			String csrf_token = st.nextToken();
			st = new StringTokenizer(csrf_token,":");
			csrf_token = st.nextToken();
			csrf_token = st.nextToken();
			System.out.println(sid);
			System.out.println(csrf_token);
			String replymessage="";
			ArrayList<HttpSession> sessions = Sessions.getSessions();
			for(int i=0;i<sessions.size();i++) {
				String id = (String)sessions.get(i).getAttribute("id");
				String token = (String)sessions.get(i).getAttribute("csrf_token");
				int permission = (int)sessions.get(i).getAttribute("permission");
				if(sid.contentEquals(id)&&csrf_token.contentEquals(token)&&permission>=4) {
					for(int j=0;j<sessions.size();j++) {
						String mid = (String)sessions.get(j).getAttribute("id");
						int m_permission = (int)sessions.get(j).getAttribute("permission");
						replymessage += "<a class=\"list-group-item list-group-item-action\" "
								+ "href=\"getmember.do?member="+mid+"\">"+mid+"("+m_permission+")"
								+ "</a>";
	
					}
					System.out.println(replymessage);
					return replymessage;
				}
			}
		}catch(Exception e) {
			return "권한이 없습니다.";
		}
		return "권한이 없습니다.";

	}

// WebSocket과 브라우저가 접속이 끊기면 요청되는 함수
	@OnClose
	public void handleClose() {
// 콘솔에 접속 끊김 로그를 출력한다.
		System.out.println("client is now disconnected...");
	}

// WebSocket과 브라우저 간에 통신 에러가 발생하면 요청되는 함수.
	@OnError
	public void handleError(Throwable t) {
// 콘솔에 에러를 표시한다.
		t.printStackTrace();
	}
}
