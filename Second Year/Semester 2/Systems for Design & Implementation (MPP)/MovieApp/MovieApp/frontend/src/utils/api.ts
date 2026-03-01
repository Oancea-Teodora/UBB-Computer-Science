// API Configuration for MovieApp
// Environment: Production deployment to Render
// Backend URL comes from VITE_API_URL environment variable

// API configuration and utility functions
import { useAuth } from '../contexts/AuthContext';

// Get API URL from environment variable or fallback to localhost for development
export const API_URL = import.meta.env.VITE_API_URL || 'http://localhost:3001';

// List of endpoints that don't require authentication
const PUBLIC_ENDPOINTS = [
    '/auth/login',
    '/auth/register',
    '/health',
    '/directors/public',
    '/movies/public'
];

// Custom fetch function with authentication and error handling
export const apiFetch = async <T = any>(
    endpoint: string,
    options: RequestInit = {}
): Promise<T> => {
    try {
        // Get the stored token from localStorage
        const token = localStorage.getItem('token');

        console.log('=== apiFetch called ===');
        console.log('Endpoint:', endpoint);
        console.log('Method:', options.method || 'GET');
        console.log('Token exists:', !!token);
        console.log('Token (first 20 chars):', token ? token.substring(0, 20) + '...' : 'null');

        // Check if this is a protected endpoint and we don't have a token
        const isPublicEndpoint = PUBLIC_ENDPOINTS.some(path =>
            endpoint === path || endpoint.startsWith(`${path}?`));

        console.log('Is public endpoint:', isPublicEndpoint);

        if (!token && !isPublicEndpoint) {
            console.log(`Skipping unauthorized request to ${endpoint} - no token available`);
            return { error: 'Authentication required', unauthorized: true } as any;
        }

        // Prepare headers with authorization if token exists
        const headers: Record<string, string> = {
            'Content-Type': 'application/json',
            ...(options.headers as Record<string, string> || {}),
        };

        if (token) {
            headers['Authorization'] = `Bearer ${token}`;
        }

        const url = `${API_URL}${endpoint.startsWith('/') ? endpoint : `/${endpoint}`}`;
        console.log(`Fetching from: ${url} with method: ${options.method || 'GET'}`);

        const response = await fetch(url, {
            ...options,
            headers,
            // Do not include credentials or mode for simpler CORS handling
        });

        console.log(`Response status: ${response.status} ${response.statusText}`);

        // Handle different response statuses
        if (response.status === 401) {
            // Check if this is a 2FA error before clearing auth data
            const errorData = await response.json().catch(() => ({}));

            if (errorData.requires2FA) {
                console.log('401 is a 2FA requirement, not an auth failure - preserving session');
                // This is a 2FA requirement, not an auth failure - let it be handled normally
                // Don't clear localStorage or redirect, just fall through to normal error handling

                // Create error object that preserves 2FA requirements
                const error = new Error(errorData.error || `Server error: ${response.status}`);
                (error as any).requires2FA = true;
                if (errorData.message) {
                    (error as any).message = errorData.message;
                }
                console.log('Throwing 2FA error:', error);
                throw error;
            } else {
                console.error('Authentication error - please log in again');
                // Clear auth data on actual 401 errors
                localStorage.removeItem('token');
                localStorage.removeItem('user');
                window.location.href = '/auth';
                throw new Error('Authentication failed - please log in again');
            }
        }

        if (!response.ok) {
            const errorData = await response.json().catch(() => ({}));
            console.error(`API Error (${response.status}):`, errorData);
            console.log('Raw error data:', JSON.stringify(errorData, null, 2));

            // Create error object with the error message
            const error = new Error(errorData.error || `Server error: ${response.status}`);
            console.log('Throwing non-401 error:', error);
            throw error;
        }

        // For 204 No Content, return empty object
        if (response.status === 204) {
            return {} as T;
        }

        return await response.json();
    } catch (error: any) {
        // Improve error messages for connection issues
        if (error.name === 'TypeError' && error.message === 'Failed to fetch') {
            console.error('Network error - server may be down or unreachable');
            throw new Error('Cannot connect to backend server. Make sure the backend is running on ' + API_URL);
        }

        console.error("API fetch error:", error.message);
        throw error;
    }
};

// API hooks for different endpoints
export const useApi = () => {
    const { token } = useAuth();

    return {
        // Authentication
        login: async (email: string, password: string) => {
            return apiFetch('/auth/login', {
                method: 'POST',
                body: JSON.stringify({ email, password }),
            });
        },

        register: async (userData: any) => {
            return apiFetch('/auth/register', {
                method: 'POST',
                body: JSON.stringify(userData),
            });
        },

        // Movies
        getMovies: async (page = 1, limit = 10, director = '') => {
            return apiFetch(`/movies?page=${page}&limit=${limit}&director=${director}`);
        },

        getAllMovies: async (director = '') => {
            return apiFetch(`/movies?all=true&director=${director}`);
        },

        createMovie: async (movieData: any) => {
            return apiFetch('/movies', {
                method: 'POST',
                body: JSON.stringify(movieData),
            });
        },

        updateMovie: async (id: number, movieData: any) => {
            return apiFetch(`/movies/${id}`, {
                method: 'PATCH',
                body: JSON.stringify(movieData),
            });
        },

        deleteMovie: async (id: number) => {
            return apiFetch(`/movies/${id}`, {
                method: 'DELETE',
            });
        },

        // Directors
        getDirectors: async (page = 1, limit = 10, name = '') => {
            return apiFetch(`/directors?page=${page}&limit=${limit}&name=${name}`);
        },

        createDirector: async (directorData: any) => {
            return apiFetch('/directors', {
                method: 'POST',
                body: JSON.stringify(directorData),
            });
        },

        updateDirector: async (id: number, directorData: any) => {
            return apiFetch(`/directors/${id}`, {
                method: 'PATCH',
                body: JSON.stringify(directorData),
            });
        },

        deleteDirector: async (id: number) => {
            return apiFetch(`/directors/${id}`, {
                method: 'DELETE',
            });
        },

        // Users (admin only)
        getUsers: async (page = 1, limit = 10, name = '') => {
            return apiFetch(`/users?page=${page}&limit=${limit}&name=${name}`);
        },

        getUserById: async (id: number) => {
            return apiFetch(`/users/${id}`);
        }
    };
}; 