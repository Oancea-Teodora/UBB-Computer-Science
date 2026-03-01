import { BrowserRouter as Router, Routes, Route, Navigate } from "react-router-dom";
import HomePage from "./pages/HomePage";
import AdminDashboard from "./pages/AdminDashboard";
import Navbar from "./components/Navbar";
import { useAuth } from './contexts/AuthContext';
import Auth from './components/Auth';
import { ProtectedRoute } from './components/ProtectedRoute';
import Debug from './pages/Debug';
import TestPage from './pages/TestPage';
import TwoFactorAuth from './components/TwoFactorAuth';

const AppRouter = () => {
    const { isAuthenticated, user } = useAuth();
    const isAdmin = user?.role === 'admin';

    return (
        <Router>
            <Navbar />
            <Routes>
                {/* Test page (temporary for debugging) */}
                <Route path="/test" element={<TestPage />} />

                {/* Debug route - accessible without auth */}
                <Route path="/debug" element={<Debug />} />

                <Route path="/auth" element={!isAuthenticated ? <Auth /> : (isAdmin ? <Navigate to="/admin" replace /> : <Navigate to="/" replace />)} />

                {/* Regular user routes */}
                <Route path="/" element={
                    <ProtectedRoute>
                        <HomePage />
                    </ProtectedRoute>
                } />

                <Route path="/movies" element={
                    <ProtectedRoute>
                        <HomePage />
                    </ProtectedRoute>
                } />

                <Route path="/directors" element={
                    <ProtectedRoute>
                        <HomePage />
                    </ProtectedRoute>
                } />

                <Route path="/2fa" element={
                    <ProtectedRoute>
                        <TwoFactorAuth />
                    </ProtectedRoute>
                } />

                {/* Admin dashboard */}
                <Route path="/admin" element={
                    <ProtectedRoute>
                        <AdminDashboard />
                    </ProtectedRoute>
                } />

                <Route path="*" element={<Navigate to="/auth" replace />} />
            </Routes>
        </Router>
    );
};

export default AppRouter;
