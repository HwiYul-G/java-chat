Azure Queue Storage

Azure Queue Stroageㄴ는 다량의 messages를 저장하는 서비스이다.
HTTP나 HTTPS를 사용해서 인증된 calls를 통해서 전세계 어디에서든 메시지에서 접근할 수 있다.
하나의 queue message는 64KB 크기까지 가능하다.

a queue는 수만개의 메시지를 포함한다.

queue는 흔하게 비동기적으로 backlog를 생성한다. 




## 참고자료
- [Azure Queue Storage](https://learn.microsoft.com/en-us/azure/storage/queues/storage-queues-introduction)

---
Treating warnings as errors because of process.env.CI=true

이 오류의 원인은?
2020년 6월 15일 부터, Netlify는 기본값을 true로 설정한 CI 환경 변수를 빌드 환경으로 추가하면서 점진적 배포를 시작했다.
이 환경 변수는 Travis CI나 github actions같은 CI 환경에 공통으로 셋팅되어 있다.

에코시스템은 이러한 환경 셋팅에 광범위하게 동의적으로 사용되어서 빌드가 CI 환경에서 실행될 때 감지된다.
로컬 개발 환경에선 감지되지 않는다.

이러한 셋팅은 많은 공통 라이브러리가 CI 환경을 감지하고 그에 따라 행동을 적절하게 바꾼다.
그러한 행동들 중 하나는 로컬 개발 터미널에서는 유용하지만 ()하 ㄴ스피너의 진행을 비활성화 하는 것이다.

이러한 몇 라이브러리들이 이전에 워닝이었던 것을 하드한 에러로 해석해서 build를 멈추게 한다. 
그 의도는 개발자에게 잠재적으로 깨진 설정을 배송하지 않게하한다는 것이다. 그러나 이것의 단점은 이전에 성공적으로 컴파일된것이 변화 후에는 실패한다는 것이다.

the fix
이 변화 후, 빌드가 안된다면, 빌드에서 CI 변수를 언셋팅하는 행동을 하면서 이 행동을 비활성화 할 수 있다.
예를 들어서 NPM command에서 CI를 언셋할 수 있다.
CI= npm run build

https://dev.to/chelsey0527/react-treating-warnings-as-errors-because-process-env-ci-true-3glh
https://dev.to/kapi1/solved-treating-warnings-as-errors-because-of-process-env-ci-true-bk5
https://vercel.com/guides/how-do-i-resolve-a-process-env-ci-true-error
