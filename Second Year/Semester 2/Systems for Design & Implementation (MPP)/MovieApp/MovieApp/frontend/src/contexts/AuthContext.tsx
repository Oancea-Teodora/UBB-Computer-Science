import React, { createContext, useContext, useState, useEffect } from 'react';

interface User {
    id: number;
    email: string;
    name: string;
    role: string;
}

interface AuthContextType {
    user: User | null;
    token: string | null;
    login: (token: string, user: User) => void;
    logout: () => void;
    isAuthenticated: boolean;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const AuthProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
    const [user, setUser] = useState<User | null>(null);
    const [token, setToken] = useState<string | null>(null);
    const [isAuthenticated, setIsAuthenticated] = useState(false);

    useEffect(() => {
        // Check for existing auth data on mount
        console.log('=== AuthContext: Checking for existing auth data ===');
        const storedToken = localStorage.getItem('token');
        const storedUser = localStorage.getItem('user');

        console.log('Stored token exists:', !!storedToken);
        console.log('Stored user exists:', !!storedUser);

        if (storedToken && storedUser) {
            console.log('Found existing auth data, setting as authenticated');
            setToken(storedToken);
            setUser(JSON.parse(storedUser));
            setIsAuthenticated(true);
        } else {
            console.log('No existing auth data found');
        }
    }, []);

    const login = (newToken: string, newUser: User) => {
        console.log('=== AuthContext: login() called ===');
        console.log('New token:', newToken ? 'exists' : 'missing');
        console.log('New user:', newUser);

        localStorage.setItem('token', newToken);
        localStorage.setItem('user', JSON.stringify(newUser));
        setToken(newToken);
        setUser(newUser);
        setIsAuthenticated(true);

        console.log('Auth state updated - isAuthenticated: true');
    };

    const logout = () => {
        console.log('=== AuthContext: logout() called ===');
        localStorage.removeItem('token');
        localStorage.removeItem('user');
        setToken(null);
        setUser(null);
        setIsAuthenticated(false);
        console.log('Auth state cleared - isAuthenticated: false');
    };

    return (
        <AuthContext.Provider value={{ user, token, login, logout, isAuthenticated }}>
            {children}
        </AuthContext.Provider>
    );
};

export const useAuth = () => {
    const context = useContext(AuthContext);
    if (context === undefined) {
        throw new Error('useAuth must be used within an AuthProvider');
    }
    return context;
}; 