import axios from 'axios';
import { getStoredToken, clearAuthStorage } from '../storage/authStorage.js';

const axiosClient = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

function normalizeApiError(error) {
  if (!error.response) {
    return {
      status: null,
      message: 'Network error. Confirm the backend server is running and reachable.',
      details: null,
    };
  }

  const { status, data } = error.response;
  const fallbackMessages = {
    400: 'Bad request. Check the submitted data.',
    401: 'Your session is invalid or expired. Please log in again.',
    403: 'You do not have permission to perform this action.',
    404: 'The requested backend resource was not found.',
    409: 'This request conflicts with existing backend data.',
    422: 'The backend rejected the submitted data.',
    500: 'The backend server encountered an error.',
  };

  return {
    status,
    message: data?.message || fallbackMessages[status] || 'The backend request failed.',
    details: data?.errors || data?.details || null,
  };
}

axiosClient.interceptors.request.use((config) => {
  const token = getStoredToken();

  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }

  return config;
});

axiosClient.interceptors.response.use(
  (response) => response,
  (error) => {
    const normalizedError = normalizeApiError(error);

    if (normalizedError.status === 401) {
      clearAuthStorage();
    }

    return Promise.reject(normalizedError);
  }
);

export default axiosClient;
