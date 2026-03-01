import { useState, useEffect } from 'react';
import { API_URL } from '../utils/api';

function Debug() {
    const [backendStatus, setBackendStatus] = useState<string>('Checking...');
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        const checkBackend = async () => {
            try {
                const response = await fetch(`${API_URL}/health`);

                if (response.ok) {
                    const data = await response.json();
                    setBackendStatus(`Connected: ${JSON.stringify(data)}`);
                } else {
                    setBackendStatus(`Error: ${response.status} ${response.statusText}`);
                }
            } catch (err: any) {
                setError(err.message);
                setBackendStatus('Failed to connect to backend');
            }
        };

        checkBackend();
    }, []);

    return (
        <div style={{ padding: '20px', maxWidth: '800px', margin: '0 auto' }}>
            <h1>Debug Page</h1>

            <div style={{ marginBottom: '20px' }}>
                <h2>Backend Connection</h2>
                <p><strong>API URL:</strong> {API_URL}</p>
                <p><strong>Status:</strong> {backendStatus}</p>
                {error && <p style={{ color: 'red' }}><strong>Error:</strong> {error}</p>}
            </div>

            <div style={{ marginBottom: '20px' }}>
                <h2>Troubleshooting Steps</h2>
                <ol>
                    <li>Make sure your backend server is running at {API_URL}</li>
                    <li>Check that CORS is properly configured on the backend</li>
                    <li>Verify network connectivity between frontend and backend</li>
                    <li>Check browser console for additional errors</li>
                </ol>
            </div>

            <div>
                <h2>Manual Test</h2>
                <p>Try opening the backend health endpoint directly in a new tab:</p>
                <a href={`${API_URL}/health`} target="_blank" rel="noreferrer">
                    {`${API_URL}/health`}
                </a>
            </div>
        </div>
    );
}

export default Debug; 