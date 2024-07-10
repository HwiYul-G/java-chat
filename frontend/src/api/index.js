import axios from "axios";

const baseURL = `${process.env.REACT_APP_SERVER_DEV_HOST}`;

const instance = axios.create({
    baseURL: baseURL,
    headers: {
        'Content-Type': 'application/json',
      },
});

export default instance;