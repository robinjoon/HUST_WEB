package tools;

import java.util.ArrayList;
import java.util.StringTokenizer;

import service.MemberService;
import vo.MemberVO;

public class Tag {
	public static ArrayList<String> extraction(String content,int permission){
		ArrayList<String> list = new ArrayList<String>();
		StringTokenizer st = new StringTokenizer(content,",");
		while(st.hasMoreTokens()) {
			String id = st.nextToken();
			MemberVO member = MemberService.getMember(id);
			if(member==null) { // 없는 id를 태그시킨 경우
				continue;
			}
			if(member.getPermission()>=permission) { // 읽기권한이 있는 회원만 리스트에 추가
				list.add(id);
			}
		}
		return list;
	}
}
