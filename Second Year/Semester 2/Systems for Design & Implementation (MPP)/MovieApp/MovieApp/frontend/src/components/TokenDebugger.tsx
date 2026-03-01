import React, { useState, useEffect } from 'react';
import {
    Box,
    Typography,
    Paper,
    Button,
    CircularProgress,
    Alert,
    TextField,
    IconButton,
    Collapse
} from '@mui/material';
import { API_URL } from '../utils/api';
import ExpandMoreIcon from '@mui/icons-material/ExpandMore';
import ExpandLessIcon from '@mui/icons-material/ExpandLess';

const TokenDebugger: React.FC = () => {
    const [tokenInfo, setTokenInfo] = useState<any>(null);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState<string | null>(null);
    const [showTokenDebugger, setShowTokenDebugger] = useState(false);
    const [manualToken, setManualToken] = useState('');
    const [storedToken, setStoredToken] = useState<string | null>(null);

    useEffect(() => {
        const token = localStorage.getItem('token');
        setStoredToken(token);
    }, []);

    const checkTokenInfo = async () => {
        setLoading(true);
        setError(null);

        try {
            const token = localStorage.getItem('token');
            const headers: Record<string, string> = {};

            if (token) {
                headers['Authorization'] = `Bearer ${token}`;
            }

            const response = await fetch(`${API_URL}/debug/auth`, {
                headers
            });

            if (!response.ok) {
                throw new Error(`Server error: ${response.status}`);
            }

            const data = await response.json();
            setTokenInfo(data);
        } catch (err: any) {
            console.error('Failed to get token debug info:', err);
            setError(err.message || 'Failed to get token information');
        } finally {
            setLoading(false);
        }
    };

    const updateToken = () => {
        if (manualToken) {
            localStorage.setItem('token', manualToken);
            setStoredToken(manualToken);
        }
    };

    const clearToken = () => {
        localStorage.removeItem('token');
        setStoredToken(null);
        setManualToken('');
    };

    return (
        <Paper elevation={2} sx={{ p: 3, mt: 3, bgcolor: '#f8f8f8' }}>
            <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                <Typography variant="h6">Authentication Debugger</Typography>
                <IconButton onClick={() => setShowTokenDebugger(!showTokenDebugger)}>
                    {showTokenDebugger ? <ExpandLessIcon /> : <ExpandMoreIcon />}
                </IconButton>
            </Box>

            <Collapse in={showTokenDebugger}>
                <Box sx={{ mt: 2 }}>
                    <Alert severity={storedToken ? "info" : "warning"} sx={{ mb: 2 }}>
                        {storedToken
                            ? `Token found in localStorage (${storedToken.substring(0, 15)}...)`
                            : "No authentication token found"}
                    </Alert>

                    <Box sx={{ display: 'flex', gap: 2, mb: 3 }}>
                        <Button
                            variant="contained"
                            onClick={checkTokenInfo}
                            disabled={loading}
                        >
                            {loading ? <CircularProgress size={24} /> : "Check Token Status"}
                        </Button>
                        <Button
                            variant="outlined"
                            color="error"
                            onClick={clearToken}
                        >
                            Clear Token
                        </Button>
                    </Box>

                    <Box sx={{ mb: 3 }}>
                        <Typography variant="subtitle1" gutterBottom>
                            Manual Token Update
                        </Typography>
                        <TextField
                            fullWidth
                            label="Enter JWT Token"
                            value={manualToken}
                            onChange={(e) => setManualToken(e.target.value)}
                            size="small"
                            sx={{ mb: 1 }}
                        />
                        <Button
                            variant="contained"
                            onClick={updateToken}
                            disabled={!manualToken}
                            size="small"
                        >
                            Update Token
                        </Button>
                    </Box>

                    {error && (
                        <Alert severity="error" sx={{ mb: 2 }}>
                            {error}
                        </Alert>
                    )}

                    {tokenInfo && (
                        <Box sx={{
                            p: 2,
                            bgcolor: '#edf7ed',
                            border: '1px solid #c8e6c9',
                            borderRadius: 1,
                            mt: 2
                        }}>
                            <Typography variant="subtitle1" gutterBottom>
                                Token Debug Information
                            </Typography>
                            <pre style={{
                                overflowX: 'auto',
                                whiteSpace: 'pre-wrap',
                                wordBreak: 'break-all'
                            }}>
                                {JSON.stringify(tokenInfo, null, 2)}
                            </pre>
                        </Box>
                    )}
                </Box>
            </Collapse>
        </Paper>
    );
};

export default TokenDebugger; 