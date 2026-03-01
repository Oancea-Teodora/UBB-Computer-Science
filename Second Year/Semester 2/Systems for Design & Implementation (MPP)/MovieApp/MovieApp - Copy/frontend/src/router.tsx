import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import HomePage from "./pages/HomePage";
import Navbar from "./components/Navbar";

const AppRouter = () => {
    return (
        <Router>
            <Navbar /> { }
            <Routes>
                <Route path="/" element={<HomePage />} />

                <Route path="*" element={<h1>404 - Page Not Found</h1>} />
            </Routes>
        </Router>
    );
};

export default AppRouter;
