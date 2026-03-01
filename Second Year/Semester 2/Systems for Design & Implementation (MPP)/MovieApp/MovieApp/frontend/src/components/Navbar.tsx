import { AppBar, Toolbar, Box, Button, Typography } from "@mui/material";
import { Link } from 'react-router-dom';
import { useAuth } from '../contexts/AuthContext';

const Navbar = () => {
    const { isAuthenticated, logout, user } = useAuth();
    const isAdmin = user?.role === 'admin';

    return (
        <AppBar
            position="fixed"
            sx={{
                backgroundColor: "rgba(25, 25, 25, 0.9)",
                backdropFilter: "blur(15px)",
                boxShadow: "none",
            }}
        >
            <Toolbar>
                <Box>
                    {isAdmin ? (
                        // Admin navigation
                        <>
                            <Button component={Link} to="/admin" sx={{ color: "#fff" }}>
                                Admin Dashboard
                            </Button>
                        </>
                    ) : (
                        // Regular user navigation
                        <>
                            <Button component={Link} to="/" sx={{ color: "#fff" }}>
                                Home
                            </Button>
                            <Button component={Link} to="/movies" sx={{ color: "#fff" }}>
                                Movies
                            </Button>
                            <Button component={Link} to="/directors" sx={{ color: "#fff" }}>
                                Directors
                            </Button>
                        </>
                    )}
                </Box>

                <Box sx={{ marginLeft: 'auto', display: 'flex', alignItems: 'center' }}>
                    {isAuthenticated && (
                        <>
                            <Button component={Link} to="/2fa" sx={{ color: "#fff", mr: 1 }}>
                                2FA
                            </Button>
                            <Typography variant="body2" sx={{ mr: 2, color: '#fff' }}>
                                {user?.name} ({user?.role})
                            </Typography>
                        </>
                    )}

                    {isAuthenticated ? (
                        <Button onClick={logout} sx={{ color: "#fff" }}>
                            Logout
                        </Button>
                    ) : (
                        <Button component={Link} to="/auth" sx={{ color: "#fff" }}>
                            Login
                        </Button>
                    )}
                </Box>
            </Toolbar>
        </AppBar>
    );
};

export default Navbar;
