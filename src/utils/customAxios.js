//This file sends the jwt token on every http call to the Backend
//Also sets up our backend basse url

import axios from 'axios';
import AsyncStorage from '@react-native-async-storage/async-storage';


// Create an Axios instance
const customAxios = axios.create({
   // baseURL: 'https://your-api-url.com', // Replace with your backend URL
});

// Add a request interceptor
customAxios.interceptors.request.use(
    async (config) => {
        // Retrieve the token from AsyncStorage
        const token = await AsyncStorage.getItem('jwtToken');
        if (token) {
            // Set the Authorization header
            config.headers.Authorization = `Bearer ${token}`;
        }
        return config;
    },
    (error) => {
        // Handle request errors
        return Promise.reject(error);
    }
);

export default customAxios;