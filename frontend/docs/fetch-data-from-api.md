```javascript
    fetch(url)
    .then((res) => res.json())
    .then((data) =>{
        console.log(data);
    });
```
`npm install axios`

```javascript
import Axios from "axios";

Axios.get(url).then((res)=> {
    setCatFact(res.data.fact); // 간단해짐
});
```

```javascript
import Axios from "axios";

const [catFact, setCatFact] = useState("");

function App(){
    const [catFact, setCatFact] = useState("");

    const fetchCatFact = () => {
        Axios.get(url).then((res)=> {
            setCatFact(res.data.fact);
        });
    };

    useEffect(() => {
        fetchCatFact();
    }, []);

    return (
        <div className="App">
            <button onClick={fetchCatFact}> Generate Cat Fact </button>
            <p> {catFact} </p>
        </div>
    );
}
```

```javascript
import "./App.css";
import Axios from "axios";

function App() {
    const [name, setName] = useState("");
    const fetchData = () => {
        Axios.get("");
    };

    return ();
}
```