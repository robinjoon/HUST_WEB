// 모든 VO 클래스는 이 인터페이스를 구현한다.
package vo;

public interface VO {
	public String toJson(); // VO 객체를 json String 형태로 변환. 로그 시각화 등으로 활용 가능.
	
	public boolean isInvalid();
}
