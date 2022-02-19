package websockets;

import java.util.ArrayList;
import java.util.StringTokenizer;

import javax.servlet.http.HttpSession;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.server.ServerEndpoint;

import service.MemberService;
import tools.Sessions;

// WebSocket의 호스트 주소 설정
@ServerEndpoint("/idcheck")
public class IdcheckSocket {
// WebSocket으로 브라우저가 접속하면 요청되는 함수
	@OnOpen
	public void handleOpen() {
// 콘솔에 접속 로그를 출력한다.
		System.out.println("client is now connected...");
	}

// WebSocket으로 메시지가 오면 요청되는 함수
	@OnMessage
	public String handleMessage(String message) { 
		// 메세지에 신규가입하려는 id가 전송된다.
		System.out.println(message);
		try{
			if(!message.matches("[0-9|a-z|A-Z|ㄱ-ㅎ|ㅏ-ㅣ|가-힝]*")){
				return "false"; // id에 특문 포함됨.
			}else{
				String ret = String.valueOf(MemberService.id_duplicate_check(message));
				return ret;
			}
		}catch(Exception e) {
			return "false";
		}
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
