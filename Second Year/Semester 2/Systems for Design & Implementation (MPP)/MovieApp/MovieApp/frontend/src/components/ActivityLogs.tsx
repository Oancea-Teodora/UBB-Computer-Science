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
    TablePagination,
    Button,
    Alert,
    CircularProgress,
    Box,
    Chip,
    TextField,
    InputAdornment
} from '@mui/material';
import SearchIcon from '@mui/icons-material/Search';
import { apiFetch, API_URL } from '../utils/api';

interface User {
    id: number;
    email: string;
    name: string;
}

interface Log {
    id: number;
    action: 'CREATE' | 'READ' | 'UPDATE' | 'DELETE';
    entityType: 'Movie' | 'Director' | 'User' | 'Other';
    entityId?: number;
    details?: string;
    timestamp: string;
    user?: User;
    userId?: number;
    isUnusual: boolean;
}

interface LogsResponse {
    logs: Log[];
    page: number;
    totalItems: number;
    totalPages: number;
}

const ActivityLogs: React.FC = () => {
    const [logs, setLogs] = useState<Log[]>([]);
    const [page, setPage] = useState(0);
    const [rowsPerPage, setRowsPerPage] = useState(10);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);
    const [totalItems, setTotalItems] = useState(0);
    const [userFilter, setUserFilter] = useState('');

    const searchParams = new URLSearchParams(window.location.search);
    const userIdParam = searchParams.get('userId');

    const fetchLogs = async () => {
        setLoading(true);
        setError(null);

        try {
            let endpoint = `/monitoring/logs?page=${page + 1}&limit=${rowsPerPage}`;

            if (userIdParam) {
                endpoint += `&userId=${userIdParam}`;
            }

            const response = await apiFetch<LogsResponse>(endpoint);

            setLogs(response.logs || []);
            setTotalItems(response.totalItems || 0);
        } catch (err: any) {
            setError(err.message || 'Failed to fetch activity logs');
            console.error('Error fetching activity logs:', err);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchLogs();
    }, [page, rowsPerPage, userIdParam]);

    const handleChangePage = (_event: unknown, newPage: number) => {
        setPage(newPage);
    };

    const handleChangeRowsPerPage = (event: React.ChangeEvent<HTMLInputElement>) => {
        setRowsPerPage(parseInt(event.target.value, 10));
        setPage(0);
    };

    const getActionColor = (action: string) => {
        switch (action) {
            case 'CREATE': return 'success';
            case 'READ': return 'info';
            case 'UPDATE': return 'warning';
            case 'DELETE': return 'error';
            default: return 'default';
        }
    };

    const formatTimestamp = (timestamp: string) => {
        const date = new Date(timestamp);
        return date.toLocaleString();
    };

    const parseLogDetails = (details?: string) => {
        if (!details) return null;

        try {
            return JSON.parse(details);
        } catch {
            return { raw: details };
        }
    };

    return (
        <Paper sx={{ p: 3, width: '100%' }}>
            <Typography variant="h5" gutterBottom>
                Activity Logs
            </Typography>

            {error && (
                <Alert severity="error" sx={{ mb: 2 }}>
                    {error}
                </Alert>
            )}

            <Box sx={{ mb: 2, display: 'flex', justifyContent: 'space-between' }}>
                <TextField
                    placeholder="Search by user..."
                    size="small"
                    value={userFilter}
                    onChange={(e) => setUserFilter(e.target.value)}
                    InputProps={{
                        startAdornment: (
                            <InputAdornment position="start">
                                <SearchIcon />
                            </InputAdornment>
                        ),
                    }}
                />

                <Button
                    variant="outlined"
                    onClick={fetchLogs}
                    disabled={loading}
                >
                    {loading ? <CircularProgress size={24} /> : 'Refresh'}
                </Button>
            </Box>

            <TableContainer>
                <Table size="small">
                    <TableHead>
                        <TableRow>
                            <TableCell>Timestamp</TableCell>
                            <TableCell>User</TableCell>
                            <TableCell>Action</TableCell>
                            <TableCell>Entity</TableCell>
                            <TableCell>Details</TableCell>
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {loading && logs.length === 0 ? (
                            <TableRow>
                                <TableCell colSpan={5} align="center">
                                    <CircularProgress size={40} />
                                </TableCell>
                            </TableRow>
                        ) : logs.length === 0 ? (
                            <TableRow>
                                <TableCell colSpan={5} align="center">
                                    No activity logs found
                                </TableCell>
                            </TableRow>
                        ) : (
                            logs.map((log) => {
                                const details = parseLogDetails(log.details);

                                return (
                                    <TableRow
                                        key={log.id}
                                        hover
                                        sx={log.isUnusual ? { backgroundColor: 'rgba(255, 0, 0, 0.05)' } : undefined}
                                    >
                                        <TableCell>{formatTimestamp(log.timestamp)}</TableCell>
                                        <TableCell>
                                            {log.user ? (
                                                <>
                                                    {log.user.name}
                                                    <Typography variant="body2" color="textSecondary">
                                                        {log.user.email}
                                                    </Typography>
                                                </>
                                            ) : (
                                                <Typography color="textSecondary">
                                                    (Anonymous)
                                                </Typography>
                                            )}
                                        </TableCell>
                                        <TableCell>
                                            <Chip
                                                label={log.action}
                                                size="small"
                                                color={getActionColor(log.action) as any}
                                            />
                                        </TableCell>
                                        <TableCell>
                                            {log.entityType}
                                            {log.entityId && ` #${log.entityId}`}
                                        </TableCell>
                                        <TableCell>
                                            {details && (
                                                <Typography variant="body2">
                                                    {details.path && `Path: ${details.path}`}
                                                    {details.method && ` (${details.method})`}
                                                    {details.statusCode && ` - Status: ${details.statusCode}`}
                                                    {details.responseTime && ` (${details.responseTime}ms)`}
                                                </Typography>
                                            )}
                                        </TableCell>
                                    </TableRow>
                                );
                            })
                        )}
                    </TableBody>
                </Table>
            </TableContainer>

            <TablePagination
                component="div"
                count={totalItems}
                page={page}
                onPageChange={handleChangePage}
                rowsPerPage={rowsPerPage}
                onRowsPerPageChange={handleChangeRowsPerPage}
                rowsPerPageOptions={[5, 10, 25, 50, 100]}
            />
        </Paper>
    );
};

export default ActivityLogs; 