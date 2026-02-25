import axios from 'axios';

const client = axios.create({
  baseURL: 'https://autoflex-kia6.onrender.com/api',
  headers: { 'Content-Type': 'application/json' },
});

client.interceptors.response.use(
  (response) => {
    return response;
  },
  (error) => {
    // Check if the backend sent a specific text or JSON error payload.
    if (error.response && error.response.data) {
      const backendMessage = error.response.data.message || error.response.data.error || (typeof error.response.data === 'string' ? error.response.data : null);
      if (backendMessage) {
        return Promise.reject(new Error(backendMessage));
      }
    }

    // Fallback to standard Axios message
    return Promise.reject(error);
  }
);

export default client;
