## React Context
### Context 사용방법
#### context 만들기
- 내장된 팩토리 함수인 `createContext(default)`로 context instance를 만든다.
- 팩토리 함수는 default value라는 오직 하나의 선택적 인자만 받는다.
```javascript
import { createContext } from 'react';

export const Context = createContext('Default Value');
```
#### context 제공하기
- `ContextProvider` 컴포넌트는 context를 자신의 하위 컴포넌트로 제공하는 데 사용된다.
- context에 값을 셋팅하기 위해서 `<Context.Provider value={value}/>` 위에 `value` props가 이용가능하다.
```javascript
import {Context} from './context';

function Main(){
    const value = 'My Context Value';
    return (
        <Context.Provider value={value}>
            <MyComponent/>
        </Context.Provider>
    );
}
```
#### context 사용하기
1. `useContext(Context)` react hook 사용하기
```javascript
```
2. `Context.Consumer`라는 특별한 컴포넌트에 child로 render function을 사용하는 것
### context의 필요성
### 사용예시: global user name
#### 
#### context가 변화할 때
### context 업데이트하기
### 결론
### 참고자료
- https://dmitripavlutin.com/react-context-and-usecontext/