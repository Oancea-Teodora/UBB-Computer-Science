import React, { useState, useEffect } from 'react';
import {
    Box,
    Button,
    TextField,
    Typography,
    Paper,
    Container,
    Tab,
    Tabs,
    Alert,
    CircularProgress,
    Collapse,
    IconButton
} from '@mui/material';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../contexts/AuthContext';
import { apiFetch, API_URL } from '../utils/api';
import DiagnosticTool from './DiagnosticTool';
import ExpandMoreIcon from '@mui/icons-material/ExpandMore';
import HelpOutlineIcon from '@mui/icons-material/HelpOutline';

interface TabPanelProps {
    children?: React.ReactNode;
    index: number;
    value: number;
}

function TabPanel(props: TabPanelProps) {
    const { children, value, index, ...other } = props;

    return (
        <div
            role="tabpanel"
            hidden={value !== index}
            id={`auth-tabpanel-${index}`}
            aria-labelledby={`auth-tab-${index}`}
            {...other}
        >
            {value === index && (
                <Box sx={{ p: 3 }}>
                    {children}
                </Box>
            )}
        </div>
    );
}

const Auth: React.FC = () => {
    const [tabValue, setTabValue] = useState(0);
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [name, setName] = useState('');
    const [twoFactorCode, setTwoFactorCode] = useState('');
    const [requires2FA, setRequires2FA] = useState(false);
    const [error, setError] = useState('');
    const [loading, setLoading] = useState(false);
    const [showDiagnostics, setShowDiagnostics] = useState(false);
    const [connectionFailed, setConnectionFailed] = useState(false);
    const navigate = useNavigate();
    const { login } = useAuth();

    useEffect(() => {
        const checkServer = async () => {
            try {
                await fetch(`${API_URL}/health`);
                setConnectionFailed(false);
            } catch (error) {
                console.error("Initial server connection check failed:", error);
                setConnectionFailed(true);
            }
        };

        checkServer();
    }, []);

    const handleTabChange = (_event: React.SyntheticEvent, newValue: number) => {
        setTabValue(newValue);
        setError('');
        // Reset 2FA state when switching tabs
        setRequires2FA(false);
        setTwoFactorCode('');
    };

    const handleLogin = async (e: React.FormEvent) => {
        e.preventDefault();
        setLoading(true);
        setError('');

        console.log('=== LOGIN ATTEMPT ===');
        console.log('Email:', email);
        console.log('Password length:', password.length);
        console.log('requires2FA:', requires2FA);
        console.log('twoFactorCode:', twoFactorCode);

        try {
            const loginData: any = { email, password };

            // Add 2FA token if required and provided
            if (requires2FA && twoFactorCode) {
                loginData.twoFactorToken = twoFactorCode;
            }

            console.log('About to call login API...');
            const data = await apiFetch('/auth/login', {
                method: 'POST',
                body: JSON.stringify(loginData),
            });

            console.log('Login API successful, data received:', data);
            console.log('About to call login() with token and user...');
            login(data.token, data.user);
            console.log('login() called, about to navigate...');
            navigate('/');
            console.log('navigate("/") called');
        } catch (err: any) {
            console.error('Login error:', err);
            console.error('Error properties:', Object.keys(err));
            console.error('Error.requires2FA:', err.requires2FA);
            console.error('Error.message:', err.message);
            console.error('Full error object:', JSON.stringify(err, null, 2));

            // Check if error indicates 2FA is required
            if (err.requires2FA || err.message.includes('Two-factor authentication required')) {
                console.log('2FA required - showing 2FA input');
                setRequires2FA(true);
                setError('Please enter your 6-digit authenticator code below.');
            } else {
                console.log('Regular login error');
                setError(err.message || 'Login failed. Please check your credentials and try again.');
            }
        } finally {
            setLoading(false);
        }
    };

    const handleRegister = async (e: React.FormEvent) => {
        e.preventDefault();
        setLoading(true);
        setError('');

        try {
            const data = await apiFetch('/auth/register', {
                method: 'POST',
                body: JSON.stringify({ email, password, name }),
            });

            login(data.token, data.user);
            navigate('/');
        } catch (err: any) {
            console.error('Registration error:', err);
            setError(err.message || 'Registration failed. Please try again.');
        } finally {
            setLoading(false);
        }
    };

    return (
        <Container component="main" maxWidth="md">
            <Paper elevation={3} sx={{ mt: 8, p: 4 }}>
                <Typography component="h1" variant="h5" align="center" gutterBottom>
                    Movie App
                </Typography>

                {connectionFailed && (
                    <Box sx={{ mt: 2, mb: 2 }}>
                        <Alert
                            severity="info"
                            action={
                                <IconButton
                                    aria-label="show diagnostics"
                                    color="inherit"
                                    size="small"
                                    onClick={() => setShowDiagnostics(!showDiagnostics)}
                                >
                                    {showDiagnostics ? <ExpandMoreIcon /> : <HelpOutlineIcon />}
                                </IconButton>
                            }
                        >
                            Connection problems detected. Click the icon to run diagnostics.
                        </Alert>
                        <Collapse in={showDiagnostics}>
                            <Box sx={{ mt: 2 }}>
                                <DiagnosticTool />
                            </Box>
                        </Collapse>
                    </Box>
                )}

                <Box sx={{ borderBottom: 1, borderColor: 'divider' }}>
                    <Tabs value={tabValue} onChange={handleTabChange} centered>
                        <Tab label="Login" />
                        <Tab label="Register" />
                    </Tabs>
                </Box>

                <TabPanel value={tabValue} index={0}>
                    <Box component="form" onSubmit={handleLogin} noValidate>
                        <TextField
                            margin="normal"
                            required
                            fullWidth
                            id="email"
                            label="Email Address"
                            name="email"
                            autoComplete="email"
                            autoFocus={!requires2FA}
                            value={email}
                            onChange={(e) => setEmail(e.target.value)}
                            disabled={requires2FA}
                        />
                        <TextField
                            margin="normal"
                            required
                            fullWidth
                            name="password"
                            label="Password"
                            type="password"
                            id="password"
                            autoComplete="current-password"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                            disabled={requires2FA}
                        />
                        {requires2FA && (
                            <TextField
                                margin="normal"
                                required
                                fullWidth
                                name="twoFactorCode"
                                label="6-digit Authenticator Code"
                                type="text"
                                id="twoFactorCode"
                                placeholder="Enter code from authenticator app"
                                value={twoFactorCode}
                                onChange={(e) => setTwoFactorCode(e.target.value.replace(/\D/g, '').slice(0, 6))}
                                inputProps={{ maxLength: 6 }}
                                autoFocus
                                helperText="Enter the 6-digit code from your authenticator app"
                            />
                        )}
                        {error && (
                            <Alert severity={requires2FA ? "info" : "error"} sx={{ mt: 2 }}>
                                {error}
                            </Alert>
                        )}
                        {requires2FA && (
                            <Button
                                fullWidth
                                variant="outlined"
                                sx={{ mt: 2 }}
                                onClick={() => {
                                    setRequires2FA(false);
                                    setTwoFactorCode('');
                                    setError('');
                                }}
                            >
                                ← Back to Login
                            </Button>
                        )}
                        <Button
                            type="submit"
                            fullWidth
                            variant="contained"
                            sx={{ mt: 3, mb: 2 }}
                            disabled={loading || (requires2FA && twoFactorCode.length !== 6)}
                        >
                            {loading ? <CircularProgress size={24} /> :
                                requires2FA ? "Verify & Sign In" : "Sign In"}
                        </Button>
                    </Box>
                </TabPanel>

                <TabPanel value={tabValue} index={1}>
                    <Box component="form" onSubmit={handleRegister} noValidate>
                        <TextField
                            margin="normal"
                            required
                            fullWidth
                            id="name"
                            label="Full Name"
                            name="name"
                            autoComplete="name"
                            autoFocus
                            value={name}
                            onChange={(e) => setName(e.target.value)}
                        />
                        <TextField
                            margin="normal"
                            required
                            fullWidth
                            id="email"
                            label="Email Address"
                            name="email"
                            autoComplete="email"
                            value={email}
                            onChange={(e) => setEmail(e.target.value)}
                        />
                        <TextField
                            margin="normal"
                            required
                            fullWidth
                            name="password"
                            label="Password"
                            type="password"
                            id="password"
                            autoComplete="new-password"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                        />
                        {error && (
                            <Alert severity="error" sx={{ mt: 2 }}>
                                {error}
                            </Alert>
                        )}
                        <Button
                            type="submit"
                            fullWidth
                            variant="contained"
                            sx={{ mt: 3, mb: 2 }}
                            disabled={loading}
                        >
                            {loading ? <CircularProgress size={24} /> : "Register"}
                        </Button>
                    </Box>
                </TabPanel>
            </Paper>
        </Container>
    );
};

export default Auth; 