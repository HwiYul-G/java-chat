## 예외 처리 흐름
1. 예외 발생
   - controller에서 예외가 발생한다. 그 예외는 spring framework로 전달된다.
2. 예외 검색
   - spring framework는 `@RestControllerAdvice` 어노테이션된 클래스에서 적절한 예외 핸들러 메서드를 검색한다.
3. 핸들러 메서드 호출
   - 해당 예외를 처리할 수 있는 메서드가 호출된다.
- 결론적으로, `@RestControllerAdvice` 어노테이션이 붙은 클래스가 전역 예외 처리기로 작동한다.

## `@ResponseStatus` vs `StatusCode`
1. `@ResponseStatus`
    - Spring Framework 자체의 어노테이션으로, 메서드가 반환하는 HTTP 상태 코드를 상징한다.
2. `StatusCode`
    - 응답 결과 상태를 표현하기 위한 사용자 정의 클래스.
    - 클라이언트 애플리케이션에서 응답을 명확하게 처리하기 위해 사용한다.
