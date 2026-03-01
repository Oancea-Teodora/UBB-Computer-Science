import React, { useState, useEffect } from 'react';
import {
    Box,
    Button,
    Typography,
    Paper,
    TextField,
    Alert,
    AlertTitle,
    CircularProgress,
    List,
    ListItem,
    ListItemIcon,
    ListItemText,
    Divider,
    Accordion,
    AccordionSummary,
    AccordionDetails
} from '@mui/material';
import CheckCircleIcon from '@mui/icons-material/CheckCircle';
import ErrorIcon from '@mui/icons-material/Error';
import ExpandMoreIcon from '@mui/icons-material/ExpandMore';
import { API_URL, apiFetch } from '../utils/api';

const DiagnosticTool: React.FC = () => {
    const [loading, setLoading] = useState(false);
    const [results, setResults] = useState<Array<{ name: string, status: 'success' | 'error', message: string }>>([]);
    const [customUrl, setCustomUrl] = useState(API_URL);
    const [expanded, setExpanded] = useState<string | false>(false);
    const [token, setToken] = useState<string | null>(null);
    const [user, setUser] = useState<any>(null);
    const [testResult, setTestResult] = useState<string | null>(null);
    const [error, setError] = useState<string | null>(null);
    const [testInProgress, setTestInProgress] = useState(false);
    const [manualToken, setManualToken] = useState('');

    const handleChange = (panel: string) => (event: React.SyntheticEvent, isExpanded: boolean) => {
        setExpanded(isExpanded ? panel : false);
    };

    useEffect(() => {
        // Load token and user from localStorage
        const storedToken = localStorage.getItem('token');
        const storedUser = localStorage.getItem('user');

        setToken(storedToken);
        try {
            if (storedUser) {
                setUser(JSON.parse(storedUser));
            }
        } catch (error) {
            setError('Failed to parse user data from localStorage');
        }
    }, []);

    const runDiagnostics = async () => {
        setLoading(true);
        setResults([]);

        // Test 1: Basic Fetch
        try {
            const response = await fetch(customUrl, {
                method: 'GET',
                mode: 'cors',
            });

            setResults(prev => [...prev, {
                name: 'Basic Connectivity',
                status: response.ok ? 'success' : 'error',
                message: response.ok
                    ? `Connected successfully (${response.status} ${response.statusText})`
                    : `Connection failed: ${response.status} ${response.statusText}`
            }]);
        } catch (error: any) {
            setResults(prev => [...prev, {
                name: 'Basic Connectivity',
                status: 'error',
                message: `Error: ${error.message}`
            }]);
        }

        // Test 2: Health endpoint
        try {
            const healthEndpoint = `${customUrl}/health`;
            const response = await fetch(healthEndpoint, {
                method: 'GET',
                mode: 'cors',
                headers: { 'Content-Type': 'application/json' }
            });

            if (response.ok) {
                const data = await response.json();
                setResults(prev => [...prev, {
                    name: 'Health Check',
                    status: 'success',
                    message: `Health endpoint responded: ${JSON.stringify(data)}`
                }]);
            } else {
                setResults(prev => [...prev, {
                    name: 'Health Check',
                    status: 'error',
                    message: `Health endpoint responded with: ${response.status} ${response.statusText}`
                }]);
            }
        } catch (error: any) {
            setResults(prev => [...prev, {
                name: 'Health Check',
                status: 'error',
                message: `Error accessing health endpoint: ${error.message}`
            }]);
        }

        // Test 3: CORS Test
        try {
            const authEndpoint = `${customUrl}/auth/login`;
            const response = await fetch(authEndpoint, {
                method: 'OPTIONS',
                mode: 'cors',
                headers: { 'Content-Type': 'application/json' },
            });

            setResults(prev => [...prev, {
                name: 'CORS Preflight',
                status: response.ok ? 'success' : 'error',
                message: response.ok
                    ? 'CORS preflight successful'
                    : `CORS preflight failed: ${response.status} ${response.statusText}`
            }]);
        } catch (error: any) {
            setResults(prev => [...prev, {
                name: 'CORS Preflight',
                status: 'error',
                message: `CORS error: ${error.message}`
            }]);
        }

        setLoading(false);
    };

    const testConnection = async () => {
        setTestInProgress(true);
        setTestResult(null);
        setError(null);

        try {
            const response = await fetch(`${API_URL}/health`);
            const data = await response.json();
            setTestResult(`Server is ${data.status === 'ok' ? 'online' : 'having issues'}`);
        } catch (err) {
            setError('Failed to connect to server. CORS or network issue detected.');
        } finally {
            setTestInProgress(false);
        }
    };

    const testAuthenticatedRequest = async () => {
        setTestInProgress(true);
        setTestResult(null);
        setError(null);

        try {
            const storedToken = token || manualToken;
            if (!storedToken) {
                setError('No authentication token available');
                setTestInProgress(false);
                return;
            }

            // Test using the custom debug endpoint
            const response = await fetch(`${API_URL}/debug/auth`, {
                headers: {
                    'Authorization': `Bearer ${storedToken}`
                }
            });

            if (response.ok) {
                const data = await response.json();
                setTestResult(`Authentication headers verified. Token preview: ${data.tokenPreview}`);
            } else {
                const errorData = await response.json().catch(() => ({ error: 'Unknown error' }));
                setError(`Authentication failed: ${errorData.error || response.statusText}`);
            }
        } catch (err: any) {
            setError(`Error: ${err.message}`);
        } finally {
            setTestInProgress(false);
        }
    };

    const testDirectLogin = async () => {
        setTestInProgress(true);
        setTestResult(null);
        setError(null);

        if (!manualToken) {
            setError('Please enter credentials before testing');
            setTestInProgress(false);
            return;
        }

        try {
            const response = await fetch(`${API_URL}/auth/login`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ email: token, password: manualToken })
            });

            if (response.ok) {
                const data = await response.json();
                localStorage.setItem('token', data.token);
                localStorage.setItem('user', JSON.stringify(data.user));
                setToken(data.token);
                setUser(data.user);
                setTestResult(`Login successful! Token received for ${data.user.email}`);
            } else {
                const errorData = await response.json().catch(() => ({ error: 'Unknown error' }));
                setError(`Login failed: ${errorData.error || response.statusText}`);
            }
        } catch (err: any) {
            setError(`Error: ${err.message}`);
        } finally {
            setTestInProgress(false);
        }
    };

    const clearTokens = () => {
        localStorage.removeItem('token');
        localStorage.removeItem('user');
        setToken(null);
        setUser(null);
        setTestResult('Tokens cleared from localStorage');
    };

    const saveManualToken = () => {
        if (manualToken) {
            localStorage.setItem('token', manualToken);
            setToken(manualToken);
            setTestResult('Manual token saved to localStorage');
        }
    };

    return (
        <Box>
            <Paper elevation={3} sx={{ p: 3, mb: 3 }}>
                <Typography variant="h6" gutterBottom>
                    Connection Diagnostic Tool
                </Typography>
                <Typography variant="body1" paragraph>
                    This tool helps diagnose connectivity issues between the frontend and backend.
                </Typography>

                <Box sx={{ mb: 2 }}>
                    <TextField
                        label="Backend URL"
                        variant="outlined"
                        fullWidth
                        value={customUrl}
                        onChange={(e) => setCustomUrl(e.target.value)}
                        margin="normal"
                    />
                </Box>

                <Button
                    variant="contained"
                    onClick={runDiagnostics}
                    disabled={loading}
                    fullWidth
                >
                    {loading ? <CircularProgress size={24} /> : 'Run Diagnostics'}
                </Button>

                {results.length > 0 && (
                    <Box sx={{ mt: 3 }}>
                        <Typography variant="h6" gutterBottom>
                            Results:
                        </Typography>
                        <List>
                            {results.map((result, index) => (
                                <React.Fragment key={index}>
                                    <ListItem>
                                        <ListItemIcon>
                                            {result.status === 'success' ? (
                                                <CheckCircleIcon color="success" />
                                            ) : (
                                                <ErrorIcon color="error" />
                                            )}
                                        </ListItemIcon>
                                        <ListItemText
                                            primary={result.name}
                                            secondary={result.message}
                                        />
                                    </ListItem>
                                    {index < results.length - 1 && <Divider />}
                                </React.Fragment>
                            ))}
                        </List>
                    </Box>
                )}
            </Paper>

            <Accordion expanded={expanded === 'panel1'} onChange={handleChange('panel1')}>
                <AccordionSummary expandIcon={<ExpandMoreIcon />}>
                    <Typography>Troubleshooting Tips</Typography>
                </AccordionSummary>
                <AccordionDetails>
                    <Typography variant="body2" paragraph>
                        If you're experiencing connection issues:
                    </Typography>
                    <List dense>
                        <ListItem>
                            <ListItemIcon>
                                <CheckCircleIcon fontSize="small" />
                            </ListItemIcon>
                            <ListItemText primary="Ensure the backend server is running (npm start in backend directory)" />
                        </ListItem>
                        <ListItem>
                            <ListItemIcon>
                                <CheckCircleIcon fontSize="small" />
                            </ListItemIcon>
                            <ListItemText primary="Check if port 3001 is already in use by another application" />
                        </ListItem>
                        <ListItem>
                            <ListItemIcon>
                                <CheckCircleIcon fontSize="small" />
                            </ListItemIcon>
                            <ListItemText primary="Try using 127.0.0.1 instead of localhost" />
                        </ListItem>
                        <ListItem>
                            <ListItemIcon>
                                <CheckCircleIcon fontSize="small" />
                            </ListItemIcon>
                            <ListItemText primary="For CORS issues, use a browser extension to disable CORS for development" />
                        </ListItem>
                        <ListItem>
                            <ListItemIcon>
                                <CheckCircleIcon fontSize="small" />
                            </ListItemIcon>
                            <ListItemText primary="Check if your firewall is blocking connections" />
                        </ListItem>
                    </List>
                </AccordionDetails>
            </Accordion>

            <Paper elevation={3} sx={{ p: 3, mt: 3 }}>
                <Typography variant="h5" gutterBottom>Authentication Diagnostic Tool</Typography>

                <Box sx={{ mb: 3 }}>
                    <Typography variant="h6">Authentication Status</Typography>
                    {token ? (
                        <Alert severity="info" sx={{ mb: 2 }}>
                            Token found in localStorage
                        </Alert>
                    ) : (
                        <Alert severity="warning" sx={{ mb: 2 }}>
                            No authentication token found in localStorage
                        </Alert>
                    )}

                    {user ? (
                        <Alert severity="info">
                            User found: {user.email} (Role: {user.role})
                        </Alert>
                    ) : (
                        <Alert severity="warning">
                            No user data found in localStorage
                        </Alert>
                    )}
                </Box>

                <Box sx={{ display: 'flex', gap: 2, mb: 3 }}>
                    <Button
                        variant="contained"
                        onClick={testConnection}
                        disabled={testInProgress}
                    >
                        Test Server Connection
                    </Button>
                    <Button
                        variant="contained"
                        onClick={testAuthenticatedRequest}
                        disabled={testInProgress}
                    >
                        Test Authentication
                    </Button>
                    <Button
                        variant="outlined"
                        color="warning"
                        onClick={clearTokens}
                    >
                        Clear Tokens
                    </Button>
                </Box>

                <Box sx={{ mb: 3 }}>
                    <Typography variant="h6" gutterBottom>Manual Token Entry</Typography>
                    <TextField
                        fullWidth
                        label="Enter Authentication Token"
                        value={manualToken}
                        onChange={(e) => setManualToken(e.target.value)}
                        margin="normal"
                    />
                    <Button
                        variant="contained"
                        onClick={saveManualToken}
                        disabled={!manualToken}
                    >
                        Save Token
                    </Button>
                </Box>

                {testResult && (
                    <Alert severity="success" sx={{ mt: 2 }}>
                        {testResult}
                    </Alert>
                )}

                {error && (
                    <Alert severity="error" sx={{ mt: 2 }}>
                        {error}
                    </Alert>
                )}

                <Box sx={{ mt: 3, p: 2, bgcolor: '#f5f5f5', borderRadius: 1 }}>
                    <Typography variant="h6" gutterBottom>Troubleshooting Tips</Typography>
                    <Typography variant="body2">
                        1. Try using the alternative login method or direct login page.
                    </Typography>
                    <Typography variant="body2">
                        2. Check browser console for any JavaScript errors.
                    </Typography>
                    <Typography variant="body2">
                        3. Clear browser cache and cookies, then try again.
                    </Typography>
                    <Typography variant="body2">
                        4. Verify CORS settings on the server allow requests from your frontend.
                    </Typography>
                </Box>
            </Paper>
        </Box>
    );
};

export default DiagnosticTool; 