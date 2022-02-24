# HUST_WEB
과거 실사용했던 HUST 홈페이지 코드입니다. 리팩토링을 경험해보기 위한 용도입니다.
아래의 방법,기준에 따라 리팩토링을 진행할 것입니다.
1. 각종 작업의 중복을 제거합니다. (로그인확인, csrf 방지 등)
2. 메서드가 null을 반환하지 않도록 합니다.
3. 메서드가 예외코드를 반환하는 것이 아닌, 예외를 투척하도록 합니다.
4. 메서드의 추상화수준을 한단계씩 내려가도록 합니다.
5. vo(DTO) 클래스들을 자료구조로 변경합니다(getter와 setter는 필요하지 않습니다.)
6. 객체의 필드 즉 상태를 철저히 숨기고, 책임이 올바른 객체에 부여되도록 수정합니다.
