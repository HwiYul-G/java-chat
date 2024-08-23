# React Route
- 목표: SPA(Single Page Application)을 위해 React Route 사용하는 방법 배우기
- 학습 내용
    - React Router DOM의
React application에서 구현하면서 React Router DOM의 핵심 개념을 탐험할 것이다.
우리의 초점은 다른 컴포넌트로 연결을 가진 간단한 네비게이션 시스템을 구축하는 것이다.
그리고 이것은 route를 configure하는 법 route matching을 다루는 방법 navigation 구현을 보여준다.

이 아티클의 끝에, 너는 React Router DOM을 사용하는 방법을 단단하게 이해해서 동적이고 군더더기 없는 네비게이션 경험을 만들 수 있을 것이다.


- 목차
    1. [SPA란?](#spa란)
    2. [React Router](#react-router)
    3. [React Router DOM의 핵심 개념과 구성](#react-router-dom의-핵심-개념과-구성)
        - BroswerRouter
        - Routes
        - Route
        - Undeclared Route
        - Nested Routes
        - Link
        - NavLink
        - Outlet
    4. [hook - `useNavigate`, `useParams`](#hook---usenavigate-useparams)

## SPA란?
SPA는 웹 서버로부터 새로운 데이터를 가진 현재의 웹 페이지를 동적으로 재작성하는 웹 사이트나 웹 애플리케이션이다.

Single Page Application 이전의 전통적인 웹 페이지는 전체 새로운 페이지를 로딩했다.
SPA는 각 클릭에서 필요한 것만을 보내주고, 브라우저는 그 부분만을 렌더링한다.
이로 인해 클라이언트 메소드는 더 빠르게 로딩할 수 있고, 서버도 보내는 정보의 양이 줄어서 서로에게 win-win할 수 있게 되었다.

### SPA의 장점
content와 data를 분리하는 SPA로 어떤 장점이 있을까?
1. HTML, CSS, JS 각각의 단일 파일 로드
    - 초기 페이지 로드 후 서버는 더이상 HTML을 보내지 않는다.
    - 클릭 등 이벤트로 SPA는 데이터와 마크업 요청을 보내고 서버는 필요한 것만 보낸다.
    - 전체를 고치지 않으므로 빠르게 상호 교환할 수 있다.
2. 서버로 추가적인 query가 없다.
    - 서버가 여러 렌더링을 하려고 많은 시간과 에너지를 가질 필요가 없다.
    - 서버에 영향을 적게 주어서 같은 트레픽에 대한 서버의 비용이 감소한다.
3. 프론트 엔드 빌딩이 빠르고 반응적이다.
    - 사용자가 사용하는 모양은 변경될 수 있지만 백엔드 내용은 많이 변경되지 않는다.
    - SPA의 분리된 아키텍처로 백엔드와 프론트를 독립적으로 사용할 수 있다.
    - 이를 위해 API를 사용해서 프론트의 변화를 빠르게 줄 수 있다.
4. 사용자 경험을 향상시킨다.

## React Router
React Router는 React app에서 navigation과 routing을 관리하는 라이브러리이다.
### 관련 패키지
- React router: 경로 매칭 알고리즘과 hook +  react router의 핵심 기능 
- React Router DOM: react-router + 몇 가지 DOM 관련 API
- react-router-native: react-router + react native 전용 API
### 설치
현재는 웹에서 수행할 것이므로 아래를 설치한다.
`npm install react-router-dom`

## React Router DOM의 핵심 개념과 구성
### BrowserRouter
- 모든 route components를 가지는 parent componet
- app의 모든 routes는 `BrowserRoute` 안에서 선형되어야 한다.
- `BrowserRouter`는 브라우저의 주소 바에서 네비게이션 동안 사용할 URLs로 현재의 위치를 저장한다.
- `BrowserRoute`는 `basename` attribute가 있다.
    - 하나의 앱에서 모든 routes를 위한 base URL을 셋팅하는데 사용한다.
    - 이를 이용해서 앱이 도메인의 하위 디렉토리에서 호스팅될 때 중요하다.
```javascript
import {BrowserRouter} from "react-router-dom";

function App(){
    // `/math`를 통해서 모든 하위 도메인이 math와 관련있음을 알 수 있다.
    return (
        <BrowserRouter basename='/math'> 
        </BrowserRouter>
    );
};

export default App;
```
### Routes
- `routes`는 최초로 매칭된 child route를 렌더링하고 부모처럼 행동한다.
- `routes`는 현재 URl 기반으로 올바른 컴포넌트를 보여주는 것을 보장한다.
```javascript
import {BrowserRouter, routes} from "react-router-dom";

funciton App() {
    return (
        <BrowserRouter>
            <Routes>
            </Routes>
        </BrowserRouter>
    );
}
export default App;
```
### Route
- `Route`는 `Routes`의 child component이다. 반드시 `routes` component 안에 선언되야 한다.
- `Route`는 `path`와 `element`라는 2개의 attributes를 가진다.
    - `path`: 명시된 path 이름
    - `element`: 렌더링 되어야 하는 component
- 하나의 `route`는 특정한 `path`와 매치되는 URL에 도달했을 때 특정한 컴포넌트를 렌더링한다.
```javascript
import { BrowserRouter, Routes, Route } from "react-router-dom";

function App() {

  return (
    <BrowserRouter>
        <Routes>
            <Route path="/" element={<Home/>}/>
            <Route path="pricing" element={<Pricing/>}/>
        </Routes>
    </BrowserRouter>
  );
}

export default App;
```
### Undeclared Route
- Error 404 page같은 존재하지 않는 `routes`를 다룸
- Not Found Message를 담은 component를 만들고 `path`의 이름을 `*`으로 만들어 route로 추가한다.
```javascript
import { BrowserRouter, Routes, Route } from "react-router-dom";

function App() {
  return (
    <BrowserRouter>
        <Routes>
            <Route path="/" element={<Home/>}/>
            <Route path="pricing" element={<Pricing/>}/>
            <Route path="*" element={<PageNotFound/>}/>
        </Routes>
    </BrowserRouter>
  );
}
export default App;
```
### Nested Routes
- `routes`는 children이나 sub-routes를 가질 수도 있다.
```javascript
import {Browser, Routes, Route } from "react-router-dom";

function App(){
    return (
        <BrowserRouter>
            <Routes>
                <Route path="/" element={<Home/>}/>
                <Route path="pricing" element={<Pricing/>}/>
                {
                    // sub-routes를 가진 경우
                    // 각 경로는 `/categories/male`와 `/categories/female`이다.
                }
                <Route path="categories" element={<Categories/>}>
                    <Route path="male" element={<Male/>}/>
                    <Route path="female" element={<Female/>}/>
                </Route>
                <Route path="*" element={<PageNotFound/>}/>
            </Routes>
        </BrowserRouter>
    );
}
export default App;
```
### Link
- `Link`는 `href` attribute처럼 행동한다.
- `to` attribute를 가진다.
    - `to`에는 component's page의 path가 전달된다. 
    - 보통 클릭 이벤트로 
- `Links`는 보통 Navbar componet 안에 있다.
- 이미 선언한 `Routes` 안에 componets path를 가리키는 2개의 Link를 넣는다.
```javascript
import {Link} from "react-router-dom";
export default function PageNav(){
    return (
        <>
            <Link to="/">Home</Link>
            <Link to="pricing">Pricing</Link>
        </>
    );
}
```
- **이때, PageNav component는 App.jsx 안에 위치되어야 한다.**
- 정확히 `BrowserRouter`의 opening 직후와 `Routes` 시작 전에 있어야 한다.
    - 다른 컴포넌트를 통해서 라우팅 되어도 PageNav가 항상 navigation menu의 top에 있는 것을 보장한다.
```javascript
import { BrowserRouter, Routes, Route } from "react-router-dom";
function App(){
    return (
        <BrowserRouter>
            {
                // 여기에 PageNav가 위치한다. 
            }
            <PageNav/> 
            <Routes>
                <Route path="/" element={<Home/>}/>
                <Route path="pricing" element={<Pricing/>}/>
                <Route path="categories" element={<Categories/>}>
                    <Route path="male" element={<Male/>}/>
                    <Route path="female" element={<Female/>}/>
                </Route>
                <Route path="*" element={<PageNotFound/>}/>
            </Routes>
        </BrowserRouter>
    );
}
export default App;
```

### NavLink
- NavLink는 Link와 같은 일을 한다. Link처럼 `to` attribute를 가진다.
- NavLink는 class attribute를 가지므로 Link와 다르다.
    - class attribute는 `active`, `isPending`, `isTransitioning`이다.
    - 이를 통해서 사용자와 상호작용동안 style을 바꾸는 등 Link보다 더 다양하게 처리할 수 있다.
```javascript
import { NavLink } from "react-router-dom";
export default function PageNav() {
  return (
    <>
        <NavLink to="/">Home</NavLink>
        <NavLink to="pricing">Pricing</NavLink>
    </>
  );
  }
```
### Outlet
- parent route element안에 child elements를 가지는 것은 child routes UI를 렌더링하는 추상층이 필요하다.
- 이것이 `Outlet` 컴포넌트가 있는 이유이다.
- 자식 경로의 컴포넌트가 Outlet 위치에 렌더링 된다.
```javascript
import { NavLink, Outlet } from "react-router-dom";
export default function Categories() {
  return (
    <>
        <NavLink to="men">Men</NavLink>
        <NavLink to="women">Women</NavLink>
        <Outlet />
    </>
  );
}
```


## hook - `useNavigate`, `useParams`
### `useNavigate` Hook
- 이 hook은 routes 사이를 프로그래밍적으로 navigation하게 하는 함수를 리턴한다.
- 사용 방법
    1. `onClick` prop을 통해서 버튼에 부착한다.
    2. `Link` component와 함께 사용한다.
    3. component path가 아니라 number를 사용한다.
        - number는 간 곳의 history stack에서 backward의 수를 의미한다.
        - `-1`의 수를 주로 사용.
#### 방법1. `onClick` prop을 통해 버튼에 부착한다.
```javascript
<button onClick={() => navigate("/categories")}>버튼</button>
```
#### 방법2. `Link` component와 함께 사용한다.
```javascript
<Link to={navigate("/categoreis")}>버튼</Link>
```
#### 방법3. component path가 아니라 number를 사용한다.
```javascript
<Link to={navigate(-1)}>Go one step backwards</Link>
```

### `useParams` Hook
- Route의 path가 매칭된 현재 URL에서 동적인 `params` object를 얻는다.
- 부모 route는 그들의 child routes에 모든 `params`를 전달한다.
```javascript
import { useParams } from "react-router-dom";

function App() {
  const {id} = useParams()
  return (
    <BrowserRouter>
        <Routes>
            <Route path="customer">
                <Route path=":id" element={<OrderPage/>}/>
            </Route>
        </Routes>
    </BrowserRouter>
  );
}

export default App;
```

## 참고자료
- https://www.bloomreach.com/en/blog/what-is-a-single-page-application
- https://www.freecodecamp.org/news/use-react-router-to-build-single-page-applications/