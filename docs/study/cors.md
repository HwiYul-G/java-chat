## CORS(Cross-Origin Resource Sharing)
- CORS는 브라우저를 위한 프로토콜과 보안 표준으로 API 개발에 중요하다.
- 현대 브라우저는 XMLHttpRequest나 Fetch같은 APIs에서 CORS를 사용해서 cross-origin HTTP requests의 위험을 줄인다.
- 그 결과 CORS로 웹 사이트의 integrity(무결성)을 유지할 수 있다.

### What is CORS?
- CORS는 브라우저 메커니즘으로 주어진 도메인 밖에서 위치한 리소스에 통제된 접근을 제공한다.
- 통제된 접근을 제공하므로 `www.example.com`으로 정적 콘텐츠를 호스트할 수 있고, `api.example.com`으로 백엔드 api를 호스트할 수 있다.
- **이 경우에 하나의 도메인의 하나의 리소스가 다른 도메인의 리소스를 요청한다.**
  - 이 요청이 CORS request 이다.

- CORS request가 적절히 설정되고 구현되지 않으면, site가 잠재적인 cross-domain 공격에 노출될 수 있다.
  - CSRF(cross-site request forgery)같은 보안 위협에 직면할 수 있다.
  - 따라서, CORS 설정에 주의를 기울여야 한다.

### Cross-Origin Resource Sharing 동작법
1. CORS workflows에서, 하나의 origin에서 로드된 하나의 스크립트는 또 다른 origin에게 request를 시도한다. (CORS request)
2. 브라우저는 자동적으로 HTTP method OPTIONS을 활용해서 외부의 웹 서버(다른 도메인)에 preflight request를 한다.
   - preflight request는 몇 가지 headers를 가진다.
3. 외부의 웹 서버(요청 받은 서버)는 그 headers를 검증한다.
   - 검증을 통해, 특정 origin(요청 한 곳)으로부터의 scripts가 실제 요청을 하도록 허락한다.
4. 외부의 웹 서버는 지정된 요청 메소드와 커스텀 요청 헤더를 사용한다.
    - headers는 수용할 수 있는 origins와 requests methods의 범위를 정의한다.
    - headers는 쿠키나 authentication headers를 포함한 credentials을 보내는 것을 수용할 수 있다.
    - 이 외에도, headers는 브라우저들이 응답을 유지해야만 하는 시간을 정의하는 데 도움을 준다.

#### 결론
- 서버에서 A 도메인만 받고 싶은데 B 도메인에서 요청하는 것을 막기 위해 사용한다.
- 내가 받기로 한 A 도메인이 아니면 cors error가 발생한다.

## References
- https://blog.apilayer.com/what-is-cors-what-is-it-used-for/
- https://medium.com/@cybersphere/fetch-api-the-ultimate-guide-to-cors-and-no-cors-cbcef88d371e
