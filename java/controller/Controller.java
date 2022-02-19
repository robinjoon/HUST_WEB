// 컨트롤러의 구조를 정의하는 인터페이스. FrontController를 제외한 모든 컨트롤러는 이 인터페이스를 상속받는다
package controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface Controller {
	public void execute(HttpServletRequest request, HttpServletResponse response, String action) throws ServletException,IOException;
	// action 은 각각의 컨트롤러가 담당하는 여러 역할 중 특정 하나의 역할을 지정하는 파라미터다.
}
