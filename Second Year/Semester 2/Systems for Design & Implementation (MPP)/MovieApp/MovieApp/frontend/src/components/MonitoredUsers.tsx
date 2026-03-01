import React, { useState, useEffect } from 'react';
import {
    Typography,
    Paper,
    Table,
    TableBody,
    TableCell,
    TableContainer,
    TableHead,
    TableRow,
    Button,
    Alert,
    CircularProgress,
    Box,
    Chip,
    IconButton,
    Tooltip
} from '@mui/material';
import VisibilityIcon from '@mui/icons-material/Visibility';
import PersonOffIcon from '@mui/icons-material/PersonOff';
import { apiFetch, API_URL } from '../utils/api';
import { formatDistanceToNow } from 'date-fns';

interface MonitoredUser {
    id: number;
    userId: number;
    user: {
        id: number;
        email: string;
        name: string;
    };
    monitoredSince: string;
    reason: string;
    suspiciousActivityCount: number;
    isActive: boolean;
}

const MonitoredUsers: React.FC = () => {
    const [users, setUsers] = useState<MonitoredUser[]>([]);
    const [loading, setLoading] = useState<boolean>(true);
    const [error, setError] = useState<string | null>(null);

    const fetchMonitoredUsers = async () => {
        setLoading(true);
        setError(null);
        try {
            const response = await apiFetch('/monitoring/users');
            setUsers(response.monitoredUsers || []);
        } catch (err: any) {
            setError(err.message || 'Failed to fetch monitored users');
            console.error('Error fetching monitored users:', err);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchMonitoredUsers();

        // Refresh data every 30 seconds
        const intervalId = setInterval(() => {
            fetchMonitoredUsers();
        }, 30000);

        return () => clearInterval(intervalId);
    }, []);

    const handleUnmonitorUser = async (id: number) => {
        try {
            await apiFetch(`/monitoring/users/${id}/unmonitor`, {
                method: 'POST'
            });

            // Update the local state to reflect the change
            setUsers(users.filter(user => user.id !== id));
        } catch (err: any) {
            setError(err.message || 'Failed to unmonitor user');
            console.error('Error unmonitoring user:', err);
        }
    };

    const viewUserLogs = (userId: number) => {
        // This could open a modal or navigate to a user logs page
        // For now, just open in a new tab
        window.open(`/admin/logs?userId=${userId}`, '_blank');
    };

    if (loading && users.length === 0) {
        return (
            <Box display="flex" justifyContent="center" alignItems="center" minHeight="200px">
                <CircularProgress />
            </Box>
        );
    }

    return (
        <Paper sx={{ p: 3, width: '100%' }}>
            <Typography variant="h5" gutterBottom>
                Monitored Users
            </Typography>

            {error && (
                <Alert severity="error" sx={{ mb: 2 }}>
                    {error}
                </Alert>
            )}

            {users.length === 0 ? (
                <Alert severity="info" sx={{ mb: 2 }}>
                    No suspicious users are currently being monitored.
                </Alert>
            ) : (
                <TableContainer>
                    <Table>
                        <TableHead>
                            <TableRow>
                                <TableCell>User</TableCell>
                                <TableCell>Reason</TableCell>
                                <TableCell align="center">Suspicious Count</TableCell>
                                <TableCell>Monitored Since</TableCell>
                                <TableCell align="center">Actions</TableCell>
                            </TableRow>
                        </TableHead>
                        <TableBody>
                            {users.map((user) => (
                                <TableRow key={user.id} hover>
                                    <TableCell>
                                        {user.user.name} <br />
                                        <Typography variant="body2" color="textSecondary">
                                            {user.user.email}
                                        </Typography>
                                    </TableCell>
                                    <TableCell>{user.reason}</TableCell>
                                    <TableCell align="center">
                                        <Chip
                                            label={user.suspiciousActivityCount}
                                            color={user.suspiciousActivityCount > 5 ? "error" : "warning"}
                                            size="small"
                                        />
                                    </TableCell>
                                    <TableCell>
                                        {formatDistanceToNow(new Date(user.monitoredSince), { addSuffix: true })}
                                    </TableCell>
                                    <TableCell align="center">
                                        <Tooltip title="View User Logs">
                                            <IconButton
                                                size="small"
                                                color="primary"
                                                onClick={() => viewUserLogs(user.userId)}
                                            >
                                                <VisibilityIcon />
                                            </IconButton>
                                        </Tooltip>
                                        <Tooltip title="Stop Monitoring">
                                            <IconButton
                                                size="small"
                                                color="error"
                                                onClick={() => handleUnmonitorUser(user.id)}
                                            >
                                                <PersonOffIcon />
                                            </IconButton>
                                        </Tooltip>
                                    </TableCell>
                                </TableRow>
                            ))}
                        </TableBody>
                    </Table>
                </TableContainer>
            )}

            <Box sx={{ mt: 2, display: 'flex', justifyContent: 'flex-end' }}>
                <Button
                    variant="outlined"
                    onClick={() => fetchMonitoredUsers()}
                    disabled={loading}
                >
                    {loading ? <CircularProgress size={24} /> : 'Refresh'}
                </Button>
            </Box>
        </Paper>
    );
};

export default MonitoredUsers; 