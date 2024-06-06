

### `@DirtiesContext`
- 이 어노테이션은 관련된 테스트나 클래스가 ApplicationContext를 수정함을 말한다.
- 이후에 진행될 테스트를 위해서 테스트 프레임워크가 context를 닫고 다시 만들게 한다.
- class 전체 or 테스트 method에 어노테이션할 수 있다.
  - spring이 컨텍스트를 닫는 것을 제어할 수 있다.
- 예시
  - add를 테스트 하기 위해서 해당 사용자를 추가했다.
  - 하지만 실제로 들어가 있으면 다음 테스트에서 영향을 미친다.
  - 이럴 때 add를 테스트 한 함수에 어노테이션을 붙이면, add 테스트 후 초기화(컨텍스트 재로드)를 한다.
  - 뒤에 사용할 때는 add가 추가되어 있지 않아서 영향을 주지 않고 테스트를 진행할 수 있다.


#### 참고자료
- https://www.baeldung.com/spring-dirtiescontext