## Context API가 동작하는 방법
Context API는 매 레벨마다 수동적으로 props를 전달하지 않고 컴포넌트 트리를 통해서 데이터가 전달되게 한다.
context api는 components 사이에서 data를 공유하기 쉽게 만든다.

예를 들어, 사용자의 쇼핑 카트를 보여주는 컴포넌트를 ...

## context api를 사용하는 방법
### 1. create a context object
- `createContext` function으로 context object를 만든다.
- 이 context object는 애플리케이션 전체에 걸쳐서 공유하고 싶은 데이터를 담는다.
```javascript
// MyContext.js
import { createContext } from 'react';

export const MyContext = createContext('');
```
### 2. wrap components with a provider
context object를 만들었다면, 공유된 데이터로 접글할 필요가 잇는 components를 provider component로 wrap할 필요가 있다.
provider component는 공유할 데이터를 담는 'value' prop를 받아들인다. 그리고 프로바이더 컴포넌트의 child인 컴포넌트들은 공유된 데이터에 접근할 숭 ㅣㅆ다.

```javascript
// Provider로 child components로 감싼 parent component를 만든다.
import {useState, React} from "react";
import {MyContext} from "./MyContext";
import MyComponent from "./MyComponent";

function App(){ // parent component
    const [text, setText] = useState("");

    return (
        <div>
            {/* provider component로 childrens를 감싼다
                그 하위로 
            */}
            <MyContext.Provider value={{text, setText}}>
                <MyComponent/>
            </MyContext.Provider>
        </div>
    );
}
```
### 3. consume the context
```javascript
function MyComponent(){
    const {text, setText} = useContext(MyContext);

    return (
        <div>
            <h1>{text}</h1>
            <button onClick={() => setText('Hello, world!')}>
                Click me
            </button>
        </div>
    );
}

export default MyComponent;
```

## 참고 자료
- https://www.freecodecamp.org/news/context-api-in-react/