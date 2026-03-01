import React, { useState } from 'react';
import {
    Box,
    Button,
    TextField,
    Typography,
    Paper,
    Alert
} from '@mui/material';
import { API_URL } from '../utils/api';

interface DirectAuthFormProps {
    onSuccess: (token: string, user: any) => void;
}

const DirectAuthForm: React.FC<DirectAuthFormProps> = ({ onSuccess }) => {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    const [response, setResponse] = useState<any>(null);

    const handleDirectLogin = async (e: React.FormEvent) => {
        e.preventDefault();
        setError('');

        try {
            // Use XMLHttpRequest which has fewer CORS restrictions in some browsers
            const xhr = new XMLHttpRequest();
            xhr.open('POST', `${API_URL}/auth/login`, true);
            xhr.setRequestHeader('Content-Type', 'application/json');

            xhr.onload = function () {
                if (xhr.status >= 200 && xhr.status < 300) {
                    try {
                        const data = JSON.parse(xhr.responseText);
                        setResponse(data);
                        onSuccess(data.token, data.user);
                    } catch (parseError) {
                        setError('Invalid response from server');
                    }
                } else {
                    setError(`Server error: ${xhr.status}`);
                }
            };

            xhr.onerror = function () {
                setError('Network error occurred. Try using direct link below.');
            };

            xhr.send(JSON.stringify({ email, password }));
        } catch (err: any) {
            setError(err.message || 'Login failed');
        }
    };

    return (
        <Paper elevation={2} sx={{ p: 3, mt: 3, bgcolor: '#f5f5f5' }}>
            <Typography variant="h6" gutterBottom>
                Alternative Login Method
            </Typography>
            <Typography variant="body2" color="text.secondary" paragraph>
                If you're experiencing CORS issues, try this alternative login method:
            </Typography>

            <Box component="form" onSubmit={handleDirectLogin}>
                <TextField
                    margin="normal"
                    required
                    fullWidth
                    id="direct-email"
                    label="Email Address"
                    name="email"
                    autoComplete="email"
                    size="small"
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
                    id="direct-password"
                    autoComplete="current-password"
                    size="small"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                />

                {error && (
                    <Alert severity="error" sx={{ mt: 1, mb: 1 }}>
                        {error}
                    </Alert>
                )}

                <Button
                    type="submit"
                    fullWidth
                    variant="contained"
                    sx={{ mt: 2 }}
                >
                    Login with Alternative Method
                </Button>

                <Typography variant="body2" sx={{ mt: 2 }} align="center">
                    <a
                        href={`${API_URL}/auth/login-page`}
                        target="_blank"
                        rel="noopener noreferrer"
                    >
                        Or try direct login page
                    </a>
                </Typography>
            </Box>
        </Paper>
    );
};

export default DirectAuthForm; 