# React Context
- 목표: SPA(Single Page Application)을 위해 React Route 사용하는 방법 배우기
- 학습 내용
    - react context와 prop threading
    - react context API 사용 방법 배우기
- 목차
    1. [React Context란?](#react-context란)
        - Prop Drilling
        - React Context usecase
    2. [react context API 사용방법](#react-context-api를-사용하는-방법)
    3. [react context의 Theme-Switching](#react-context의-theme-switching)

## React Context란?
- parent와 child components 사이에서 데이터를 관리할 때 `props`를 사용해서 parent에서 child로 data를 전달한다.
- props는 데이터를 parent에서 child라는 방향으로만 전달 할 수 있다.
    - 이 상황이 매우 깊어지는 것을 `Prop Drilling`이라 한다.
- parent element에서 state 변화가 발생했을 때 react는 그 values를 가진 모든 components를 다시 랜더링한다.
**Context를 통해서 매 층마다 props를 전달하지 않고 component tree를 통해서 data를 전달하는 방법이 있다.**이 결과로 프로젝트의 모든 컴포넌트에서 global storage로 행동하게 된다.

### Prop Drilling
- props는 데이터를 parent에서 child로 전달할 수 있고 이 상황이 깊어져 복잡해 진 것을 `Prop Drilling`이라 한다.
- 데이터가 여러 층을 통해서 전달되므로 특정 컴포넌트가 그 데이터를 사용하지 않아도 받아야 한다.
#### 간단한 예시
- 아래 코드의 props가 전달되는 방식은 아래와 같다. 
    - ParentComponent -> ChildComponent -> GrandChildComponent로 넘어간다.
- ParentComponent는 top-level component로 `userData`라는 데이터를 가진다.
- ChildComponent는 ParentComponent의 child이고 dataFromParent로 prop를 받는다.
    - 이는 GrandChildComponent를 렌더링하고 그 값으로 위에서 받은 prop을 전달한다.
- GrandChildComponent는 깊게 중첩되어지고 dataFromChild로부터 prop을 받는다.
    - 여기서 data를 렌더링한다.

```javascript
import React, { useState } from 'react';

const GrandchildComponent = ({ dataFromChild }) => {
  return (
    <div>
      <p>Username in Grandchild: {dataFromChild.username}</p>
    </div>
  );
};

const ChildComponent = ({ dataFromParent }) => {
  return (
    <div>
      <p>Your email: {dataFromParent.email}</p>
      <GrandchildComponent dataFromChild={dataFromParent} />
    </div>
  );
};

const ParentComponent = () => {
  const [userData, setUserData] = useState({
    username: 'benny_dicta',
    email: 'benedictaonyebuchi@gmail.com',
  });

  return (
    <div>
      <h1>Welcome, {userData.username}!</h1>
      <ChildComponent dataFromParent={userData} />
    </div>
  );
};

const App = () => {
  return <ParentComponent />;
};

export default App;
```

### React Context를 사용하는 경우
1. prop drilling이 복잡해질 때(multi-level nesting될 때)
    - 간단하고 직관적인 프로젝트에선 `prop`를 전달하는 것이 적절하다.
    - 여러 컴포넌트 층을 통해 `prop`를 전달해야 하는 경우 context API가 유리하다.
2. global data가 필요할 때
    - user authentication status, theme preferences 등

## React Context API를 사용
### react context api의 구성: Provide, Context, Consumer
1. Provider
    - context value로 접근하기 위해서 components를 cover하는 컴포넌트
    - 컴포넌트 트리 체계에ㅓ value prop을 사용해서 공유하길 원하는 values를 전달하는 곳
2. Context
    - 데이터가 저장되는 저장소처럼 행동
    - `createContext()`: global object를 생성해서 context를 만든다.(context를 만들기 위해서 사용)
    - `useContext()`: provider가 이용가능하게 만들어준 정보를 사용한다. (component에서 가져오기 위해 사용)
3. Consumer
    - component 안에서 공유된 데이터를 쓰는데 사용
    - context change를 구독하고 shared value에 접근한다.
    - Provider는 데이터로 components를 먼저 렌더링해서 consumer는 provider 내부에서 중첩되어있다.
    - `useContext`를 사용하기 전에 존재한다.

### 사용 방법
1. `createContext()`를 사용해서 Context를 만든다.
2. Context Provider로 App을 감싼다.
3. `useContext`로 Components에서 context를 사용한다.
#### STEP1: `createContext()`를 사용해서 Context를 만든다.
```javascript
import { createContext } from 'react';
const MyContext = createContext();
export default MyContext;
```
#### STEP2: Context Provider로 App을 감싼다.
```javascript
import React from 'react';
import MyContext from './MyContext';

const App = () => {
    const sharedValue = 'shared value';

    return (
        <MyContext.Provider value={sharedValue}>
            {/* 공유할 components를 여기에 둔다. */}
        </MyContext.Provider>
    );
};

export default App;
```
#### STEP3: `useContext`로 Components에서 context를 사용한다.
```javascript
import React, { useContext } from 'react';
import MyContext from './MyContext';

const MyComponent = () => {
    const sharedValue = useContext(MyContext);

    return (
        <p>{sharedValue}</p>
    );
};

export default MyComponent;
```

## react context의 Theme-Switching
1. `src`에 `context` folder를 만들고 `ThemeContext.js` 라는 새로운 파일을 생성한다.
    ```javascript
    import React, { createConetxt, useContext, useState } from 'react';

    // context 생성
    const ThemeContext = createContext();

    // ThemeProvider component 생성
    const ThemeProvider = ({ children} ) => {
        // useState hook 사용해서 현재 theme을 관리
        const [theme, setTheme] = useState('light');

        const toggleTheme = () => {
            setTheme((prev) => (prev === 'light' ? 'dark' : 'light'));
        };

        // 모든 children 컴포넌트에서 전달 가능하도록 props를 Provider로 제공한다. 
        return (
            <ThemeContext.Provider value={{ theme, toggleTheme}}>
                {children}
            </ThemeContext.Provider>
        );
    };

    const useTheme = () => {
        return useContext(ThemeContext);
    };

    export { ThemeProvider, useTheme };
    ```
2. `src`에 `ThemedComponent.js`를 만든다.
    ```javascript
    import React from 'react';
    import { useTheme } from './context/ThemeContext';

    const ThemedComponent = () => {
        const { theme, toggleTheme } = useTheme();

        return (
            <div style={
                { background: theme === 'light' ? '#fff' : '#333', 
            color: theme === 'light'? '#333' : '#fff' }}>
                <h2>Themed Component</h2>
                <p> Current Theme: {theme} </p>
                <button onClick={toggleTheme}>Toggle Theme</button>
            </div>
        );
    };

    export default ThemedComponent;
    ```
3. `App.js`에 Provider와 component를 추가한다.
    ```javascript
    import React from 'react';
    import { ThemeProvider } from './context/ThemeContext';
    import ThemedComponent from './ThemedComponent.js';

    const App = () => {
        return (
            <ThemeProvider>
                <div>
                    <h1>Themed App</h1>
                    <ThemedComponent />
                </div>
            </ThemeProvider>
        );
    };

    export default App;
    ```

## 참고자료
- https://www.telerik.com/blogs/react-basics-how-when-use-react-context
- https://www.freecodecamp.org/news/how-to-use-react-context/