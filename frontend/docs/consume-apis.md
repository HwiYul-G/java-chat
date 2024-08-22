# React에서 Server와 소통하기 위한 API 학습
- 목표: react application에서 CRUD를 위해서 APIs를 사용하는 방법 학습
- 학습 내용
    -  fetch API와 Axios API를 이용한 Restful APIs 사용 방법
    -  React hooks와 결합해 프론트 화면을 고려해 적절한 CRUD 하기
- 목차
    1. [REST API란?](#rest-api란)
    2. [React Hook - useEffect, useState](#react에서-api사용을-위해-알아야하는-hook)
    3. [Fetch API](#fetch-api)
    4. [Axios API](#axios-api)

## REST API란?
Representational State Transfer은 웹 서비스를 만들 때 사용되는 규약을 정한 아키텍처 스타일이다.

### 동작
1. Client가 web URL의 형식으로 서버에게 request를 보낸다.
2. HTML, XML, JSON, image 같은 자원이 서버에서 response로 날라온다.

### 메소드
- GET: 데이터를 read 한다.
- POST: 새로운 자원을 create한다.
- PUT: 멱등성(변화X)을 가진 update를 수행한다.
    - 정확히 그 값을 수정만 한다.
- PATCH: 멱등성(idempotent)을 가지지 않은 update를 수행한다.
    - 스팩 구현상 제약이 없으므로 1씩 증가하게 할 수도 있다.
- DELETE: URI가 정의한 자원을 delete한다.
### 상태코드
- 2xx: 요청이 성공적으로 이루어진다.
- 3xx: 리다이렉션
- 4xx: client 에러
- 5xx: 서버 오류

## React에서 API사용을 위해 알아야하는 hook
react component에서 requests가 행해지므로 js에서 APIs를 사용하는 것과 react에서 사용하는 것은 다르다.
### hook이란?
React의 Hooks은 함수 컴포넌트가 state나 다른 React features에 접근할 수 있게 한 것이다. hook덕분에 class component가 보통 필요없게 되었다.
- useState
- useEffect
- useContext
- useRef
- useReducer
- useCallback
- useMemo
- Custom Hooks

현재 API 사용을 위해 위의 8가지 hook에서 `useState`와 `useEffect`를 알아야 한다.

#### useEffect
- 쓰임: data fetch, DOM 직접 업데이트, 타이머
- 사용법: `useEffect(<fucntion>, <dependency>)`

```javascript
useEffect(() => {
    // No dependency
    // Runs on every render   
});
```
**useEffect는 기본적으로는 위와 같이 모든 렌더링에서 실행된다.** 즉 특정 화면의 변화로 렌더링이 발생하고 이로 인해 다른 효과가 트리거 된다.
이 부작용이 언제 실행되는 지 제어하면 편리하게 사용할 수 있다.

```javascript
// 빈 배열을 종속성으로 넣어 준 경우
useEffect(() => {
  //Runs only on the first render
}, []);
```

```javascript
useEffect(() => {
  //Runs on the first render
  //And any time any dependency value changes
}, [prop, state]);
```

**일부 효과는 메모리 누수를 줄이기 위해 정리가 필요하다.** 
정리가 필요한 효과로는 타임 아웃, 구독, 이벤트 리스너 및 기타 효과가 있다.
```javascript
import { useState, useEffect } from "react";

function Timer() {
  const [count, setCount] = useState(0);

  useEffect(() => {
    let timer = setTimeout(() => {
    setCount((count) => count + 1);
  }, 1000);
    // 타이머를 지워준다.
  return () => clearTimeout(timer)
  }, []); // 1. 빈 배열이므로 한 번만 수행됨

  return <h1>I've rendered {count} times!</h1>;
}
```

#### useState
- 효과: useState Hook을 사용해서 함수형 컴포넌트에서 상태를 추적할 수 있다. 
    - state는 앱에서 추적해야 하는 데이터나 속성을 의미한다.
```javascript
import { useState } from "react";

function FavoriteColor = () => {
    const [color, setColor] = useState("");
    // color라 이름 붙인 state를 읽을 때는 color를 사용한다.
    // 업데이트할 때는 setColor(값)을 이용한다.
};
```
객체를 보관하는 단일 hook
`.`을 이용해 접근한다.
```javascript
function Car(){
    const [car, setCar] = useState({
        brand: 'A',
        model: 'B',
        year: '1111',
        color: 'red'
    });

    return (
        <>
            <p>It is a {car.color} {car.model}</p>
        </>
    );
}
```
```javascript
// 객체 및 배열 업데이트 방법
function Car(){
    const [car, setCar] = useState({
        brand: "Ford",
        model: "Mustang",
        year: "1964",
        color: "red"
    });

    const updateColor = () => {
        setCar(prev => {
            // 이전과 같은 key를 가진 것이 있으면 후에 넣은 것으로 처리
            return {...prev, color: 'blue'};
        })
    };
    
    return (
        <>
        <button type='button' onClick = {updateColor}>Blue</button>
        </>
    );
};
```
- useState Hook
    우리가 데이터를 요청할 때, 우리는 state를 준비해야만한다.
    이 state에 저장된 데이터는 그것이 return될 때 저장되어져야 한다.
    우리는 redux같은 state 관리 툴이나 context object안에 data를 저장해야한다.
    간단하게 유지하기 위해서, 우리는 react local state에서 리턴된 데이터를 저장할 것이다.

# Fetch API와 Axios API
- 사용 API: JSONPlacehoder posts API `https://jsonplaceholder.typicode.com/posts `
- 사용 도구(가장 많이 사용하는 것 2가지)
    - Fetch API: 브라우저 내장 웹 API
    - Axios: promise 기반 HTTP client

## Fetch API
- fetch api는 js 내장 메소드이므로 의존성 및 패키지 설치 없이 사용가능
- `fetch()`는 path나 url같은 필수 인자가 필요하다.
- `fetch()`는 기본 응답으로 HTTP response를 주므로 json()으로 파싱해서 사용해야 한다.
- `fetch()`는 `Promise`를 리턴해서 `then()`, `catch()`를 이용해서 성공과 살패를 다룰 수 있다.
    - 이 경우 체이닝이 길어져서 불편할 수도 있다.
- `async/await`을 이용해서 체이닝이 길어지는 것을 가독성있게 할 수 있다.
    - 이때는 `try-catch`로 성공과 실패를 다룬다.
- `fetch('url주소', {method: "GET"});` 형식으로 이용한다.
### GET
- `get`의 경우 method 인자를 생략해도 된다. 기본값이 get으로 되어 있기 때문이다.
- 애플리케이션이 로드되자마자 posts라는 데이터가 패치되게 된다.
```javascript
const App = () => {
    // API로 부터 받은 데이터를 저장하기 위한 state
    // 이를 이용함으로써 나중에 데이터를 사용할 수 있음.
    const [posts, setPosts] = useState([]);
    
    useEffect(() => {
        fetch(url) // response object를 응답한다.
            .then((res) => res.json()) // json으로 변경해준다.
            .then((data) => {
                console.log(data);
                setPosts(data);
            })
            .catch((err) => {
                console.error(err.message);
            });
    }, []); // 기본적으로 빈 배열을 주어서 1번만 실행되게 설정

    // map을 통해서 반복적으로 소비한다.
    return (
   <div className="posts-container">
      {posts.map((post) => {
            <div className="post-card" key={post.id}>
               <h2 className="post-title">{post.title}</h2>
               <p className="post-body">{post.body}</p>
               <div className="button">
               <div className="delete-btn">Delete</div>
               </div>
            </div>
      })}
   </div>
   );
};
export default App;
```
### POST
```javascript
const addPosts = async (title, body) => {
    await fetch(주소, {
        method: 'POST', 
        body: JSON.stringify({ // 서버로 보낼 데이터, 직렬화를 해준다.
            title: title,
            body: body,
            userId: Math.random().toString(36).slice(2),
        }),
        headers: { // 보내는 데이터의 정보
            'Content-type': 'application/json; charset=UTF-8',
        }
    })
    .then((res) => res.json())
    .then((data) => {
        // UI도 업데이트 해주어야 하므로
        setPosts((posts) => [data, ...posts]);
        setTitle('');
        setBody('');
    })
    .catch((err) => {
        console.error(err.message);
    });
};

// 이를 버튼에 적용한다.
const handleSubmit = (e) => {
   e.preventDefault();
   addPosts(title, body);
};

return (
   <div className="app">
      <div className="add-post-container">
         <form onSubmit={handleSubmit}>
            <input type="text" className="form-control" value={title}
            // 이런식으로 event의 value를 넘겨주어서 사용해야한다.
               onChange={(e) => setTitle(e.target.value)}
            />
            <textarea name="" className="form-control" id="" cols="10" rows="8" 
               value={body} onChange={(e) => setBody(e.target.value)} 
            ></textarea>
            <button type="submit">Add Post</button>
         </form>
      </div>
      {/* ... */}
   </div>
);

```
### DELETE
```javascript
const deletePost = async(id) => {
    await fetch(주소, {
        method: 'DELETE',
    })
    .then((response) => {
        if(response.status === 200){
            // 반환된 데이터(id)를 전체 데이터에서 필터링 해준다.
            setPosts(
                posts.filter((post) => {
                    return post.id !== id;
                });
            );
        }else{
            return;
        }
    });
};
```
보통 버튼 클릭 이벤트를 통해서 발생한다.
```javascript
const App = () => {
// ...

   return (
   <div className="posts-container">
      {posts.map((post) => {
         return (
            <div className="post-card" key={post.id}>
               {/* ... */}
               <div className="button">
                  <div className="delete-btn" onClick={() => deletePost(post.id)}>
                     Delete
                  </div>
               </div>    
            </div>
         );
      })}
   </div>
   );
};

export default App;
```
### Async/Await를 사용해서 체이닝 탈출
- async 함수를 호출한다.
- request를 요청하고 response를 기대할 때 result를 가진 promise가 정착할 때까지 기다리는 함수 앞에 await을 붙인다.
```javascript
import React, {useState, useEffect} from 'react';

const App = () => {
    const [title, setTitle] = useState('');
    const [body, setBody] = useState('');
    const [posts, setPosts] = useState([]);

    // Get with fetch API
    useEffect(() => {
        const fetchPost = async () => {
            const response = await fetch(주소);
            const data = await response.json();
            console.log(data);
            setPosts(data);
        };
        fetchPost();
    }, []);

    // delete with fetchAPI
    const deletePost = async(id) => {
        let response = await fetch(
            주소, {
                method: 'DELETE',
            }
        );
        if(response.status === 200){
            setPosts(
                posts.filter((post) => {
                    return post.id !== id;
                })
            );
        }else{
            return;
        }
    };

    // Post with fetchAPI
    const addPosts = async(title, body) => {
        let response = await fetch(주소, {
            method: 'POST',
            body: JSON.stringfy({
                title: title,
                body: body,
                userId: Math.random().toString(36).slice(2),
            }),
            headers: {
                'Content-type': 'application/json; charset=UTF-8',
            },
        });

        let data = await response.json();
        setPosts((posts) => [data, ...posts]);
        setTitle('');
        setBody('');
    };

    const handleSubmit = (e) => {
        e.preventDefault();
        addPosts(title, body);
    };

    return (
        // ...
    );
};

export default App;
```
### Async/Await에서 try-catch로 에러 핸들링
```javascript
const fetchPost = async() => {
    try{
        const res = await fetch();
        const data = await res.json();
        setPosts(data);
    }catch(err){
        console.error(err);
    }
};
```

# Axios API
Axios는 비동기 HTTP 요청을 REST endpoints로 보내는 것을 단순환 promises에 기반을 둔 HTTP client 라이브러리이다.

## Axios 설치 및 설정
Fetch API와 달리 Axios는 내장된 라이브러리가 아니다.
### 설치
```
npm install axios
```
### 설정
- Axios를 설치하면 Axios instance를 만들 수 있다. 
- Axios instance를 만드는 것은 필수가 아니지만 불필요한 반복을 줄일 수 있으므로 만드는 것이 추천된다.
- instance를 만들기 위해서 `.create()` 메소드를 사용한다.
    - 이때 URL이나 headers 같은 정보를 명시할 수 있다.
```javascript
import axios from "axios";

const client = axios.create({
    baseURL: "https://localhost:8080"
});
```
## GET, POST, DELETE
- CRUD를 위해서 만들어둔 인스턴스를 사용한다.
- 해야할 일은 필요하다면 파라미터를 설정하는 것이다.
### GET
- get method는 기본적으로 JSON을 응답으로 받는다.
```javascript
useEffect(() => {
    client.get('?_limit=10').then((res) => {
        setPosts(res.data);
    });
}, []);
```
#### POST
```javascript
const addPosts = (title, body) => {
    client.post('', {
        title: title,
        body: body,
    })
    .then((res) => {
        setPosts((posts) => [res.data, ...posts]);
    });
};
```
### DELETE
- fetch API를 사용한 것과 마찬가지로 UI에서도 데이터를 지우기 위해서 filter를 한다.
```javascript
const deletePost = (id) => {
    client.delete(`${id}`);
    setPosts(posts.filter((post) => {
        return post.id !== id;
    }));
};
```
## Axios에서 Async/Await
- Async/Await을 사용함으로써 chaining을 줄이고 가독성을 높여보자.
```javascript
import React, { useState, useEffect } from 'react';

const App = () => {
   const [title, setTitle] = useState('');
   const [body, setBody] = useState('');
   const [posts, setPosts] = useState([]);

   // GET with Axios
   useEffect(() => {
      const fetchPost = async () => {
         let response = await client.get('?_limit=10'); // 응답을 await한다.
         setPosts(response.data);
      };
      fetchPost();
   }, []);

   // Delete with Axios
   const deletePost = async (id) => {
      await client.delete(`${id}`); // 응답을 await한다.
      setPosts(
         posts.filter((post) => {
            return post.id !== id;
         })
      );
   };

   // Post with Axios
   const addPosts = async (title, body) => {
      let response = await client.post('', { // 응답을 await해
         title: title,
         body: body,
      });
      setPosts((posts) => [response.data, ...posts]);
   };

   const handleSubmit = (e) => {
      e.preventDefault();
      addPosts(title, body);
   };

   return (
      // ...
   );
};

export default App;
```

## Axios에서 async/await을 try/catch로 오류 핸들링
- Axios requests도 promise 기반이므로 `.then()`과 `.catch()`를 사용해서 오류를 핸들링할 수 있다.
- async/awiat에는 `try...catch`를 사용할 수 있다.
```javascript
const fetchPost = async() => {
    try{
        let response = await client.get('?_limit=10');
        setPosts(response.data);
    }catch(err){
        console.error(err);
    }
};
``` 

# 참고자료
- https://www.geeksforgeeks.org/rest-api-introduction/
- https://aws.amazon.com/ko/what-is/restful-api/
- https://www.freecodecamp.org/news/how-to-consume-rest-apis-in-react/
- https://www.w3schools.com/react/react_useeffect.asp
- https://www.w3schools.com/react/react_usestate.asp